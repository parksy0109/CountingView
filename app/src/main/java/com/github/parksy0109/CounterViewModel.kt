package com.github.parksy0109

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel: ViewModel() {

    private var _currentCount = MutableLiveData<Int>()
    val currentCount: LiveData<Int>
        get() = _currentCount

    var increaseSpeed: Long = 1000

    init {
        _currentCount.value = 0
    }

    fun increaseCount() {
        _currentCount.value?.let {
            val nextValue = it.plus(1)
            if(nextValue <= MAX_VALUE) {
                _currentCount.value = it.plus(1)
            }
        } ?: run {
            Log.e(TAG, "increaseCount: _currentCount.value 가 Null 입니다.")
        }
    }

    fun initializeCount() {
        _currentCount.value?.let {
            _currentCount.value = 0
        } ?: run {
            Log.e(TAG, "increaseCount: _currentCount.value 가 Null 입니다.")
        }
    }

    companion object {
        const val TAG = "CounterViewModel"
        const val MAX_VALUE = 9999
    }

}