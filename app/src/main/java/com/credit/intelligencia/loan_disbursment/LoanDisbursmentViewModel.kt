package com.credit.intelligencia.loan_disbursment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.credit.intelligencia.api.IntelligenciaApi
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class LoanDisbursmentViewModel(
    val api: IntelligenciaApi
):ViewModel() {


    private val _uiState = MutableLiveData<LoanDisbursmentUIState>()
    val uiState: LiveData<LoanDisbursmentUIState> = _uiState

    private val _interactions = MutableLiveData<Event<LoanDisbursmentActions>>()
    val interactions: LiveData<Event<LoanDisbursmentActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

}

sealed class LoanDisbursmentActions {
    data class Navigate(val destination: NavDirections) : LoanDisbursmentActions()
}

sealed class LoanDisbursmentUIState {

    object Loading : LoanDisbursmentUIState()

    data class Error(val title: String = "Try again", val message: Any) :
        LoanDisbursmentUIState()

    data class LimitError(val message: String) : LoanDisbursmentUIState()

}