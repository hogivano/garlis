package id.trydev.garlis.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import  id.trydev.garlis.R
import id.trydev.garlis.contract.CompleteRegisContract
import id.trydev.garlis.presenter.CompleteRegisPresenter
import kotlinx.android.synthetic.main.activity_complete_register.*
import org.json.JSONObject
import kotlin.collections.HashMap

class CompleteRegisterActivity : AppCompatActivity(), OnMapReadyCallback, CompleteRegisContract.View {
    private lateinit var mapBoxMap: MapboxMap
    private lateinit var mapView: MapView
    private var marker: Marker? = null
    private lateinit var presenter: CompleteRegisPresenter
    private var imgProfile: Uri? = Uri.EMPTY
    private var latLng: LatLng? = LatLng()
    var role: Int? = -1
    private var dataImg: Intent? = null
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, "pk.eyJ1IjoiaG9naXZhbm8iLCJhIjoiY2p2dzJ5ZGZoMDYzMjRhdGhhOW0zODcyNCJ9.fdKb8W6-hk5Tu9VUJWq04w")
        setContentView(R.layout.activity_complete_register)


        mapView = idMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        presenter = CompleteRegisPresenter()
        presenter.attachView(this)

        alamat.setOnClickListener {
            startActivityForResult(AlamatActivity.newIntent(this), 0)
        }

        imgProfil.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 1000)
        }

        pilihRole.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.regar ->
                    role = 0
                R.id.figar ->
                    role = 1
            }
        }

        btnSimpan.setOnClickListener {
            if (imgProfile == Uri.EMPTY){
                presenter.process(Uri.EMPTY,
                    nama.text.toString(),
                    noTelp.text.toString(),
                    inputAlamat.text.toString(),
                    latLng!!,
                    role!!,
                    applicationContext,
                    dataImg!!)
            } else {
                presenter.process(imgProfile!!,
                    nama.text.toString(),
                    noTelp.text.toString(),
                    inputAlamat.text.toString(),
                    latLng!!,
                    role!!,
                    applicationContext,
                    dataImg!!)

                imgProfile?.let { it1 ->  }
            }
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

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapBoxMap = mapboxMap
        this.mapBoxMap.setStyle(Style.MAPBOX_STREETS, object: Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {

            }
        })
    }

    override fun showSuccess(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        val intent = MainActivity.newIntent(this)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        this.finish()
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

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, CompleteRegisterActivity::class.java)
            return intent
        }
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
            imgProfil.setImageURI(imageUri)
            imgProfile = imageUri
            dataImg = data
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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

    override fun dataProfil(map: HashMap<String, Any>) {
        var j = JSONObject(map)
        this.getSharedPreferences("myPref", 0)
            .edit()
            .putString("dataProfil", j.toString())
            .apply()
    }
}
