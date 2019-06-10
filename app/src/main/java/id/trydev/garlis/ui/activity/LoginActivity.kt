package id.trydev.garlis.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.http.OkHttp3Requestor
import com.dropbox.core.http.OkHttpRequestor
import com.dropbox.core.v2.DbxClientV2
import com.google.firebase.auth.FirebaseUser
import id.trydev.garlis.R
import id.trydev.garlis.contract.AuthContract
import id.trydev.garlis.presenter.AuthPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity(), AuthContract.View {
    private lateinit var load: ProgressBar
    private lateinit var presenter: AuthPresenter
    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = this.getSharedPreferences("myPref", 0)

        var btnRegister = btnRegister
        var btnLogin = btnLogin
        var usrmail = usrmail
        var password = password

        load = loading
        presenter = AuthPresenter()
        presenter.attachView(this)
        presenter.checkAuth()

        btnLogin.setOnClickListener {
            if (!load.isVisible){
                presenter.processLogin(usrmail.text.toString(), password.text.toString())
            }
        }
        btnRegister.setOnClickListener {
            if (!load.isVisible){
                val intent = RegisterActivity.newIntent(this)
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(applicationContext, "Masih dalam proses login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

    override fun showSuccess(msg: String, user: FirebaseUser) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        if (msg.contains("berhasil login")){
            val intent = MainActivity.newIntent(this)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            this.finish()
        } else {
            val intent = CompleteRegisterActivity.newIntent(this)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            this.finish()
        }
    }

    override fun showError(msg: String) {
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
