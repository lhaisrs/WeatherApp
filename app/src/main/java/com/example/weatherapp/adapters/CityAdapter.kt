package com.example.weatherapp.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.weatherapp.R
import com.example.weatherapp.models.City
import com.example.weatherapp.views.DetailsCityActivity

import kotlinx.android.synthetic.main.item_city_list.view.*

class CityAdapter (
    private val items: List<City>,
    private val context: Context
): RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_city_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(item: City) = with(itemView) {
            //Using just name as field to RecyclerView
            cityName.text = item.name

            cityName.setOnClickListener {
                //Navigate to DetailsCityActivity
                val intent = Intent(context, DetailsCityActivity::class.java)
                intent.putExtra(CITY, item)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val CITY = "Selected_City"
    }
}
