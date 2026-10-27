package com.example.uiharness.notes

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NotesViewModelTest {

    @Test
    fun startsWithNoNotesAndNoSelection() {
        val vm = NotesViewModel(InMemoryNotesRepository())
        assertTrue(vm.state.value.notes.isEmpty())
        assertNull(vm.state.value.selectedNote)
    }

    @Test
    fun addNoteOpensEditorWithEmptyNote() {
        val vm = NotesViewModel(InMemoryNotesRepository())
        vm.addNote()
        assertEquals(1, vm.state.value.notes.size)
        assertEquals("", vm.state.value.selectedNote?.title)
    }

    @Test
    fun editingUpdatesSelectedNoteAndList() {
        val vm = NotesViewModel(InMemoryNotesRepository())
        vm.addNote()
        vm.updateTitle("Alışveriş")
        vm.updateBody("Süt")
        val listed = vm.state.value.notes.single()
        assertEquals("Alışveriş", listed.title)
        assertEquals("Süt", listed.body)
        assertEquals(listed, vm.state.value.selectedNote)
    }

    @Test
    fun newestNoteIsFirst() {
        val vm = NotesViewModel(InMemoryNotesRepository())
        vm.addNote()
        vm.updateTitle("İlk")
        vm.clearSelection()
        vm.addNote()
        vm.updateTitle("İkinci")
        assertEquals(listOf("İkinci", "İlk"), vm.state.value.notes.map { it.title })
    }

    @Test
    fun clearSelectionReturnsToList() {
        val vm = NotesViewModel(InMemoryNotesRepository())
        vm.addNote()
        vm.clearSelection()
        assertNull(vm.state.value.selectedNote)
        assertEquals(1, vm.state.value.notes.size)
    }

    @Test
    fun deleteRemovesSelectedNoteAndClearsSelection() {
        val vm = NotesViewModel(InMemoryNotesRepository())
        vm.addNote()
        vm.updateTitle("Silinecek")
        vm.deleteSelected()
        assertTrue(vm.state.value.notes.isEmpty())
        assertNull(vm.state.value.selectedNote)
    }

    @Test
    fun selectNoteFromListOpensIt() {
        val vm = NotesViewModel(InMemoryNotesRepository(sampleNotes()))
        vm.selectNote(2)
        assertEquals("Toplantı notları", vm.state.value.selectedNote?.title)
    }
}
