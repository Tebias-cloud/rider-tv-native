package com.rider.tv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rider.tv.data.IptvApiService
import com.rider.tv.data.model.Category
import com.rider.tv.data.model.Stream
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class IptvUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val streams: List<Stream> = emptyList(),
    val error: String? = null
)

class IptvViewModel(private val api: IptvApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(IptvUiState())
    val uiState: StateFlow<IptvUiState> = _uiState.asStateFlow()

    fun loadData(user: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val cats = api.getLiveCategories(user, pass)
                val streams = api.getLiveStreams(user, pass)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    categories = cats,
                    streams = streams
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }
}
