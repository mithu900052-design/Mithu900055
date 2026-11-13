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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MonexaLanguage
import com.example.data.model.MonexaStrings
import com.example.data.model.PairingRequest
import com.example.ui.theme.MonexaCardBorder
import com.example.ui.theme.MonexaCyanPrimary
import com.example.ui.theme.MonexaEmeraldSuccess
import com.example.ui.theme.MonexaErrorRed
import com.example.ui.theme.MonexaWarningAmber

@Composable
fun PairingApprovalDialog(
    request: PairingRequest,
    currentLanguage: MonexaLanguage,
    onApprove: (PairingRequest) -> Unit,
    onReject: (PairingRequest) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("pairing_approval_dialog"),
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MonexaWarningAmber.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Request",
                        tint = MonexaWarningAmber,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (currentLanguage == MonexaLanguage.ENGLISH) "Admin Pairing Request" else "অ্যাডমিন পেয়ারিং অনুরোধ",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Device ID: ${request.deviceId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MonexaCyanPrimary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = if (currentLanguage == MonexaLanguage.ENGLISH)
                        "An authorized administrator (${request.adminName}) has requested to pair with your device."
                    else
                        "একজন অনুমোদিত অ্যাডমিনিস্ট্রেটর (${request.adminName}) আপনার ডিভাইসের সাথে সংযোগ স্থাপন করতে অনুরোধ করেছেন।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MonexaCardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Consent Notice",
                            tint = MonexaCyanPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == MonexaLanguage.ENGLISH)
                                "If approved, the admin will be able to view permitted telemetry. You can revoke access at any time."
                            else
                                "অনুমোদিত হলে অ্যাডমিন অনুমোদিত টেলিমেট্রি দেখতে পারবেন। আপনি যেকোনো সময় সংযোগটি বাতিল করতে পারবেন।",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onApprove(request) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MonexaEmeraldSuccess,
                    contentColor = Color(0xFF00240B)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_approve_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = MonexaStrings.approve(currentLanguage),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onReject(request) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MonexaErrorRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_reject_button")
            ) {
                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = MonexaStrings.reject(currentLanguage),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    )
}
