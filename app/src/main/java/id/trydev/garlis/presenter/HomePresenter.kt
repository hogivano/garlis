package id.trydev.garlis.presenter

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import id.trydev.garlis.base.BasePresenter
import id.trydev.garlis.contract.HomeContract
import kotlin.collections.mutableListOf

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {
    private lateinit var view: HomeContract.View
    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser

    override fun detachView() {
        super.detachView()
    }

    override fun attachView(mRootView: HomeContract.View) {
        super.attachView(mRootView)
        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseFirestore.getInstance()
        view = mRootView
    }

    override fun requestHomeData() {
        Log.e("masuk reqhome Data", "okee")

        view.showLoading()
        db.collection("user")
            .get()
            .addOnCompleteListener {taskDb ->
                if (taskDb.isSuccessful){
                    val list = mutableListOf<Map<String, Any>>()
                    for (document in taskDb.result!!){
                        var data = document.data
                        if (data.get("role").toString() == "0"){
                            list.add(data)
                        }
                    }
                    view.showData(list)
                } else {
                    Log.e("not succes", "okee")
                    view.showError("db not show", 400)
                }
                view.dismissLoading()
            }
    }
}