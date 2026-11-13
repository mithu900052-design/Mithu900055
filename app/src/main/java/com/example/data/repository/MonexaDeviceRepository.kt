package com.example.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import com.example.R
import com.example.data.model.DeviceDocument
import com.example.data.model.DeviceTelemetry
import com.example.data.model.PairingRequest
import com.example.data.model.PairingStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MonexaDeviceRepository(private val context: Context) {

    private val tag = "MonexaRepo"
    private val prefs = context.getSharedPreferences("monexa_device_prefs", Context.MODE_PRIVATE)

    // Mandatory custom database ID resolution
    private val db: FirebaseFirestore = try {
        FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
    } catch (e: Exception) {
        Log.e(tag, "Failed to resolve custom database ID, fallback default", e)
        FirebaseFirestore.getInstance()
    }

    private val auth = Firebase.auth
    private val secureRandom = SecureRandom()

    fun getStoredDeviceId(): String? {
        return prefs.getString("registered_device_id", null)
    }

    fun storeDeviceId(id: String) {
        prefs.edit().putString("registered_device_id", id).apply()
    }

    /**
     * Generates a 6-digit numeric device ID verified for uniqueness
     */
    suspend fun generateOrGetDeviceId(): String = withContext(Dispatchers.IO) {
        val existing = getStoredDeviceId()
        if (!existing.isNullOrBlank()) {
            return@withContext existing
        }

        var candidateId: String
        var isUnique = false
        var attempts = 0

        do {
            val num = 100000 + secureRandom.nextInt(900000)
            candidateId = num.toString()
            attempts++

            try {
                val query = db.collection("devices")
                    .whereEqualTo("deviceId", candidateId)
                    .limit(1)
                    .get()
                    .await()
                if (query.isEmpty) {
                    isUnique = true
                }
            } catch (e: Exception) {
                Log.w(tag, "Backend check offline or non-blocking, using candidate $candidateId", e)
                isUnique = true
            }
        } while (!isUnique && attempts < 10)

        storeDeviceId(candidateId)
        candidateId
    }

    /**
     * Reads actual on-device hardware telemetry
     */
    fun readLiveDeviceTelemetry(): DeviceTelemetry {
        // Battery info
        val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, batteryFilter)
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level >= 0 && scale > 0) (level * 100) / scale else 85
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        // Storage info
        var availableGb = 0.0
        var totalGb = 0.0
        try {
            val stat = StatFs(Environment.getDataDirectory().path)
            val bytesAvailable = stat.availableBlocksLong * stat.blockSizeLong
            val bytesTotal = stat.blockCountLong * stat.blockSizeLong
            availableGb = String.format(Locale.US, "%.1f", bytesAvailable / (1024.0 * 1024.0 * 1024.0)).toDouble()
            totalGb = String.format(Locale.US, "%.1f", bytesTotal / (1024.0 * 1024.0 * 1024.0)).toDouble()
        } catch (e: Exception) {
            Log.e(tag, "Failed to read storage", e)
        }

        // Network info
        var networkType = "Disconnected"
        var isOnline = false
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val network = cm?.activeNetwork
            val capabilities = cm?.getNetworkCapabilities(network)
            if (capabilities != null) {
                isOnline = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                networkType = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi (High Speed)"
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular (4G/5G)"
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                    else -> "Connected"
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to read network status", e)
        }

        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        return DeviceTelemetry(
            batteryPercentage = batteryPct,
            isCharging = isCharging,
            availableStorageGb = availableGb,
            totalStorageGb = totalGb,
            networkType = networkType,
            isOnline = isOnline,
            model = Build.MODEL ?: "Android Device",
            manufacturer = Build.MANUFACTURER ?: "Generic",
            androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            lastSyncFormatted = timeStr
        )
    }

    /**
     * Registers device in backend Firestore database
     */
    suspend fun registerDeviceInBackend(
        deviceId: String,
        customerName: String,
        customerAge: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val telemetry = readLiveDeviceTelemetry()
            val userId = auth.currentUser?.uid ?: "device_client_$deviceId"
            val deviceDoc = hashMapOf(
                "deviceId" to deviceId,
                "userId" to userId,
                "customerName" to customerName,
                "customerAge" to customerAge,
                "pairingStatus" to PairingStatus.UNPAIRED.name,
                "approvedAdminId" to null,
                "model" to telemetry.model,
                "manufacturer" to telemetry.manufacturer,
                "androidVersion" to telemetry.androidVersion,
                "appVersion" to "1.0.0",
                "batteryPercentage" to telemetry.batteryPercentage,
                "isCharging" to telemetry.isCharging,
                "availableStorageGb" to telemetry.availableStorageGb,
                "totalStorageGb" to telemetry.totalStorageGb,
                "networkType" to telemetry.networkType,
                "isOnline" to true,
                "locationSharingEnabled" to false,
                "screenSharingEnabled" to false,
                "usageAccessGranted" to false,
                "lastSyncTime" to FieldValue.serverTimestamp(),
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )

            db.collection("devices").document(deviceId).set(deviceDoc, SetOptions.merge()).await()
            Log.d(tag, "Device $deviceId registered successfully in Firestore")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Failed to register device in Firestore", e)
            Result.failure(e)
        }
    }

    /**
     * Observes pairing requests for this device
     */
    fun observePendingPairingRequests(deviceId: String): Flow<List<PairingRequest>> {
        return db.collection("device_pairings")
            .whereEqualTo("deviceId", deviceId)
            .whereEqualTo("status", "PENDING")
            .snapshots()
            .map { snap -> snap.toObjects(PairingRequest::class.java) }
            .catch { e ->
                Log.e(tag, "Error observing pairing requests: ${e.message}")
                emit(emptyList())
            }
            .flowOn(Dispatchers.IO)
    }

    /**
     * Approves an admin pairing request
     */
    suspend fun approvePairing(deviceId: String, pairingId: String, adminId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            db.collection("device_pairings").document(pairingId).update(
                mapOf(
                    "status" to "APPROVED",
                    "respondedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            db.collection("devices").document(deviceId).update(
                mapOf(
                    "pairingStatus" to PairingStatus.PAIRED.name,
                    "approvedAdminId" to adminId,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Error approving pairing", e)
            Result.failure(e)
        }
    }

    /**
     * Rejects an admin pairing request
     */
    suspend fun rejectPairing(pairingId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            db.collection("device_pairings").document(pairingId).update(
                mapOf(
                    "status" to "REJECTED",
                    "respondedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Error rejecting pairing", e)
            Result.failure(e)
        }
    }

    /**
     * Customer revokes admin access at any time
     */
    suspend fun revokePairing(deviceId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            db.collection("devices").document(deviceId).update(
                mapOf(
                    "pairingStatus" to PairingStatus.REVOKED.name,
                    "approvedAdminId" to null,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(tag, "Error revoking pairing", e)
            Result.failure(e)
        }
    }
}
