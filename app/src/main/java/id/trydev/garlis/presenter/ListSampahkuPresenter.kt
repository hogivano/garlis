package id.trydev.garlis.presenter

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.trydev.garlis.base.BasePresenter
import id.trydev.garlis.contract.ListSampahkuContract
import id.trydev.garlis.model.Sampah
import kotlin.collections.mutableMapOf

class ListSampahkuPresenter : BasePresenter<ListSampahkuContract.View>(), ListSampahkuContract.Presenter {
    private lateinit var view: ListSampahkuContract.View
    private lateinit var db: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var user: FirebaseUser

    override fun detachView() {
        super.detachView()
    }

    override fun attachView(mRootView: ListSampahkuContract.View) {
        view = mRootView
        storageReference = FirebaseStorage.getInstance().reference
        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseFirestore.getInstance()
    }

    override fun getSampahku() {
        view.showLoading()
        db.collection("sampah").addSnapshotListener { querySnapshot, e ->
            if (e != null){
                view.showError(e.message.toString())
                view.dismissLoading()
                return@addSnapshotListener
            }

            if (querySnapshot != null && !querySnapshot.isEmpty){
                var list = mutableListOf<Map<String, Any>>()
                for(q in querySnapshot.documents){
                    if (q.exists()) {
                        if (q.data!!.get("user_id") == user.uid){
                            var d = q.data!!
                            list.add(d)
                        }
                    }
                }
                view.data(list)
            } else {
                view.showError("sampah anda masih kosong")
            }
            view.dismissLoading()
        }
    }
}