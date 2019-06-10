package id.trydev.garlis.contract

import android.net.Uri
import com.mapbox.mapboxsdk.geometry.LatLng
import id.trydev.garlis.base.IBaseView

interface BuatSampahContract{
    interface View: IBaseView {
        fun showSuccess(msg: String)
        fun showError(msg: String)
    }

    interface Presenter {
        fun process(imgProfil: Uri, judul: String, deskripsi: String, jenis: String, berat: String, latLng: LatLng)
    }
}