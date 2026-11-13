package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MonexaLanguage
import com.example.data.model.MonexaStrings
import com.example.data.model.PairingStatus
import com.example.ui.theme.MonexaCardBorder
import com.example.ui.theme.MonexaCyanPrimary
import com.example.ui.theme.MonexaElectricBlue
import com.example.ui.theme.MonexaEmeraldSuccess
import com.example.ui.theme.MonexaErrorRed
import com.example.ui.theme.MonexaWarningAmber

@Composable
fun DeviceIdCard(
    deviceId: String,
    pairingStatus: PairingStatus,
    currentLanguage: MonexaLanguage,
    onCopyDeviceId: () -> Unit,
    onRevokeAccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("device_id_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(MonexaCyanPrimary.copy(alpha = 0.6f), MonexaElectricBlue.copy(alpha = 0.3f))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Chip
            val (statusColor, statusText) = when (pairingStatus) {
                PairingStatus.UNPAIRED -> Pair(MonexaCyanPrimary, MonexaStrings.statusUnpaired(currentLanguage))
                PairingStatus.PENDING_APPROVAL -> Pair(MonexaWarningAmber, MonexaStrings.statusPending(currentLanguage))
                PairingStatus.PAIRED -> Pair(MonexaEmeraldSuccess, MonexaStrings.statusPaired(currentLanguage))
                PairingStatus.REVOKED -> Pair(MonexaErrorRed, MonexaStrings.statusRevoked(currentLanguage))
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = MonexaStrings.deviceIdLabel(currentLanguage),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 6-digit numeric Device ID display container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .border(1.dp, MonexaCardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (deviceId.length == 6) {
                        "${deviceId.substring(0, 3)} ${deviceId.substring(3, 6)}"
                    } else deviceId.ifEmpty { "------" },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 6.sp,
                        fontSize = 34.sp
                    ),
                    color = MonexaCyanPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Copy Device ID Button
                Button(
                    onClick = onCopyDeviceId,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("copy_device_id_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MonexaCyanPrimary,
                        contentColor = Color(0xFF001424)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = MonexaStrings.copyButton(currentLanguage),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Revoke access button if currently paired
                if (pairingStatus == PairingStatus.PAIRED) {
                    OutlinedButton(
                        onClick = onRevokeAccess,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("revoke_pairing_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MonexaErrorRed
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(listOf(MonexaErrorRed, MonexaErrorRed))
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.LinkOff,
                            contentDescription = "Revoke",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = MonexaStrings.revoke(currentLanguage),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
