package id.trydev.garlis.presenter

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.trydev.garlis.base.BasePresenter
import id.trydev.garlis.contract.AuthContract

class AuthPresenter : BasePresenter<AuthContract.View>(), AuthContract.Presenter {
    private lateinit var view: AuthContract.View
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun attachView(mRootView: AuthContract.View) {
        view = mRootView
//        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun detachView() {

    }

    override fun checkAuth() {
        var user = auth?.currentUser
        if (user != null){
            db = FirebaseFirestore.getInstance()
            db.collection("user")
                .get()
                .addOnCompleteListener{ taskDb ->
                    if (taskDb.isSuccessful){
                        val arr: ArrayList<Map<String, Any>> = ArrayList()
                        var i = 0
                        for (document in taskDb.result!!){
                            i++
                            if (document.id == auth.currentUser?.uid ?: null){
                                i = -1
                                view.dataProfil(document.data)
                                break
                            }
                        }
                        if (i == -1){
                            auth.currentUser?.let { view.showSuccess("berhasil login" , it) }
                        }
                    }
                }
//            view.showSuccess("berhasil login", auth.currentUser!!)
        }
    }

    override fun processRegister(
        email: String,
        password: String,
        rePassword: String
    ) {
        if (email == "" || password == "" || rePassword == "") {
            view.showError("semua isian harus diisi")
        } else if (password != rePassword){
            view.showError("password tidak sama")
        } else {
            view.showLoading()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        auth.currentUser?.let { view.showSuccess("berhasil ditambahkan, silahkan lengkapi profil terlebih dahulu", it) }
                        view.dismissLoading()
                    } else {
                        if (task.exception.toString().contains("email")){
                            view.showError("email sudah terdaftar silahkan login")
                        } else if (task.exception.toString().contains("Password")){
                            view.showError("password minimal 6 karakter")
                        } else if (task.exception.toString().contains("network error")){
                            view.showError("koneksi internet tidak tersedia")
                        } else {
                            view.showError(task.exception.toString())
                        }
                        view.dismissLoading()
                    }
                }
        }
    }

    override fun processLogin(usrmail: String, password: String) {
        if (usrmail == "" || password == ""){
            view.showError("email/password harus diisi")
        } else {
            view.showLoading()
            auth.signInWithEmailAndPassword(usrmail, password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        db = FirebaseFirestore.getInstance()
                        db.collection("user")
                            .get()
                            .addOnCompleteListener{ taskDb ->
                                if (taskDb.isSuccessful){
                                    val arr: ArrayList<Map<String, Any>> = ArrayList()
                                    var i = 0
                                    for (document in taskDb.result!!){
                                        i++
                                        if (document.id == auth.currentUser?.uid ?: null){
                                            i = -1
                                            break
                                        }
                                    }
                                    if (i == -1){
                                        auth.currentUser?.let { view.showSuccess("berhasil login" , it) }
                                    } else {
                                        auth.currentUser?.let { view.showSuccess("lengkapi profil terlebih dahulu" , it) }
                                    }

                                    view.dismissLoading()
                                } else {
                                    view.showError(taskDb.exception.toString())

                                    view.dismissLoading()
                                }
                            }
                    } else {
                        if (task.exception.toString().contains("network error")){
                            view.showError("koneksi internet tidak tersedia")
                        } else if (task.exception.toString().contains("password")){
                            view.showError("email/password anda salah")
                        } else if (task.exception.toString().contains("no user")){
                            view.showError("email/password anda salah")
                        } else {
                            view.showError(task.exception.toString())
                        }
                        view.dismissLoading()

                    }
                }
        }
    }
}