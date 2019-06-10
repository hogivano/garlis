package id.trydev.garlis.contract

import com.google.firebase.auth.FirebaseUser
import id.trydev.garlis.base.IBaseView

interface AuthContract{
    interface View: IBaseView {
        fun showSuccess(msg: String, user: FirebaseUser)
        fun showError(msg: String)
        fun dataProfil(map: Map<String, Any>)
    }

    interface Presenter {
        fun processRegister(email: String, password: String, rePassword: String)
        fun processLogin(usrmail: String, password: String)
        fun checkAuth()
    }
}