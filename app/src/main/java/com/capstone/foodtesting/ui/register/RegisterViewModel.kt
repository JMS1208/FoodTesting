package com.capstone.foodtesting.ui.register

import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {


}