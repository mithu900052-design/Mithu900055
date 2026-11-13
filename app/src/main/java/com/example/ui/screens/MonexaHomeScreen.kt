package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceTelemetry
import com.example.data.model.MonexaLanguage
import com.example.data.model.MonexaStrings
import com.example.data.model.PairingRequest
import com.example.data.model.PairingStatus
import com.example.data.repository.MonexaDeviceRepository
import com.example.ui.components.DeviceIdCard
import com.example.ui.components.MonexaHeader
import com.example.ui.components.PairingApprovalDialog
import com.example.ui.components.TelemetryCards
import com.example.ui.theme.MonexaCardBorder
import com.example.ui.theme.MonexaCyanPrimary
import com.example.ui.theme.MonexaElectricBlue
import com.example.ui.theme.MonexaEmeraldSuccess
import com.example.ui.theme.MonexaWarningAmber
import kotlinx.coroutines.launch

@Composable
fun MonexaHomeScreen(
    repository: MonexaDeviceRepository,
    currentLanguage: MonexaLanguage,
    isDarkTheme: Boolean,
    onLanguageToggle: () -> Unit,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var deviceId by remember { mutableStateOf(repository.getStoredDeviceId() ?: "") }
    var pairingStatus by remember { mutableStateOf(PairingStatus.UNPAIRED) }
    var telemetry by remember { mutableStateOf(repository.readLiveDeviceTelemetry()) }
    var activePairingRequest by remember { mutableStateOf<PairingRequest?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Initialize or load device ID and register initial telemetry
    LaunchedEffect(Unit) {
        if (deviceId.isEmpty()) {
            isLoading = true
            val generatedId = repository.generateOrGetDeviceId()
            deviceId = generatedId
            repository.registerDeviceInBackend(generatedId, "Customer Device", 28)
            isLoading = false
        }
    }

    // Observe live pending pairing requests for this deviceId
    val pendingRequestsFlow = remember(deviceId) {
        if (deviceId.isNotEmpty()) {
            repository.observePendingPairingRequests(deviceId)
        } else null
    }
    val pendingRequests by pendingRequestsFlow?.collectAsState(initial = emptyList())
        ?: remember { mutableStateOf(emptyList()) }

    LaunchedEffect(pendingRequests) {
        if (pendingRequests.isNotEmpty()) {
            activePairingRequest = pendingRequests.first()
            pairingStatus = PairingStatus.PENDING_APPROVAL
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("monexa_home_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MonexaHeader(
                currentLanguage = currentLanguage,
                isDarkTheme = isDarkTheme,
                onLanguageToggle = onLanguageToggle,
                onThemeToggle = onThemeToggle
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Phase 1 Foundation Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MonexaElectricBlue.copy(alpha = 0.12f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(MonexaCyanPrimary.copy(alpha = 0.4f), MonexaElectricBlue.copy(alpha = 0.2f))
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MonexaCyanPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "Cloud Connected",
                            tint = MonexaCyanPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = MonexaStrings.phase1Notice(currentLanguage),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MonexaCyanPrimary
                        )
                        Text(
                            text = "Firestore DB: Active • Zero-Trust Rules: Enforced",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 6-digit Device ID Holographic Card
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MonexaCyanPrimary)
                }
            } else {
                DeviceIdCard(
                    deviceId = deviceId,
                    pairingStatus = pairingStatus,
                    currentLanguage = currentLanguage,
                    onCopyDeviceId = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Monexa Device ID", deviceId)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, MonexaStrings.copiedToast(currentLanguage), Toast.LENGTH_SHORT).show()
                    },
                    onRevokeAccess = {
                        scope.launch {
                            repository.revokePairing(deviceId)
                            pairingStatus = PairingStatus.REVOKED
                            Toast.makeText(context, MonexaStrings.statusRevoked(currentLanguage), Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }

            // Live Hardware Telemetry Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = MonexaStrings.telemetryOverview(currentLanguage),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedButton(
                    onClick = {
                        telemetry = repository.readLiveDeviceTelemetry()
                        scope.launch {
                            repository.registerDeviceInBackend(deviceId, "Customer Device", 28)
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MonexaCyanPrimary),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(MonexaCyanPrimary.copy(alpha = 0.5f), MonexaCyanPrimary.copy(alpha = 0.5f)))
                    ),
                    modifier = Modifier.testTag("refresh_telemetry_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentLanguage == MonexaLanguage.ENGLISH) "Refresh" else "রিফ্রেশ",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            TelemetryCards(
                telemetry = telemetry,
                currentLanguage = currentLanguage
            )

            // Test Simulation / Quick Demo for Pairing Flow
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(MonexaCardBorder)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MonexaWarningAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == MonexaLanguage.ENGLISH)
                                "Pairing Simulation & Diagnostics"
                            else
                                "পেয়ারিং সিমুলেশন ও ডায়াগনস্টিকস",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (currentLanguage == MonexaLanguage.ENGLISH)
                            "Admins connect by entering your 6-digit Device ID in the Web Admin Dashboard. You can simulate an incoming pairing request below to verify customer approval controls."
                        else
                            "অ্যাডমিনরা ওয়েব ড্যাশবোর্ডে আপনার ৬-সংখ্যার ডিভাইস আইডি প্রবেশ করিয়ে অনুরোধ পাঠান। গ্রাহক অনুমোদন পরীক্ষা করতে নিচে সিমুলেশন বোতাম চাপুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                activePairingRequest = PairingRequest(
                                    id = "sim_req_${System.currentTimeMillis()}",
                                    deviceId = deviceId,
                                    adminId = "admin_hq_01",
                                    adminName = "Monexa Security Desk Alpha",
                                    status = "PENDING"
                                )
                                pairingStatus = PairingStatus.PENDING_APPROVAL
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_pairing_button"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MonexaWarningAmber)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == MonexaLanguage.ENGLISH) "Simulate Admin Request" else "অনুরোধ সিমুলেট করুন",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            // Privacy & Consent Safeguards Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(MonexaCardBorder)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security",
                            tint = MonexaEmeraldSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = MonexaStrings.privacyConsentHeader(currentLanguage),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = MonexaStrings.privacyConsentDesc(currentLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Pairing Approval Dialog
    activePairingRequest?.let { request ->
        PairingApprovalDialog(
            request = request,
            currentLanguage = currentLanguage,
            onApprove = { req ->
                scope.launch {
                    repository.approvePairing(deviceId, req.id, req.adminId)
                    pairingStatus = PairingStatus.PAIRED
                    activePairingRequest = null
                    Toast.makeText(
                        context,
                        if (currentLanguage == MonexaLanguage.ENGLISH) "Pairing approved successfully!" else "পেয়ারিং সফলভাবে অনুমোদিত হয়েছে!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onReject = { req ->
                scope.launch {
                    repository.rejectPairing(req.id)
                    pairingStatus = PairingStatus.UNPAIRED
                    activePairingRequest = null
                    Toast.makeText(
                        context,
                        if (currentLanguage == MonexaLanguage.ENGLISH) "Pairing request rejected" else "পেয়ারিং অনুরোধ বাতিল করা হয়েছে",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onDismiss = {
                activePairingRequest = null
            }
        )
    }
}
