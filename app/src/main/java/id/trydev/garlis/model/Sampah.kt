package id.trydev.garlis.model

import com.google.firebase.firestore.GeoPoint

data class Sampah(
    var user_id: String,
    var berat: String,
    var foto: String,
    var jenis: String,
    var judul: String,
    var lokasi: GeoPoint
)
