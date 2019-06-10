package id.trydev.garlis.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.trydev.garlis.R
import kotlinx.android.synthetic.main.card_sampah_item.view.*

class SampahkuAdapter(private val list: List<Map<String, Any>>, private val context: Context) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): Holder {
        return Holder(LayoutInflater.from(viewGroup.context).inflate(R.layout.card_sampah_item, viewGroup, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindHolder(list[position], context)
    }
}

class Holder(view: View) : RecyclerView.ViewHolder(view) {
//        private val tvHeroName = view.txtHeroName
    val judul = view.judul
    val jenis = view.jenis
    val berat = view.berat
    val img = view.img
    fun bindHolder(map: Map<String, Any>, context: Context) {
        judul.text = map.get("judul").toString()
        jenis.text = map.get("jenis").toString()
        berat.text = map.get("berat").toString() + "Kg"
        Glide
            .with(context)
            .load(map.get("foto").toString())
            .centerCrop()
            .placeholder(R.drawable.no_image)
            .into(img)
    }
}
