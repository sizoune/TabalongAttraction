package com.example.mywisatadestination

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mywisatadestination.data.model.Lokasi
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_place.view.*

class MainAdapter(
    private val context: Context,
    private val dataNews: List<Lokasi>,
    private val clickListener: (Lokasi) -> Unit
) : RecyclerView.Adapter<MainAdapter.PlacesHolder>() {

    class PlacesHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItemtoView(
            data: Lokasi,
            clickListener: (Lokasi) -> Unit
        ) {
            containerView.lat_lng.text = "${data.latitude} , ${data.longitude}}"
            containerView.title.text = data.nama
            Picasso.get().load(data.foto).placeholder(R.drawable.image_12).fit()
                .into(containerView.image)
            containerView.setOnClickListener { clickListener(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesHolder {
        return PlacesHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_place,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataNews.size
    }

    override fun onBindViewHolder(holder: PlacesHolder, position: Int) {
        holder.bindItemtoView(dataNews[position], clickListener)
    }
}