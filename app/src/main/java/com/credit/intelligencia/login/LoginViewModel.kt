package com.credit.intelligencia.login

import android.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.credit.intelligencia.api.IntelligenciaApi
import com.credit.intelligencia.register.RegisterFragmentDirections
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.asEvent
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class LoginViewModel(val api:IntelligenciaApi) : ViewModel() {

    private val _uiState = MutableLiveData<LoginUIState>()
    val uiState: LiveData<LoginUIState> = _uiState

    private val _interactions = MutableLiveData<Event<LoginActions>>()
    val interactions: LiveData<Event<LoginActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

    fun loginUser(email: String, password: String) {

        _uiState.postValue(LoginUIState.Loading)
        viewModelScope.launch(exceptionHandler) {
            val userDataResponse = withContext(Dispatchers.IO) {
              /*  api.loginUser(
                        email = email,
                        password = password

                )*/

            }
            val toPermission = LoginFragmentDirections.toWelcomeFragment(email= email)
            val action = LoginActions.Navigate(toPermission)
            _interactions.postValue(action.asEvent())

          /*  if (userDataResponse.success) {
                val toPermission = LoginFragmentDirections.toWelcomeFragment(email= email)
                val action = LoginActions.Navigate(toPermission)
                _interactions.postValue(action.asEvent())

            }
            else if (!userDataResponse.success){
                _uiState.postValue(LoginUIState.Error(message = "Try again"))
            }*/
        }
    }


    fun navigateToRegister() {
        val toRegister = LoginFragmentDirections.toRegisterFragment(email = "")
        val action = LoginActions.Navigate(toRegister)
        _interactions.postValue(action.asEvent())

    }

    sealed class LoginActions {
        data class Navigate(val destination: NavDirections) : LoginActions()
    }

    sealed class LoginUIState {

        object Loading : LoginUIState()

        data class Error(val message: String = "Try again") :
            LoginUIState()

        data class LimitError(val message: String) : LoginUIState()

        data class Message(val message: String): LoginUIState()
    }
}
