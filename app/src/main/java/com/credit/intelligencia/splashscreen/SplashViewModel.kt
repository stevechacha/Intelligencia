package com.credit.intelligencia.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.credit.intelligencia.util.Event
import com.credit.intelligencia.util.handleThrowable
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class SplashViewModel : ViewModel() {
    private val _uiState = MutableLiveData<SplashUIState>()
    val uiState: LiveData<SplashUIState> = _uiState

    private val _interactions = MutableLiveData<Event<SplashActions>>()
    val interactions: LiveData<Event<SplashActions>> = _interactions

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        val errorPair = exception.handleThrowable()
    }

}

sealed class SplashActions {
    data class Navigate(val destination: NavDirections) : SplashActions()
}

sealed class SplashUIState {

    object Loading : SplashUIState()

    data class Error(val title: String = "Try again", val message: Any) :
        SplashUIState()

    data class LimitError(val message: String) : SplashUIState()
}
