package com.capstone.foodtesting.ui.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
  private val repository: MainRepository,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {


  var menuName = String()
      set(value) {
      field = value
        savedStateHandle[KEY_MENU_NAME] = value
      }
      init {
        menuName = savedStateHandle.get<String>(KEY_MENU_NAME) ?: ""
      }


  companion object {
      private const val KEY_MENU_NAME = "menu name"

  }

}