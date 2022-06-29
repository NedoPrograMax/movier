package com.example.themovier.screens.update

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovier.data.DataOrException
import com.example.themovier.model.MovierItem
import com.example.themovier.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<DataOrException<MovierItem, Exception>> =
        mutableStateOf(DataOrException(null, Exception("")))

    fun getMovie(movieId: String){
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getMovie(movieId)
            if (data.value.data != null) {
                loading.value = false
                return@launch
            }

            Log.d("RRRR", loading.value.toString())
        }
    }
}