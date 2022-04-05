package com.WarwickWestonWright.CoinLayer.DataObjects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatesObj(val key: String, val value: Double) : Parcelable