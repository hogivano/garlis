package id.trydev.garlis.contract

import android.net.Uri
import com.mapbox.mapboxsdk.geometry.LatLng
import id.trydev.garlis.base.IBaseView

interface ListSampahkuContract{
    interface View: IBaseView {
        fun showSuccess(msg: String)
        fun showError(msg: String)
        fun data(list: List<Map<String, Any>>)
    }

    interface Presenter {
        fun getSampahku()
    }
}