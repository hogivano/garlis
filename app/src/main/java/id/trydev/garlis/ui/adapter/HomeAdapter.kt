package id.trydev.garlis.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.trydev.garlis.R
import kotlinx.android.synthetic.main.card_profil.view.*

class HomeAdapter(private val list: List<Map<String, Any>>, private val context: Context) : RecyclerView.Adapter<HolderHome>() {
    override fun onBindViewHolder(holder: HolderHome, position: Int) {
        holder.bindHolder(list[position], context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): HolderHome {
        return HolderHome(LayoutInflater.from(viewGroup.context).inflate(R.layout.card_profil, viewGroup, false))
    }

    override fun getItemCount(): Int = list.size
}

class HolderHome(view: View) : RecyclerView.ViewHolder(view) {
    //        private val tvHeroName = view.txtHeroName
    val nama = view.nama
    val noTelp = view.noTelp
    val img = view.img
    fun bindHolder(map: Map<String, Any>, context: Context) {
        nama.text = map.get("nama").toString()
        noTelp.text = map.get("noTelp").toString()

        Glide
            .with(context)
            .load(map.get("foto").toString())
            .centerCrop()
            .placeholder(R.drawable.no_image)
            .into(img)
    }
}