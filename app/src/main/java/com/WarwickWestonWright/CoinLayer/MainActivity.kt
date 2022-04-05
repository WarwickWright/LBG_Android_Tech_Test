package com.WarwickWestonWright.CoinLayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.WarwickWestonWright.CoinLayer.Constants.API_KEY
import com.WarwickWestonWright.CoinLayer.Constants.DEFAULT_TARGET
import com.WarwickWestonWright.CoinLayer.Constants.END_POINT
import com.WarwickWestonWright.CoinLayer.DataObjects.RatesObj
import com.WarwickWestonWright.CoinLayer.NetUtils.NetUtils
import com.WarwickWestonWright.CoinLayer.NetUtils.OnlineData
import com.WarwickWestonWright.CoinLayer.UI.ListFragments.RatesAdapter
import com.WarwickWestonWright.CoinLayer.UI.MainFragment
import com.WarwickWestonWright.CoinLayer.ViewModels.DateViewModel
import com.WarwickWestonWright.CoinLayer.ViewModels.RatesViewModel
import org.json.JSONObject
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity(),
    OnlineData.IOnlineData,
    NetUtils.IConnectionStatus,
    RatesAdapter.IRatesAdapter{

    private lateinit var mainFragment: MainFragment
    private lateinit var netUtils: NetUtils
    private lateinit var onlineData: OnlineData
    private lateinit var url: URL
    private lateinit var time: Date
    private lateinit var ratesObjs: MutableList<RatesObj>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        netUtils = NetUtils(this, this)
        onlineData = OnlineData(this)
        url = URL(END_POINT + "access_key=" + API_KEY + "&" + "target=" + DEFAULT_TARGET)
        dateViewModel = ViewModelProvider(this).get(DateViewModel::class.java)
        ratesViewModel = ViewModelProvider(this).get(RatesViewModel::class.java)
        mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().replace(R.id.lytMainFragmentContainer, mainFragment, "MainFragment").commit()
        netUtils.setIsNetworkAvailable()
    }

    override fun netStatusSet(isConnected: Boolean) {
        if(isConnected && !App.getApp().rpcCallInProgress) {
            onlineData.getHttpDataAsString(url)
        }
    }

    override fun iOnlineData(data: String?) {
        val jso = JSONObject(data.toString())
        if(jso.getBoolean("success")) {
            time = Date(jso.getInt("timestamp").toLong().times(1000))
            dateViewModel.selected.value = time
            val jsonHashMap = jso.getJSONObject("rates").keys()
            val jso = jso.get("rates") as JSONObject
            ratesObjs = mutableListOf()
            for (map in jsonHashMap) {
                ratesObjs.add(RatesObj(map.toString(), jso.getDouble(map.toString())))
                map.toString()
            }
            ratesViewModel.selected.value = ratesObjs
        }
    }

    companion object {
        lateinit var dateViewModel: DateViewModel
        lateinit var ratesViewModel: RatesViewModel
    }

    override fun iRatesAdapter(ratesObj: RatesObj) {
        val bundle = Bundle()
        bundle.putString("target", ratesObj.key)
        mainFragment = MainFragment()
        mainFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.lytMainFragmentContainer, mainFragment, "MainFragment").commit()
        url = URL(END_POINT + "access_key=" + API_KEY + "&" + "target=" + ratesObj.key)
        netUtils.setIsNetworkAvailable()
    }

}