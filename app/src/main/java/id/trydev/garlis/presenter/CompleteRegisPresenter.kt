package id.trydev.garlis.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import id.trydev.garlis.contract.CompleteRegisContract
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class CompleteRegisPresenter : BasePresenter<CompleteRegisContract.View>(), CompleteRegisContract.Presenter {

    private lateinit var view: CompleteRegisContract.View
    private lateinit var storageReference: StorageReference
    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseFirestore

    override fun detachView() {
        super.detachView()
    }

    override fun attachView(mRootView: CompleteRegisContract.View) {
        view = mRootView
        storageReference = FirebaseStorage.getInstance().reference
        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseFirestore.getInstance()
    }

    override fun process(imgProfil: Uri, nama: String, noTelp: String, alamat: String, latLng: LatLng,
                         role: Int, context: Context, data: Intent) {
        if (nama == "" || noTelp == "" || alamat == "" || role == -1){
            view.showError("semua isian harus diisi")
        } else if (latLng == LatLng()) {
            view.showError("pilih lokasi alamat terlebih dahulu")
        } else {
            view.showLoading()
            var pathProfil: String = ""
            if (imgProfil != Uri.EMPTY){
                var nameImg = UUID.randomUUID().toString()
                val uploadTask = storageReference!!.child("/gambar_profil/" + nameImg)
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
//                        pathProfil = "gambar_profil/" + nameImg
//
//                        storeData(pathProfil, nama, noTelp, alamat, latLng, role)
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
                        storeData(url, nama, noTelp, alamat, latLng, role)
                        Log.e("url", url)
                    }
                }
            } else {
                storeData(pathProfil, nama, noTelp, alamat, latLng, role)
            }
        }
    }

    private fun storeData(pathProfil: String, nama: String, noTelp: String, alamat: String, latLng: LatLng,
                          role: Int){
        var map: HashMap<String, Any> = HashMap<String, Any>()
        map.put("nama", nama)
        map.put("noTelp", noTelp)
        map.put("alamat", alamat)
        map.put("lokasi", GeoPoint(latLng.latitude, latLng.longitude))
        map.put("foto", pathProfil)
        map.put("role", role)
        map.put("email", user.email.toString())

        db.collection("user")
            .document(user.uid)
            .set(map)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    view.dataProfil(map)
                    view.showSuccess("berhasil disimpan")
                }
                view.dismissLoading()
            }.addOnFailureListener {e ->
                view.showError(e.message.toString())
                view.dismissLoading()
            }

    }
}