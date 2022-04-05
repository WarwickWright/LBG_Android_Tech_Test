package com.WarwickWestonWright.CoinLayer.UI.ListFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.WarwickWestonWright.CoinLayer.DataObjects.RatesObj
import com.WarwickWestonWright.CoinLayer.databinding.ConversionListFragmentBinding

class RatesAdapter(private val values: MutableList<RatesObj>, private val iRatesAdapter: IRatesAdapter) : RecyclerView.Adapter<RatesAdapter.ViewHolder>(), View.OnClickListener {

    interface IRatesAdapter { fun iRatesAdapter(ratesObj: RatesObj) }

    override fun onClick(v: View?) {
        iRatesAdapter.iRatesAdapter(v?.tag as RatesObj)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.conversion_list_fragment, parent, false)
        //view.setOnClickListener(this)
        /*
        val view = LayoutInflater.from(parent.context).inflate(R.layout.breaking_bad_item, parent, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
        * */


        return ViewHolder(ConversionListFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemView.tag = item
        holder.itemView.setOnClickListener(this)
        holder.lblKey.text = item.key
        holder.lblValue.text = item.value.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ConversionListFragmentBinding) : RecyclerView.ViewHolder(binding.root) {
        val lblKey: TextView = binding.lblKey
        val lblValue: TextView = binding.lblValue

        override fun toString(): String {
            return super.toString() + " '" + lblValue.text + "'"
        }
    }

}