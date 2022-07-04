package com.example.themovier.ui.screens.update

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.models.MovierItem
import com.example.themovier.data.repo.FireRepoImpl
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val repository: FireRepoImpl): ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<MovierItem?> =
        mutableStateOf(null)

    fun getMovie(movieId: String){
        viewModelScope.launch {
            loading.value = true
            repository.getMovie(movieId).onSuccess {
                data.value = it
                if (data.value != null) {
                    loading.value = false
                    return@launch
                }
            }

            Log.d("RRRR", loading.value.toString())
        }
    }
}
