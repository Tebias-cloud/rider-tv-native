package com.rider.tv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rider.tv.auth.SupabaseManager
import com.rider.tv.data.MovieApiService
import com.rider.tv.data.model.Category
import com.rider.tv.data.model.ExternalAccount
import com.rider.tv.data.model.Stream
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MovieUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val streams: List<Stream> = emptyList(),
    val account: ExternalAccount? = null,
    val error: String? = null
)

class MovieViewModel(private var api: MovieApiService) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun loadData(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                var externalAccount = SupabaseManager.getExternalAccount(userId)
                
                // FALLBACK: If specific ID returns null, try to find ANY active line for testing
                if (externalAccount == null) {
                    android.util.Log.w("MovieViewModel", "ID $userId not found, trying fallback...")
                    externalAccount = SupabaseManager.getAnyAccount()
                }

                if (externalAccount == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, 
                        error = "No se encontró ninguna línea activa en la base de datos."
                    )
                    return@launch
                }

                // Re-create API with the specific portal URL
                api = MovieApiService.create(externalAccount.portalUrl)
                
                val cats = api.getMovieCategories(externalAccount.username, externalAccount.password)
                val streams = api.getMovieStreams(externalAccount.username, externalAccount.password)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    categories = cats,
                    streams = streams,
                    account = externalAccount
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }
}
