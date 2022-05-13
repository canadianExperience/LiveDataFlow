package com.me.livedataflow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {
    private val _tvLiveData by lazy { MutableLiveData<String>() }
    val tvLiveData: LiveData<String> get() = _tvLiveData

    private val _coldFlow = flow<String> {
        var number = 0
        repeat(100){
            Log.d(TAG, "cold flow emitted: $number")
            emit(number.toString())
            number++
            delay(1000)
        }
    }

    val coldFlow get() = _coldFlow



    private val _stateFlow = MutableStateFlow("State initial value")
    val stateFlow: StateFlow<String> get() = _stateFlow.asStateFlow()

    private val _flowToSharedFlow = flow<String> {
        var number = 0
        repeat(100){
            Log.d(TAG, "flow to shared flow emitted: $number")
            emit(number.toString())
            number++
            delay(1000)
        }
    }.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),  // stop hot emit when not subscribed
       // SharingStarted.Eagerly,
        replay = 1)

    val sharedFlow: SharedFlow<String> get() = _flowToSharedFlow


    fun populateLiveData() = viewModelScope.launch {
        var number = 0
        repeat(100){
            _tvLiveData.postValue(number.toString())
            Log.d(TAG, "live data emitted: $number")
            number++
            delay(1000)
        }
    }

    fun populateStateFlow() = viewModelScope.launch {
        var number = 0
        repeat(100){
            _stateFlow.emit("update $number")
            Log.d(TAG, "state flow emitted: update $number")
            number++
            delay(1000)
        }
    }
}