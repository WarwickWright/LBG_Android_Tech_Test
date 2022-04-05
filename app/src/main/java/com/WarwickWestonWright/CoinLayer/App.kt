package com.WarwickWestonWright.CoinLayer

import android.app.Application

class App : Application() {

    var rpcCallInProgress = false

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @JvmStatic
        private lateinit var instance : App
        fun getApp() : App {
            return instance
        }
    }

}