package com.WarwickWestonWright.CoinLayer.UI.ListFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.WarwickWestonWright.CoinLayer.DataObjects.RatesObj
import com.WarwickWestonWright.CoinLayer.R

class ConversionListFragment : Fragment() {

    private var ratesObjs : MutableList<RatesObj> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            ratesObjs = bundle.getParcelableArrayList("ratesObjs")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.conversion_list_fragment_layout, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = RatesAdapter(ratesObjs, activity as RatesAdapter.IRatesAdapter)
            }
        }
        return view
    }

}