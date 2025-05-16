package com.m.ammar.itaskmanager.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.ammar.itaskmanager.data.remote.ApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val apiRepo: ApiRepo
) : ViewModel() {
    private val _quote = MutableStateFlow("")
    val quote: StateFlow<String> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            apiRepo.getList().run {
                onSuccess {
                    val firstQuote = it.firstOrNull()
                    if (firstQuote != null) {
                        _quote.emit("\"${firstQuote.q}\" \n\n${firstQuote.a}")
                    } else {
                        _quote.emit(fallbackQuote())
                    }
                }
                onFailure {

                    _quote.value = fallbackQuote()

                }
            }
        }
    }

    private fun fallbackQuote(): String {
        return "Believe in yourself. You're stronger than you think."
    }
}
