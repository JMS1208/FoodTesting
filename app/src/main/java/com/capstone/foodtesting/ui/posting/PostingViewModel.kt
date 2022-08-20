package com.capstone.foodtesting.ui.posting

import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {


}