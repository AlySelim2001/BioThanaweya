package com.example

import com.example.domain.engine.BiologyMindMapProvider
import org.junit.Assert.*
import org.junit.Test

class MindMapCoverageTest {

    @Test
    fun testSarcomereMindMapIntegrity() {
        val map = BiologyMindMapProvider.getMindMapForTopic("sarcomere_contraction")
        assertNotNull(map)
        assertEquals("sarcomere_contraction", map.topicId)
        assertTrue(map.titleArabic.contains("القطعة العضلية") || map.titleArabic.contains("هكسلي"))
        assertTrue(map.mermaidCode.contains("graph TD") || map.mermaidCode.contains("flowchart"))
        assertTrue("Should have at least 5 nodes", map.nodes.size >= 5)
        assertTrue("Should have connecting edges", map.edges.isNotEmpty())

        val nodeIds = map.nodes.map { it.id }.toSet()
        map.edges.forEach { edge ->
            assertTrue("Edge source ${edge.fromId} must exist", nodeIds.contains(edge.fromId))
            assertTrue("Edge target ${edge.toId} must exist", nodeIds.contains(edge.toId))
        }

        map.nodes.forEach { node ->
            assertTrue("xRatio must be in [0..1]", node.xRatio in 0.0f..1.0f)
            assertTrue("yRatio must be in [0..1]", node.yRatio in 0.0f..1.0f)
            assertTrue("labelArabic must not be blank", node.labelArabic.isNotBlank())
            assertTrue("scientificSummary must not be blank", node.scientificSummary.isNotBlank())
        }
    }

    @Test
    fun testDnaReplicationMindMapIntegrity() {
        val map = BiologyMindMapProvider.getMindMapForTopic("dna_structure_replication")
        assertNotNull(map)
        assertTrue(map.mermaidCode.contains("Helicase") || map.mermaidCode.contains("اللولب"))
        assertTrue(map.highYieldExamTips.isNotEmpty())
    }

    @Test
    fun testImmunityMindMapIntegrity() {
        val map = BiologyMindMapProvider.getMindMapForTopic("immunity_humoral_cellular")
        assertNotNull(map)
        assertTrue(map.nodes.any { it.labelArabic.contains("تائية") || it.labelArabic.contains("MHC") })
        assertTrue(map.mermaidCode.contains("CD4") || map.mermaidCode.contains("CD8") || map.mermaidCode.contains("MHC"))
    }

    @Test
    fun testFallbackReturnsValidMindMap() {
        val map = BiologyMindMapProvider.getMindMapForTopic("unknown_topic_id")
        assertNotNull(map)
        assertTrue(map.nodes.isNotEmpty())
        assertTrue(map.mermaidCode.isNotBlank())
    }
}
