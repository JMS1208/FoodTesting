package com.capstone.foodtesting.ui.common.dialog

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.foodtesting.data.model.kakao.local.AddressInfo
import com.capstone.foodtesting.data.model.kakao.search.address.Document
import com.capstone.foodtesting.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddressSettingsViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    //Rest API - CoordToAddress
    suspend fun convertCoordToAddress(location: Location): Flow<AddressInfo?> {

        val x: String = location.longitude.toString()
        val y: String = location.latitude.toString()
        val response = repository.convertCoordToAddress(x, y)

        return if (response.isSuccessful) {
            flow {
                emit(response.body()?.addressInfos?.first())
            }
        } else emptyFlow()

    }
    //Overloading
    suspend fun convertCoordToAddress(x: String, y: String): Flow<AddressInfo?> {

        val response = repository.convertCoordToAddress(x, y)

        return if (response.isSuccessful) {
            flow {
                emit(response.body()?.addressInfos?.first())
            }
        } else emptyFlow()

    }

    //Paging
    private val _searchPagingResult = MutableStateFlow<PagingData<Document>>(PagingData.empty())
    val searchPagingResult: StateFlow<PagingData<Document>> get() = _searchPagingResult.asStateFlow()

    fun searchAddress(
        query: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchAddressPaging(query)
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }

    }
}