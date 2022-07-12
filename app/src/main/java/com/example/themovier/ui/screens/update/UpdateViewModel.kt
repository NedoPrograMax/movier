package com.example.themovier.ui.screens.update

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.models.UpdateModel
import com.example.themovier.data.utils.toUpdateModel
import com.example.themovier.domain.repositories.FireRepo
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val repository: FireRepo) : ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<UpdateModel?> =
        mutableStateOf(null)

    fun getMovie(movieId: String) {
        viewModelScope.launch {
            loading.value = true
            repository.getMovie(movieId).onSuccess {
                data.value = it.toUpdateModel()
                if (data.value != null) {
                    loading.value = false
                    return@launch
                }
            }

            Log.d("RRRR", loading.value.toString())
        }
    }
}
