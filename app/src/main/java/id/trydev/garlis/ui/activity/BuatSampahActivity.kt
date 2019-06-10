package id.trydev.garlis.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import dmax.dialog.SpotsDialog
import id.trydev.garlis.R
import id.trydev.garlis.contract.BuatSampahContract
import id.trydev.garlis.presenter.BuatSampahPresenter
import kotlinx.android.synthetic.main.activity_buat_sampah.*
import kotlinx.android.synthetic.main.activity_buat_sampah.idMapView

class BuatSampahActivity : AppCompatActivity(), BuatSampahContract.View, AdapterView.OnItemSelectedListener,
    OnMapReadyCallback {

    private lateinit var mapBoxMap: MapboxMap
    private lateinit var mapView: MapView
    private var marker: Marker? = null
    private var imgUri: Uri? = Uri.EMPTY
    private lateinit var presenter: BuatSampahPresenter
    private lateinit var dialog: AlertDialog
    private var pilihJenis: String?= ""
    private var latLng: LatLng? = LatLng()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, "pk.eyJ1IjoiaG9naXZhbm8iLCJhIjoiY2p2dzJ5ZGZoMDYzMjRhdGhhOW0zODcyNCJ9.fdKb8W6-hk5Tu9VUJWq04w")
        setContentView(R.layout.activity_buat_sampah)

        presenter = BuatSampahPresenter()
        presenter.attachView(this)

        mapView = idMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        back.setOnClickListener {
            onBackPressed()
        }

        img.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 1000)
        }

        jenis.setOnItemSelectedListener(this)

        simpan.setOnClickListener {
            presenter.process(imgUri!!, judul.text.toString(), deskripsi.text.toString(),
                pilihJenis!!, berat.text.toString(), latLng!!)
        }

        btnLokasi.setOnClickListener {
            startActivityForResult(AlamatActivity.newIntent(this), 0)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BuatSampahActivity::class.java)
        }
    }

    private fun addMarker(point: LatLng){
        val style = mapBoxMap.getStyle()
        if (style != null) {
            if (marker != null){
                mapBoxMap.removeMarker(marker!!)
            }
            // Move map camera to the selected location
            mapBoxMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(
                            point
                        )
                        .zoom(13.0)
                        .build()
                ), 2000
            )
            var iconF = IconFactory.getInstance(applicationContext)
            var icon: Icon = iconF.fromResource(R.drawable.reuse_color)

            marker = mapBoxMap.addMarker(
                MarkerOptions()
                    .position(point)
                    .icon(icon)
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK) {
                var lat = data!!.getStringExtra("lat")
                var lng = data!!.getStringExtra("lng")
                if (lat == null || lng == null){
                    Toast.makeText(applicationContext, "lokasi belum dipilih", Toast.LENGTH_SHORT).show()
                } else {
                    latLng = LatLng(lat.toDouble(), lng.toDouble())
                    addMarker(LatLng(lat.toDouble(), lng.toDouble()))
//                    Toast.makeText(applicationContext, lat + " okeoke " + lng, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == 1000){
            var imageUri = data!!.data!!
            img.setImageURI(imageUri)
            imgUri = imageUri
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapBoxMap = mapboxMap
        this.mapBoxMap.setStyle(Style.MAPBOX_STREETS, object: Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {

            }
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var arr = arrayOf("Organik", "Non Organik", "Logam/Besi/Tembaga", "Lainnya")
        pilihJenis = arr.get(position)
    }

    override fun showSuccess(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    override fun showError(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Loading")
            .setCancelable(false)
            .build()

        dialog.show()
    }

    override fun dismissLoading() {
        dialog.dismiss()
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
