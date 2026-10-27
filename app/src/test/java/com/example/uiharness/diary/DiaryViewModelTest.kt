package com.example.uiharness.diary

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DiaryViewModelTest {

    // Sabit saat: testler gerçek saate bağlı kalmasın.
    private fun viewModel(seed: List<DiaryEntry> = emptyList()) =
        DiaryViewModel(InMemoryDiaryRepository(seed), clock = { 1_000L })

    @Test
    fun startsWithNoEntriesAndNoSelection() {
        val vm = viewModel()
        assertTrue(vm.state.value.entries.isEmpty())
        assertNull(vm.state.value.selectedEntry)
    }

    @Test
    fun addEntryStampsCurrentTimeAndOpensEditor() {
        val vm = viewModel()
        vm.addEntry()
        assertEquals(1, vm.state.value.entries.size)
        assertEquals(1_000L, vm.state.value.selectedEntry?.createdAtMillis)
        assertEquals("", vm.state.value.selectedEntry?.title)
    }

    @Test
    fun editingUpdatesSelectedEntryAndList() {
        val vm = viewModel()
        vm.addEntry()
        vm.updateTitle("Bugün")
        vm.updateBody("Güzel bir gündü")
        val listed = vm.state.value.entries.single()
        assertEquals("Bugün", listed.title)
        assertEquals("Güzel bir gündü", listed.body)
        assertEquals(listed, vm.state.value.selectedEntry)
    }

    @Test
    fun newestEntryIsFirst() {
        var now = 1L
        val vm = DiaryViewModel(InMemoryDiaryRepository(), clock = { now })
        vm.addEntry()
        vm.updateTitle("İlk")
        vm.clearSelection()
        now = 2L
        vm.addEntry()
        vm.updateTitle("İkinci")
        assertEquals(listOf("İkinci", "İlk"), vm.state.value.entries.map { it.title })
    }

    @Test
    fun clearSelectionReturnsToList() {
        val vm = viewModel()
        vm.addEntry()
        vm.clearSelection()
        assertNull(vm.state.value.selectedEntry)
        assertEquals(1, vm.state.value.entries.size)
    }

    @Test
    fun deleteRemovesSelectedEntryAndClearsSelection() {
        val vm = viewModel()
        vm.addEntry()
        vm.updateTitle("Silinecek")
        vm.deleteSelected()
        assertTrue(vm.state.value.entries.isEmpty())
        assertNull(vm.state.value.selectedEntry)
    }

    @Test
    fun selectEntryFromListOpensIt() {
        val vm = viewModel(sampleDiaryEntries())
        vm.selectEntry(2)
        assertEquals("Yürüyüş", vm.state.value.selectedEntry?.title)
    }
}
