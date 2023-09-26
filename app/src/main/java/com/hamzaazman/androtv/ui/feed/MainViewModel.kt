package com.hamzaazman.androtv.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamzaazman.androtv.common.Resource
import com.hamzaazman.androtv.data.model.ChannelDto
import com.hamzaazman.androtv.data.repository.ChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val channelRepository: ChannelRepository
) : ViewModel() {

    private val _channelUiState: MutableStateFlow<ChannelUiState> =
        MutableStateFlow(ChannelUiState(loading = true))
    val channelUiState: StateFlow<ChannelUiState> get() = _channelUiState


    init {

    }


     suspend fun getAllChannel() {
        channelRepository.getAllChannel().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _channelUiState.value = _channelUiState.value.copy(
                        loading = false,
                        errorMessage = result.exception?.message.toString(),
                        channelList = emptyList()
                    )
                }

                is Resource.Loading -> {
                    _channelUiState.value = _channelUiState.value.copy(
                        loading = true,
                        errorMessage = "",
                        channelList = emptyList()
                    )
                }

                is Resource.Success -> {
                    _channelUiState.value = _channelUiState.value.copy(
                        loading = false,
                        errorMessage = "",
                        channelList = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}

data class ChannelUiState(
    val errorMessage: String = "",
    val loading: Boolean = false,
    val channelList: List<ChannelDto> = emptyList(),
)