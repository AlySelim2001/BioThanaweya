package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BioTealPrimary
import com.example.ui.theme.ExamGold
import com.example.ui.theme.MolecularPurple
import com.example.ui.theme.SuccessGreen

@Composable
fun SevenAgentBadge(
    agentNumber: Int,
    title: String = "",
    subtitle: String = "",
    agentTitleArabic: String = title,
    agentRoleEnglish: String = subtitle,
    modifier: Modifier = Modifier
) {
    val displayTitle = if (agentTitleArabic.isNotBlank()) agentTitleArabic else title
    val displaySubtitle = if (agentRoleEnglish.isNotBlank()) agentRoleEnglish else subtitle
    val agentColor = when (agentNumber) {
        1 -> BioTealPrimary
        2 -> Color(0xFF0288D1)
        3 -> ExamGold
        4 -> SuccessGreen
        5 -> MolecularPurple
        6 -> Color(0xFF00897B)
        else -> Color(0xFFE91E63)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = agentColor.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, agentColor.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(agentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A$agentNumber",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = displaySubtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
