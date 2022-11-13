package com.capstone.foodtesting.ui.restaurant.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.foodtesting.data.model.member.Member
import com.capstone.foodtesting.data.model.menu.Menu
import com.capstone.foodtesting.data.model.restaurant.Restaurant
import com.capstone.foodtesting.data.model.restaurant.RestaurantResponse
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import retrofit2.Response
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RestaurantAddMenuViewModel @Inject constructor(
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

    suspend fun postNewMenu(menu: Menu): Response<Menu> {
        return repository.postNewMenu(menu)
    }

    suspend fun getMarketInfo(uuid: UUID): Response<List<RestaurantResponse>> {
        return repository.getStoreInfoByCustomerId(uuid.toString())
    }

    val getMemberInfo: StateFlow<Member?> = repository.getMember().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

  companion object {
      private const val KEY_MENU_NAME = "menu name"

  }

}