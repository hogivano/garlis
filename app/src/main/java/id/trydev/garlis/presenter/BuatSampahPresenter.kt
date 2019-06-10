package id.trydev.garlis.presenter

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mapbox.mapboxsdk.geometry.LatLng
import id.trydev.garlis.base.BasePresenter
import id.trydev.garlis.contract.BuatSampahContract
import java.util.*

class BuatSampahPresenter : BasePresenter<BuatSampahContract.View>(), BuatSampahContract.Presenter {
    private lateinit var view: BuatSampahContract.View
    private lateinit var db: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var user: FirebaseUser

    override fun detachView() {
        super.detachView()
    }

    override fun attachView(mRootView: BuatSampahContract.View) {
        view = mRootView
        storageReference = FirebaseStorage.getInstance().reference
        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseFirestore.getInstance()
    }

    override fun process(
        imgProfil: Uri,
        judul: String,
        deskripsi: String,
        jenis: String,
        berat: String,
        latLng: LatLng
    ) {
        if (judul == ""|| deskripsi == "" || jenis == "" || berat == "0" || berat == ""){
            view.showError("Semua isian harus diisi")
        } else if (latLng == LatLng()){
            view.showError("Harus pilih lokasi dengan tepat")
        } else {
            view.showLoading()
            var pathProfil: String = ""
            if (imgProfil != Uri.EMPTY){
                var nameImg = UUID.randomUUID().toString()
                val uploadTask = storageReference!!.child("/gambar_barang/" + nameImg)
                val task = uploadTask.putFile(imgProfil).continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                        view.dismissLoading()
                    }
                    return@Continuation storageReference.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        val downloadUri = task.result
//                        pathProfil = "gambar_barang/" + nameImg
//
//                        storeData(pathProfil, judul, deskripsi, jenis, berat, latLng)
//
////                        view.showSuccess(pathProfil)
//                        Log.d("download url", downloadUri?.path.toString())
                    } else {
                        view.showError("Error upload gambar coba lagi")
                        view.dismissLoading()
                        Log.e("error", "failed")
                    }
                }.addOnFailureListener {e ->
                    view.showError(e.message.toString())
                    view.dismissLoading()
                }.addOnSuccessListener { task ->
                    uploadTask.downloadUrl.addOnSuccessListener {task ->
                        var url = task.toString()

                        storeData(url, judul, deskripsi, jenis, berat, latLng)
                        Log.e("url", url)
                    }
                }
            } else {
                storeData(pathProfil, judul, deskripsi, jenis, berat, latLng)
            }
        }
    }

    private fun storeData(pathProfil: String,
                          judul: String,
                          deskripsi: String,
                          jenis: String,
                          berat: String,
                          latLng: LatLng){
        var map: HashMap<String, Any> = HashMap<String, Any>()
        map.put("user_id", user.uid)
        map.put("judul", judul)
        map.put("deskripsi", deskripsi)
        map.put("jenis", jenis)
        map.put("lokasi", GeoPoint(latLng.latitude, latLng.longitude))
        map.put("foto", pathProfil)
        map.put("berat", berat)

        db.collection("sampah")
            .add(map)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    view.showSuccess("berhasil disimpan")
                }
                view.dismissLoading()
            }.addOnFailureListener {e ->
                view.showError(e.message.toString())
                view.dismissLoading()
            }
    }

}