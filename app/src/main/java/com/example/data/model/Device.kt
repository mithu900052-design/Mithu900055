package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class PairingStatus {
    UNPAIRED,
    PENDING_APPROVAL,
    PAIRED,
    REVOKED
}

@IgnoreExtraProperties
data class DeviceDocument(
    @DocumentId val id: String = "",
    val deviceId: String = "", // 6-digit numeric ID (e.g. 827491)
    val userId: String = "",
    val customerName: String = "",
    val customerAge: Int = 0,
    val pairingStatus: String = PairingStatus.UNPAIRED.name,
    val approvedAdminId: String? = null,
    val model: String = "",
    val manufacturer: String = "",
    val androidVersion: String = "",
    val appVersion: String = "1.0.0",
    val batteryPercentage: Int = 100,
    val isCharging: Boolean = false,
    val availableStorageGb: Double = 0.0,
    val totalStorageGb: Double = 0.0,
    val networkType: String = "WIFI",
    val isOnline: Boolean = true,
    val locationSharingEnabled: Boolean = false,
    val screenSharingEnabled: Boolean = false,
    val usageAccessGranted: Boolean = false,
    @ServerTimestamp val lastSyncTime: Timestamp? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class DeviceTelemetry(
    val batteryPercentage: Int = 100,
    val isCharging: Boolean = false,
    val availableStorageGb: Double = 0.0,
    val totalStorageGb: Double = 0.0,
    val networkType: String = "Wi-Fi",
    val isOnline: Boolean = true,
    val model: String = "",
    val manufacturer: String = "",
    val androidVersion: String = "",
    val lastSyncFormatted: String = "Just now"
)

@IgnoreExtraProperties
data class PairingRequest(
    @DocumentId val id: String = "",
    val deviceId: String = "",
    val adminId: String = "",
    val adminName: String = "Monexa Operations Desk",
    val userId: String = "",
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED, REVOKED
    @ServerTimestamp val requestedAt: Timestamp? = null,
    @ServerTimestamp val respondedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ScreenSessionRecord(
    @DocumentId val id: String = "",
    val deviceId: String = "",
    val userId: String = "",
    val status: String = "STOPPED", // ACTIVE, PAUSED, STOPPED
    @ServerTimestamp val startedAt: Timestamp? = null,
    @ServerTimestamp val endedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class AuditLogEntry(
    @DocumentId val id: String = "",
    val action: String = "",
    val actorId: String = "",
    val targetDeviceId: String = "",
    val details: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null
)
