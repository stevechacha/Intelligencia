package com.credit.intelligencia.loan_application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.credit.intelligencia.api.IntelligenciaApi
import com.credit.intelligencia.data.Transaction
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.asEvent
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoanApplicationViewModel(
    val api: IntelligenciaApi
): ViewModel() {


    private val _uiState = MutableLiveData<LoanApplicationtUIState>()
    val uiState: LiveData<LoanApplicationtUIState> = _uiState

    private val _interactions = MutableLiveData<Event<LoanApplicationActions>>()
    val interactions: LiveData<Event<LoanApplicationActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

    fun navigateToDisbursements(total: Double, amount: Int, date: String) {

        _uiState.postValue(LoanApplicationtUIState.Loading)

        viewModelScope.launch(exceptionHandler) {
            val userDataResponse = withContext(Dispatchers.Main) {

                val loanTransaction = Transaction(
                    totalAmount = total,
                    amount = amount,
                    date = date
                )

                val toLoanDisbursemnt = LoanApplicationFragmentDirections.toLoanDisbursmentFragment(
                    loanTransaction
                )
                val action = LoanApplicationActions.Navigate(toLoanDisbursemnt)
                _interactions.postValue(action.asEvent())


            }

        }

    }

}


sealed class LoanApplicationActions {
    data class Navigate(val destination: NavDirections) : LoanApplicationActions()
}

sealed class LoanApplicationtUIState {

    object Loading : LoanApplicationtUIState()

    data class Error(val title: String = "Try again", val message: Any) :
        LoanApplicationtUIState()

    data class LimitError(val message: String) : LoanApplicationtUIState()
}
