package com.example.lifeos.ui.screens.money

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.money.TransactionCategory
import com.example.lifeos.data.money.TransactionRepository
import com.example.lifeos.data.money.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: TransactionRepository,
) : ViewModel() {

    var type by mutableStateOf(TransactionType.EXPENSE)
        private set
    var category by mutableStateOf(TransactionCategory.FOOD)
        private set
    var amountText by mutableStateOf("")
        private set
    var date by mutableStateOf(LocalDate.now())
        private set
    var note by mutableStateOf("")
        private set

    val amount: Long get() = amountText.toLongOrNull() ?: 0L
    val canSave: Boolean get() = amount > 0

    fun onTypeSelected(value: TransactionType) {
        type = value
        category = TransactionCategory.forType(value).first()
    }

    fun onCategorySelected(value: TransactionCategory) {
        category = value
    }

    fun onAmountChange(value: String) {
        amountText = value.filter { it.isDigit() }
    }

    fun onDateSelected(value: LocalDate) {
        date = value
    }

    fun onNoteChange(value: String) {
        note = value
    }

    fun save(onSaved: () -> Unit) {
        if (!canSave) return
        viewModelScope.launch {
            repository.addTransaction(amount, type, category, note.trim(), date)
            onSaved()
        }
    }
}
