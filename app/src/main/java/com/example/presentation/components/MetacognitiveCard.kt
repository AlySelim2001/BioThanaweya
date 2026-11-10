package com.example.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MetacognitiveAnalysis
import com.example.domain.model.MetacognitiveGapType
import com.example.ui.theme.AlertCoral
import com.example.ui.theme.BioTealPrimary
import com.example.ui.theme.ExamGold
import com.example.ui.theme.SuccessGreen

@Composable
fun MetacognitiveCard(
    analysis: MetacognitiveAnalysis,
    modifier: Modifier = Modifier
) {
    val (headerColor, cardBg) = when (analysis.gapType) {
        MetacognitiveGapType.CALIBRATED_MASTERY -> SuccessGreen to SuccessGreen.copy(alpha = 0.1f)
        MetacognitiveGapType.OVERCONFIDENCE_BIAS -> AlertCoral to AlertCoral.copy(alpha = 0.1f)
        MetacognitiveGapType.UNDERCONFIDENCE_DOUBT -> ExamGold to ExamGold.copy(alpha = 0.1f)
        MetacognitiveGapType.CALIBRATED_HUMILITY -> BioTealPrimary to BioTealPrimary.copy(alpha = 0.1f)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, headerColor.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Metacognitive Agent",
                        tint = headerColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Agent 5: Metacognitive Evaluator",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = headerColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = headerColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Confidence: ${analysis.confidencePercent}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = headerColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = analysis.feedbackTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = analysis.actionableAdvice,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
