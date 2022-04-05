package com.WarwickWestonWright.CoinLayer.UI

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.WarwickWestonWright.CoinLayer.Constants.DEFAULT_TARGET
import com.WarwickWestonWright.CoinLayer.DataObjects.RatesObj
import com.WarwickWestonWright.CoinLayer.R
import com.WarwickWestonWright.CoinLayer.UI.ListFragments.ConversionListFragment
import com.WarwickWestonWright.CoinLayer.Utils.TimeUtils.TimeUtils
import com.WarwickWestonWright.CoinLayer.ViewModels.DateViewModel
import com.WarwickWestonWright.CoinLayer.ViewModels.RatesViewModel
import java.util.*

class MainFragment : Fragment() {

    private lateinit var rootView: View
    private val dateViewModel: DateViewModel by activityViewModels()
    private val ratesViewModel: RatesViewModel by activityViewModels()
    private lateinit var conversionListFragment: ConversionListFragment
    private val timeUtils = TimeUtils()
    private lateinit var lblTime: TextView
    private lateinit var lblTarget: TextView
    private var target: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            target = bundle.getString("target", DEFAULT_TARGET)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.main_fragment, container, false)

        lblTime = rootView.findViewById<TextView>(R.id.lblTime)
        lblTarget = rootView.findViewById(R.id.lblTarget)

        if(target != "") {
            lblTarget.text = target
        }
        else {
            lblTarget.text = DEFAULT_TARGET
        }

        dateViewModel.selected.observe(viewLifecycleOwner, Observer<Date> { date: Date? ->
            lblTime.text = timeUtils.getFriendlyDate(date!!)
        })

        ratesViewModel.selected.observe(viewLifecycleOwner, Observer<MutableList<RatesObj>> { ratesObjs: MutableList<RatesObj> ->
            conversionListFragment = ConversionListFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("ratesObjs", ratesObjs as ArrayList<out Parcelable>)
            conversionListFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.lytListContainer, conversionListFragment, "ConversionListFragment")?.commit()
        })

        return rootView
    }

}