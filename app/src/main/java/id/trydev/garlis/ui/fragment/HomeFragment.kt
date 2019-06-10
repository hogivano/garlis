package id.trydev.garlis.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView

import id.trydev.garlis.R
import id.trydev.garlis.contract.HomeContract
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.GeoPoint
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import id.trydev.garlis.presenter.HomePresenter
import id.trydev.garlis.ui.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*



class HomeFragment : Fragment(), HomeContract.View, OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var presenter: HomePresenter
    private lateinit var list: MutableList<Map<String, Any>>
    private var adapter: HomeAdapter? = null
    private lateinit var contex: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = HomePresenter()
        presenter.attachView(this)

        this.mapView = idMapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        this.mapboxMap.setStyle(Style.MAPBOX_STREETS, object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {

            }
        })

        var iconF = IconFactory.getInstance(requireContext())
        var icon: Icon = iconF.fromResource(R.drawable.bank_garbage_color_20)

        presenter.requestHomeData()

//        mapboxMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(-7.2693671, 112.7339852))
//                .icon(icon)
//                .title("Bank Sampah Mas Pecah")
//        )
//
//        mapboxMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(-7.237659, 112.7543592))
//                .icon(icon)
//                .title("Bank sampah lestari")
//        )
//
//        mapboxMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(-7.3131353, 112.7495098))
//                .icon(icon)
//                .title("Bank Sampah Jaya Lestari")
//        )
//
//        mapboxMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(-7.3240747, 112.7151775))
//                .icon(icon)
//                .title("Bank Sampah Jambangan Pitu")
//        )
//
//        mapboxMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(-7.2809115, 112.7213144))
//                .icon(icon)
//                .title("Bank Sampah GMH RT. 2")
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(
            requireContext(),
            "pk.eyJ1IjoiaG9naXZhbm8iLCJhIjoiY2p2dzJ5ZGZoMDYzMjRhdGhhOW0zODcyNCJ9.fdKb8W6-hk5Tu9VUJWq04w"
        )
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun showSuccess(msg: String) {

    }

    override fun showData(list: List<Map<String, Any>>) {
        Toast.makeText(requireContext(), "showdata", Toast.LENGTH_SHORT).show()
        this.list = list.toMutableList()

        var iconF = IconFactory.getInstance(requireContext())
        var icon: Icon = iconF.fromResource(R.drawable.bank_garbage_color_20)

        for (l in list) {
            var geo = l.get("lokasi") as GeoPoint
            mapboxMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(geo.latitude, geo.longitude))
                    .icon(icon)
                    .title(l.get("nama").toString())
            )
        }

        rv.apply {
            layoutManager = LinearLayoutManager(contex, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeAdapter(list, contex)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contex = context!!
    }

    override fun showError(msg: String, errorCode: Int) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        Log.e("error in home fragment ", msg + "" + errorCode.toString())
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }


    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
