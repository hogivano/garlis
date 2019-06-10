package id.trydev.garlis.presenter

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.trydev.garlis.base.BasePresenter
import id.trydev.garlis.contract.ProfilContract

class ProfilPresenter : BasePresenter<ProfilContract.View>(), ProfilContract.Presenter{

    private lateinit var auth: FirebaseAuth
    private lateinit var view: ProfilContract.View

    override fun detachView() {
        super.detachView()
    }

    override fun attachView(mRootView: ProfilContract.View) {
        view = mRootView
        auth = FirebaseAuth.getInstance()
    }

    override fun keluar() {
        auth.signOut()
        view.showSucess("berhasil keluar")
    }
}