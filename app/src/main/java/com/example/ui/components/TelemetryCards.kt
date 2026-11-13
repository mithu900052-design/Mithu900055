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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceTelemetry
import com.example.data.model.MonexaLanguage
import com.example.data.model.MonexaStrings
import com.example.ui.theme.MonexaCardBorder
import com.example.ui.theme.MonexaCyanPrimary
import com.example.ui.theme.MonexaElectricBlue
import com.example.ui.theme.MonexaEmeraldSuccess
import com.example.ui.theme.MonexaWarningAmber

@Composable
fun TelemetryCards(
    telemetry: DeviceTelemetry,
    currentLanguage: MonexaLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Battery Metric Card
            TelemetryItemCard(
                title = MonexaStrings.battery(currentLanguage),
                value = "${telemetry.batteryPercentage}%",
                subtitle = if (telemetry.isCharging) MonexaStrings.charging(currentLanguage) else MonexaStrings.discharging(currentLanguage),
                icon = if (telemetry.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                iconColor = if (telemetry.batteryPercentage < 20) MonexaWarningAmber else MonexaEmeraldSuccess,
                modifier = Modifier.weight(1f)
            )

            // Storage Metric Card
            TelemetryItemCard(
                title = MonexaStrings.storage(currentLanguage),
                value = "${telemetry.availableStorageGb} GB",
                subtitle = "Total ${telemetry.totalStorageGb} GB",
                icon = Icons.Default.Storage,
                iconColor = MonexaCyanPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Network Metric Card
            TelemetryItemCard(
                title = MonexaStrings.network(currentLanguage),
                value = if (telemetry.isOnline) "Connected" else "Offline",
                subtitle = telemetry.networkType,
                icon = if (telemetry.networkType.contains("Cellular")) Icons.Default.SignalCellularAlt else Icons.Default.Wifi,
                iconColor = if (telemetry.isOnline) MonexaEmeraldSuccess else Color.Gray,
                modifier = Modifier.weight(1f)
            )

            // OS & Model Metric Card
            TelemetryItemCard(
                title = MonexaStrings.osVersion(currentLanguage),
                value = telemetry.model,
                subtitle = telemetry.androidVersion,
                icon = Icons.Default.Android,
                iconColor = MonexaElectricBlue,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TelemetryItemCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
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
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
