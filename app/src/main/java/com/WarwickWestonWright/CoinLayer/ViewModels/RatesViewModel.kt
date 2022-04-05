package com.WarwickWestonWright.CoinLayer.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.WarwickWestonWright.CoinLayer.DataObjects.RatesObj

class RatesViewModel : ViewModel() {
    val selected = MutableLiveData<MutableList<RatesObj>>()
    fun select(ratesObj: MutableList<RatesObj>) {
        selected.value = ratesObj
    }
}