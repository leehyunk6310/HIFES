package com.ssafy.hifes.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.hifes.data.model.Event
import com.ssafy.hifes.data.model.FCMForGroupDto
import com.ssafy.hifes.data.model.MarkerDto
import com.ssafy.hifes.data.model.TimeTable
import com.ssafy.hifes.data.repository.festival.FestivalRepository
import com.ssafy.hifes.util.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DetailViewModel_하이페스"
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: FestivalRepository
) : ViewModel() {

    private val _msg = MutableLiveData<Event<String>>()
    val errorMsg: LiveData<Event<String>> = _msg

    private var _markerList: MutableLiveData<List<MarkerDto>> =
        MutableLiveData()
    val markerList: LiveData<List<MarkerDto>> = _markerList

    private var _timeTableList: MutableLiveData<List<TimeTable>> =
        MutableLiveData()
    val timeTableList: LiveData<List<TimeTable>> = _timeTableList

    private var _selectedBoothChip: MutableLiveData<Int> = MutableLiveData()
    val selectedBoothChip: LiveData<Int> = _selectedBoothChip

    private var _callGroupResponse: MutableLiveData<String> = MutableLiveData()
    val callGroupResponse: LiveData<String> = _callGroupResponse

    private var _festivalNoticeSubscribeState: MutableLiveData<Boolean> = MutableLiveData()
    val festivalNoticeSubscribeState: LiveData<Boolean> = _festivalNoticeSubscribeState

    private var _subscribeResponseStateType: MutableLiveData<Pair<SubscribeStateType, Boolean>> = MutableLiveData()
    val subscribeResponseStateType: LiveData<Pair<SubscribeStateType, Boolean>> = _subscribeResponseStateType

    fun getMarkerList(festivalId: Int) {
        viewModelScope.launch {
            val response = repository.getMarkerList(festivalId)
            val type = "token 정보 조회에"
            when (response) {
                is NetworkResponse.Success -> {
                    Log.d(TAG, "getNearFestivalList: $response")
                    _markerList.postValue(response.body)
                }

                is NetworkResponse.ApiError -> {
                    postValueEvent(0, type)
                }

                is NetworkResponse.NetworkError -> {
                    postValueEvent(1, type)
                }

                is NetworkResponse.UnknownError -> {
                    postValueEvent(2, type)
                }
            }
        }
    }

    fun getTimeTableList(festivalId: Int) {
        viewModelScope.launch {
            val response = repository.getFestivalTimeTable(festivalId)
            val type = "token 정보 조회에"
            when (response) {
                is NetworkResponse.Success -> {
                    Log.d(TAG, "getTimeTableList: $response")
                    _timeTableList.postValue(response.body)
                }

                is NetworkResponse.ApiError -> {
                    postValueEvent(0, type)
                }

                is NetworkResponse.NetworkError -> {
                    postValueEvent(1, type)
                }

                is NetworkResponse.UnknownError -> {
                    postValueEvent(2, type)
                }
            }
        }
    }

    fun callGroupNotification(fcmForGroupDto: FCMForGroupDto) {
        viewModelScope.launch {
            val response = repository.callGroupNotification(fcmForGroupDto)
            val type = "token 정보 조회에"
            when (response) {
                is NetworkResponse.Success -> {
                    Log.d(TAG, "callGroupNotification: $response")
                    _callGroupResponse.postValue(response.body)
                }

                is NetworkResponse.ApiError -> {
                    postValueEvent(0, type)
                }

                is NetworkResponse.NetworkError -> {
                    postValueEvent(1, type)
                }

                is NetworkResponse.UnknownError -> {
                    postValueEvent(2, type)
                }
            }
        }
    }

    // 축제 맵에서 사용하는 마커 리스트
    fun updateSelectedBoothChip(index: Int) {
        _selectedBoothChip.postValue(index)
    }

    fun initSubscribeFestivalNoticeState(subscribeState: Boolean){
        _festivalNoticeSubscribeState.postValue(subscribeState)
    }

    fun initSubscribeResponseStateType(){
        _subscribeResponseStateType.postValue(Pair(SubscribeStateType.LOADING,false))
    }

    fun subscribeFestivalNotice(festivalId: Int){
        viewModelScope.launch {
            val response = repository.subscribeFestivalNotice(festivalId)
            val type = "행사 알림 구독에"
            when (response) {
                is NetworkResponse.Success -> {
                    _festivalNoticeSubscribeState.postValue(response.body)
                    _subscribeResponseStateType.postValue(Pair(SubscribeStateType.SUCCESS,response.body))
                }

                is NetworkResponse.ApiError -> {
                    postValueEvent(0, type)
                }

                is NetworkResponse.NetworkError -> {
                    postValueEvent(1, type)
                }

                is NetworkResponse.UnknownError -> {
                    postValueEvent(2, type)
                }
            }
        }
    }

    private fun postValueEvent(value: Int, type: String) {
        val msgArrayList = arrayOf(
            "Api 오류 : $type 실패했습니다.",
            "서버 오류 : $type 실패했습니다.",
            "알 수 없는 오류 : $type 실패했습니다."
        )

        when (value) {
            0 -> _msg.postValue(Event(msgArrayList[0]))
            1 -> _msg.postValue(Event(msgArrayList[1]))
            2 -> _msg.postValue(Event(msgArrayList[2]))
        }
    }

}