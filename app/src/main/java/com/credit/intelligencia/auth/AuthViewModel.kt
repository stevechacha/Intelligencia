package com.credit.intelligencia.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.credit.intelligencia.login.LoginFragmentDirections
import com.credit.intelligencia.login.LoginViewModel
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.asEvent
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AuthViewModel: ViewModel() {
    fun navigateToLogin() {
        val toLogin = AuthFragmentDirections.toLoginFragment()
        val action = AuthActions.Navigate(toLogin)
        _interactions.postValue(action.asEvent())

    }

    fun navigateToRegister() {
        val toRegister = AuthFragmentDirections.toRegisterFragment(email = "")
        val action = AuthActions.Navigate(toRegister)
        _interactions.postValue(action.asEvent())

    }


    private val _uiState = MutableLiveData<AuthUIState>()
    val uiState: LiveData<AuthUIState> = _uiState

    private val _interactions = MutableLiveData<Event<AuthActions>>()
    val interactions: LiveData<Event<AuthActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

}

sealed class AuthActions {
    data class Navigate(val destination: NavDirections) : AuthActions()
}

sealed class AuthUIState {

    object Loading : AuthUIState()

    data class Error(val title: String = "Try again", val message: Any) :
        AuthUIState()

    data class LimitError(val message: String) : AuthUIState()
}