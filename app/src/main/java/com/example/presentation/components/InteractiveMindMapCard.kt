package com.example.presentation.components

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.domain.model.BiologicalMindMap
import com.example.domain.model.MindMapNode
import com.example.domain.model.MindMapNodeType
import com.example.ui.theme.*

@Composable
fun InteractiveMindMapCard(
    mindMap: BiologicalMindMap,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedViewMode by remember { mutableIntStateOf(0) } // 0: Interactive Canvas, 1: Mermaid Flowchart, 2: Syntax Code
    var selectedNode by remember { mutableStateOf<MindMapNode?>(mindMap.nodes.firstOrNull()) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("mind_map_card_${mindMap.topicId}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title & Central Question
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MolecularPurple.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccountTree,
                        contentDescription = "Mind Map",
                        tint = MolecularPurple
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mindMap.titleArabic,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = mindMap.titleEnglish,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Central Inquiry Question
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mindMap.centralQuestionArabic,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // View Mode Switcher
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = selectedViewMode == 0,
                    onClick = { selectedViewMode = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                    icon = { Icon(Icons.Default.Hub, contentDescription = null, modifier = Modifier.size(16.dp)) }
                ) {
                    Text("شبكة تفاعلية", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                SegmentedButton(
                    selected = selectedViewMode == 1,
                    onClick = { selectedViewMode = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                    icon = { Icon(Icons.Default.Schema, contentDescription = null, modifier = Modifier.size(16.dp)) }
                ) {
                    Text("Mermaid", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                SegmentedButton(
                    selected = selectedViewMode == 2,
                    onClick = { selectedViewMode = 2 },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                    icon = { Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp)) }
                ) {
                    Text("كود المخطط", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Content Area based on View Mode
            when (selectedViewMode) {
                0 -> {
                    // Canvas Graph View
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw connecting edges
                            mindMap.edges.forEach { edge ->
                                val from = mindMap.nodes.find { it.id == edge.fromId }
                                val to = mindMap.nodes.find { it.id == edge.toId }
                                if (from != null && to != null) {
                                    val start = Offset(from.xRatio * w, from.yRatio * h)
                                    val end = Offset(to.xRatio * w, to.yRatio * h)
                                    val isSelected = selectedNode?.id == from.id || selectedNode?.id == to.id

                                    drawLine(
                                        color = if (edge.isHighlighted || isSelected) BioTealPrimary else Color.Gray.copy(alpha = 0.45f),
                                        start = start,
                                        end = end,
                                        strokeWidth = if (edge.isHighlighted || isSelected) 3.5f else 1.8f,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }

                        // Render clickable node pins
                        mindMap.nodes.forEach { node ->
                            val isSelected = selectedNode?.id == node.id
                            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                val xPos = (maxWidth.value * node.xRatio).dp
                                val yPos = (maxHeight.value * node.yRatio).dp

                                Box(
                                    modifier = Modifier
                                        .offset(x = xPos - 38.dp, y = yPos - 18.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when (node.type) {
                                                MindMapNodeType.ROOT -> BioTealPrimary
                                                MindMapNodeType.KEY_MOLECULE -> MolecularPurple
                                                MindMapNodeType.EXAM_TRAP -> AlertCoral
                                                MindMapNodeType.REGULATION -> ExamGold
                                                MindMapNodeType.MECHANISM -> Color(0xFF1E88E5)
                                            }
                                        )
                                        .border(
                                            width = if (isSelected) 2.5.dp else 0.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedNode = node }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .testTag("mind_map_node_${node.id}")
                                ) {
                                    Text(
                                        text = node.labelArabic,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Selected Node Details Card
                    selectedNode?.let { node ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = node.labelArabic,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when (node.type) {
                                            MindMapNodeType.ROOT -> BioTealPrimary.copy(alpha = 0.2f)
                                            MindMapNodeType.KEY_MOLECULE -> MolecularPurple.copy(alpha = 0.2f)
                                            MindMapNodeType.EXAM_TRAP -> AlertCoral.copy(alpha = 0.2f)
                                            MindMapNodeType.REGULATION -> ExamGold.copy(alpha = 0.2f)
                                            MindMapNodeType.MECHANISM -> Color(0xFF1E88E5).copy(alpha = 0.2f)
                                        }
                                    ) {
                                        Text(
                                            text = node.type.labelArabic,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = node.scientificSummary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // WebView Mermaid Diagram Viewer
                    MermaidWebViewContainer(
                        mermaidCode = mindMap.mermaidCode,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                    )
                }

                2 -> {
                    // Raw Mermaid Syntax with Copy Action
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E1E1E))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Mermaid.js Flowchart Code:",
                                color = Color(0xFF81C784),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Mermaid Diagram", mindMap.mermaidCode)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "تم نسخ كود Mermaid للحافظة!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy Code",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = mindMap.mermaidCode,
                            color = Color(0xFFECEFF1),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // High-Yield Ministry Exam Tips
            if (mindMap.highYieldExamTips.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ExamGoldLight.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ExamGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = DeepNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "نقاط ذهبية للامتحانات الوزارية:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DeepNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        mindMap.highYieldExamTips.forEach { tip ->
                            Text(
                                text = "• $tip",
                                fontSize = 11.sp,
                                color = DeepNavy,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun MermaidWebViewContainer(
    mermaidCode: String,
    modifier: Modifier = Modifier
) {
    val escapedCode = mermaidCode
        .replace("\\", "\\\\")
        .replace("`", "\\`")

    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
            <script src="https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.min.js"></script>
            <style>
                body {
                    margin: 0;
                    padding: 12px;
                    background-color: #FAFAFA;
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    min-height: 100vh;
                }
                .mermaid {
                    width: 100%;
                    text-align: center;
                }
            </style>
            <script>
                mermaid.initialize({
                    startOnLoad: true,
                    theme: 'default',
                    flowchart: { curve: 'basis', useMaxWidth: true }
                });
            </script>
        </head>
        <body>
            <div class="mermaid">
                $escapedCode
            </div>
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                webViewClient = WebViewClient()
                loadDataWithBaseURL("https://cdn.jsdelivr.net", htmlContent, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL("https://cdn.jsdelivr.net", htmlContent, "text/html", "UTF-8", null)
        }
    )
}
