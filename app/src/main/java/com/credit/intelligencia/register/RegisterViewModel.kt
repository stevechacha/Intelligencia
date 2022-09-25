package com.credit.intelligencia.register

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.credit.intelligencia.api.IntelligenciaApi
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.asEvent
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*


class RegisterViewModel(val api: IntelligenciaApi) : ViewModel() {

    private val _uiState = MutableLiveData<RegisterUIState>()
    val uiState: LiveData<RegisterUIState> = _uiState

    private val _interactions = MutableLiveData<Event<RegisterActions>>()
    val interactions: LiveData<Event<RegisterActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

    fun register(
        firstname: String,
        lastname: String,
        email: String,
        phone: String,
        idnum: String,
        password: String
    ) {
        _uiState.postValue(RegisterUIState.Loading)

        viewModelScope.launch(exceptionHandler) {
            val userDataResponse = withContext(Dispatchers.Main) {
                api.registerUser(
                    firstname = firstname,
                    lastname = lastname,
                    email = email,
                    password = password,
                    phone = phone,
                    idnum = idnum,
                )

            }
            if (userDataResponse.success) {
                val toPermission = RegisterFragmentDirections.toWelcomeFragment(email = email)
                val actions: RegisterActions = RegisterActions.Navigate(toPermission)
                _interactions.postValue(actions.asEvent())

            } else if (userDataResponse.success){
                _uiState.postValue(RegisterUIState.Error(message = "try again"))

            }
        }
    }

    fun navigateToLogin() {




    }

    sealed class RegisterActions {
        data class Navigate(val destination: NavDirections) : RegisterActions()
    }

    sealed class RegisterUIState {

        object Loading : RegisterUIState()

        data class Error(val title: String = "Try again", val message: Any) :
            RegisterUIState()

        data class LimitError(val message: String) : RegisterUIState()
    }
}