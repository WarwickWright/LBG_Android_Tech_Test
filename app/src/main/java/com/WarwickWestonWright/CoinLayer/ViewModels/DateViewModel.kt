package com.WarwickWestonWright.CoinLayer.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class DateViewModel : ViewModel() {
    val selected = MutableLiveData<Date>()
}