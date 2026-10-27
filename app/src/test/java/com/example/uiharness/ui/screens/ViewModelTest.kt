package com.example.uiharness.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SignUpViewModelTest {

    @Test
    fun emptyFormCannotSubmit() {
        val vm = SignUpViewModel()
        assertFalse(vm.state.value.canSubmit)
        assertNull(vm.state.value.emailError)
    }

    @Test
    fun invalidEmailShowsErrorAndBlocksSubmit() {
        val vm = SignUpViewModel()
        vm.onNameChange("Ayşe")
        vm.onEmailChange("ayse@")
        assertEquals("Geçersiz e-posta", vm.state.value.emailError)
        assertFalse(vm.state.value.canSubmit)
    }

    @Test
    fun validFormCanSubmit() {
        val vm = SignUpViewModel()
        vm.onNameChange("Ayşe")
        vm.onEmailChange("ayse@example.com")
        assertNull(vm.state.value.emailError)
        assertTrue(vm.state.value.canSubmit)
    }

    @Test
    fun blankNameBlocksSubmitEvenWithValidEmail() {
        val vm = SignUpViewModel()
        vm.onNameChange("   ")
        vm.onEmailChange("ayse@example.com")
        assertFalse(vm.state.value.canSubmit)
    }
}

class ItemListViewModelTest {

    @Test
    fun startsEmpty() {
        assertTrue(ItemListViewModel().state.value.items.isEmpty())
    }

    @Test
    fun addItemTrimsAndAppends() {
        val vm = ItemListViewModel()
        vm.addItem("  Süt ")
        vm.addItem("Ekmek")
        assertEquals(listOf("Süt", "Ekmek"), vm.state.value.items)
    }

    @Test
    fun blankItemIsIgnored() {
        val vm = ItemListViewModel()
        vm.addItem("   ")
        assertTrue(vm.state.value.items.isEmpty())
    }
}
