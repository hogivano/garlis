package id.trydev.garlis.ui.activity

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseUser
import id.trydev.garlis.R
import id.trydev.garlis.contract.AuthContract
import id.trydev.garlis.presenter.AuthPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(), AuthContract.View {
    private lateinit var presenter: AuthPresenter
    private lateinit var load: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var btnLogin = btnLogin
        var btnRegister = btnRegister
        var email = email
        var password = password
        var rePassword = rePassword
        load = loading

        presenter = AuthPresenter()
        presenter.attachView(this)

        btnLogin.setOnClickListener{
            if (!load.isVisible){
                startActivity(LoginActivity.newIntent(this))
                this.finish()
            } else {
                Toast.makeText(applicationContext, "masih dalam proses registrasi", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener{
            if (!load.isVisible){
                presenter.processRegister(email.text.toString(), password.text.toString(),
                    rePassword.text.toString())
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, RegisterActivity::class.java)
            return intent
        }
    }

    override fun showSuccess(msg: String, user: FirebaseUser) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        startActivity(CompleteRegisterActivity.newIntent(this))
    }

    override fun showError(msg: String) {
        Log.e("error", msg)
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        load.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        load.visibility = View.GONE
    }

    override fun dataProfil(map: Map<String, Any>) {
        var j = JSONObject(map)
        this.getSharedPreferences("myPref", 0)
            .edit()
            .putString("dataProfil", j.toString())
            .apply()
    }

}
