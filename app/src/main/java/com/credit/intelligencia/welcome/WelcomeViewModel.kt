package com.credit.intelligencia.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.credit.intelligencia.data.ScoreLimit
import com.credit.intelligencia.permission.PermissionActions
import com.credit.intelligencia.permission.PermissionFragmentDirections
import com.credit.intelligencia.permission.PermissionUIState
import com.credit.intelligencia.register.RegisterFragmentDirections
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.asEvent
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class WelcomeViewModel : ViewModel() {

    private val _uiState = MutableLiveData<WelcomeUIState>()
    val uiState: LiveData<WelcomeUIState> = _uiState

    private val _interactions = MutableLiveData<Event<WelcomeActions>>()
    val interactions: LiveData<Event<WelcomeActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

    fun navigateToPermission(email: String) {
        _uiState.postValue(WelcomeUIState.Loading)

        val toPermission = WelcomeFragmentDirections.toPermissionFragment(email = email)
        val actions: WelcomeActions = WelcomeActions.Navigate(toPermission)
        _interactions.postValue(actions.asEvent())
    }




}

sealed class WelcomeActions {
    data class Navigate(val destination: NavDirections) : WelcomeActions()
}

sealed class WelcomeUIState {

    object Loading : WelcomeUIState()

    data class Error(val title: String = "Try again", val message: Any) :
        WelcomeUIState()

    data class LimitError(val message: String) : WelcomeUIState()

}