package id.trydev.garlis.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions

import id.trydev.garlis.R
import kotlinx.android.synthetic.main.activity_alamat.*
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import android.graphics.Color
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.geojson.FeatureCollection
import android.app.Activity
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode


class AlamatActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    private lateinit var mapView: MapView
    private lateinit var mapBoxMap: MapboxMap
    private val REQUEST_CODE_AUTOCOMPLETE = 1
    private val geojsonSourceLayerId = "geojsonSourceLayerId"
    private val symbolIconId = "symbolIconId"
    private var marker: Marker? = null
    private lateinit var btnSelesai: Button
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private var location: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, "pk.eyJ1IjoiaG9naXZhbm8iLCJhIjoiY2p2dzJ5ZGZoMDYzMjRhdGhhOW0zODcyNCJ9.fdKb8W6-hk5Tu9VUJWq04w")
        setContentView(R.layout.activity_alamat)

        btnSelesai = selesai
        mapView = idMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        btnSelesai.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapBoxMap = mapboxMap
        this.mapBoxMap.setStyle(Style.MAPBOX_STREETS, object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
                initSearchFab()
                style.addImage(symbolIconId, BitmapFactory.decodeResource(
                    resources, R.drawable.reuse_color))

                setUpSource(style)
                setupLayer(style)
//                enableLocationComponent(style)
            }
        })

        this.mapBoxMap.addOnMapClickListener(this)
    }

    private fun initSearchFab(){
        var fabSearch = search
        fabSearch.setOnClickListener {
            var intent = PlaceAutocomplete.IntentBuilder()
                .accessToken("pk.eyJ1IjoiaG9naXZhbm8iLCJhIjoiY2p2dzJ5ZGZoMDYzMjRhdGhhOW0zODcyNCJ9.fdKb8W6-hk5Tu9VUJWq04w")
                .placeOptions(PlaceOptions.builder()
                    .backgroundColor(Color.parseColor("#EEEEEE"))
                    .limit(10)
                    .build(PlaceOptions.MODE_CARDS))
                .build(this@AlamatActivity)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        }
    }

    private fun setUpSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(geojsonSourceLayerId))
    }

    private fun setupLayer(loadedMapStyle: Style) {
        loadedMapStyle.addLayer(
            SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(arrayOf(0f, -8f))
            )
        )
    }

    override fun onMapClick(point: LatLng): Boolean {
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
                        .zoom(14.0)
                        .build()
                ), 4000
            )
            var iconF = IconFactory.getInstance(applicationContext)
            var icon: Icon = iconF.fromResource(R.drawable.reuse_color)

            marker = mapBoxMap.addMarker(
                MarkerOptions()
                    .position(point)
                    .icon(icon)
            )
            location = point
        }

        return true
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
                .locationComponentOptions(customLocationComponentOptions)
                .build()

// Get an instance of the LocationComponent and then adjust its settings
            mapBoxMap.locationComponent.apply {

                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

// Enable to make the LocationComponent visible
                if (!isLocationComponentEnabled){
                    isLocationComponentEnabled = true
                }

// Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

// Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, "Silahkan nyalahkan GPS anda", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapBoxMap.style!!)
        } else {
            Toast.makeText(this, "Silahlan nyalahkan GPS anda", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            val selectedCarmenFeature = PlaceAutocomplete.getPlace(data!!)

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapBoxMap != null) {
                val style = mapBoxMap.getStyle()
                if (style != null) {
                    if (marker != null){
                        mapBoxMap.removeMarker(marker!!)
                    }

                    val source = style!!.getSourceAs<GeoJsonSource>(geojsonSourceLayerId)
                    if (source != null) {
                        source!!.setGeoJson(
                            FeatureCollection.fromFeatures(
                                arrayOf<Feature>(Feature.fromJson(selectedCarmenFeature.toJson()))
                            )
                        )
                    }

                    // Move map camera to the selected location
                    mapBoxMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                .target(
                                    LatLng(
                                        (selectedCarmenFeature.geometry() as Point).latitude(),
                                        (selectedCarmenFeature.geometry() as Point).longitude()
                                    )
                                )
                                .zoom(14.0)
                                .build()
                        ), 4000
                    )

                    location = LatLng(
                        (selectedCarmenFeature.geometry() as Point).latitude(),
                        (selectedCarmenFeature.geometry() as Point).longitude()
                    )
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, AlamatActivity::class.java)
            return intent
        }
    }

    override fun onBackPressed() {
        if (location != null){
            val intent = Intent().apply {
                putExtra("lat", location?.latitude.toString())
                putExtra("lng", location?.longitude.toString())
            }
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}
