package id.trydev.garlis.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.mapbox.mapboxsdk.geometry.LatLng
import id.trydev.garlis.base.IBaseView

interface CompleteRegisContract {
    interface View: IBaseView {
        fun showSuccess(msg: String)
        fun showError(msg: String)
        fun dataProfil(map: HashMap<String, Any>)
    }

    interface Presenter {
        fun process(imgProfil: Uri, nama: String, noTelp: String, alamat: String, latLng: LatLng,
                    role: Int, context: Context, data: Intent)
    }
}