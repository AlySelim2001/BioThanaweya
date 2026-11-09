package com.example

import com.example.data.repository.CellBiologyDataProvider
import com.example.domain.model.CellType
import com.example.domain.model.OrganelleType
import com.example.domain.model.Vector3D
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.abs

class CellBiologySimulationTest {

    @Test
    fun testVector3DRotationMath() {
        val original = Vector3D(10f, 0f, 0f)
        // 90 degrees around Y axis (pi / 2)
        val rotated = original.rotateY((Math.PI / 2).toFloat())
        assertEquals(0f, rotated.x, 0.01f)
        assertEquals(0f, rotated.y, 0.01f)
        assertEquals(-10f, rotated.z, 0.01f)
    }

    @Test
    fun testAnimalCellOrganellesCoverage() {
        val animalOrganelles = CellBiologyDataProvider.getOrganellesForCell(CellType.ANIMAL_CELL)
        val types = animalOrganelles.map { it.type }

        // Must contain core eukaryotic animal organelles
        assertTrue(types.contains(OrganelleType.NUCLEUS))
        assertTrue(types.contains(OrganelleType.MITOCHONDRIA))
        assertTrue(types.contains(OrganelleType.ROUGH_ER))
        assertTrue(types.contains(OrganelleType.SMOOTH_ER))
        assertTrue(types.contains(OrganelleType.GOLGI_APPARATUS))
        assertTrue(types.contains(OrganelleType.LYSOSOMES))
        assertTrue(types.contains(OrganelleType.CENTROSOME))

        // Animal cell must NOT contain chloroplast
        assertFalse(types.contains(OrganelleType.CHLOROPLAST))
    }

    @Test
    fun testPlantCellOrganellesCoverage() {
        val plantOrganelles = CellBiologyDataProvider.getOrganellesForCell(CellType.PLANT_CELL)
        val types = plantOrganelles.map { it.type }

        // Must contain plant specific organelles
        assertTrue(types.contains(OrganelleType.CHLOROPLAST))
        assertTrue(types.contains(OrganelleType.CENTRAL_VACUOLE))

        // Plant cells in Thanaweya Amma curriculum do NOT contain typical centrosomes
        assertFalse(types.contains(OrganelleType.CENTROSOME))
        assertFalse(types.contains(OrganelleType.LYSOSOMES))
    }

    @Test
    fun testAllOrganellesHaveCompleteEducationalDataAndQuizzes() {
        OrganelleType.values().forEach { organelleType ->
            val detail = CellBiologyDataProvider.organelleDetails[organelleType]
            assertNotNull("Detail must exist for organelle $organelleType", detail)
            assertTrue("Arabic name must not be blank", detail!!.arabicName.isNotBlank())
            assertTrue("English name must not be blank", detail.englishName.isNotBlank())
            assertTrue("Primary function must be comprehensive", detail.primaryFunction.length > 20)
            assertTrue("Detailed structure must be rich", detail.detailedStructure.length > 20)
            assertTrue("Ministry trap must provide valuable exam insight", detail.ministryExamTrap.length > 20)
            assertTrue("Analogy must be engaging", detail.eli5Analogy.isNotBlank())
            assertTrue("Key reactions must be present", detail.keyReactions.isNotEmpty())

            // Quiz checks
            val quiz = detail.microQuiz
            assertTrue("Quiz options must have at least 3 choices", quiz.options.size >= 3)
            assertTrue("Correct index must be within range", quiz.correctIndex in quiz.options.indices)
            assertTrue("Explanation must be detailed", quiz.explanationArabic.length > 10)
        }
    }

    @Test
    fun test3DInstancesGeneratedProperly() {
        val animalInstances = CellBiologyDataProvider.get3DInstances(CellType.ANIMAL_CELL)
        assertTrue(animalInstances.size >= 8)

        val plantInstances = CellBiologyDataProvider.get3DInstances(CellType.PLANT_CELL)
        assertTrue(plantInstances.size >= 8)

        // Ensure nucleus is positioned centrally
        val nucleus = animalInstances.find { it.type == OrganelleType.NUCLEUS }
        assertNotNull(nucleus)
        assertEquals(0f, nucleus!!.localPosition.x, 0.1f)
        assertEquals(0f, nucleus.localPosition.y, 0.1f)
        assertEquals(0f, nucleus.localPosition.z, 0.1f)
    }
}
