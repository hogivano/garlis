package id.trydev.garlis.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.JsonParser

import id.trydev.garlis.R
import id.trydev.garlis.contract.ProfilContract
import id.trydev.garlis.presenter.ProfilPresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import id.trydev.garlis.utils.AppNameGlideModule
import id.trydev.garlis.ui.activity.LoginActivity

class ProfileFragment : Fragment(), ProfilContract.View {

    private lateinit var presenter: ProfilPresenter
    var prefs: SharedPreferences? = null
    private lateinit var contex: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ProfilPresenter()
        presenter.attachView(this)

        prefs = this.activity!!.getSharedPreferences("myPref", 0)
        var str = prefs!!.getString("dataProfil", null)
        var i = JsonParser()
        var json = i.parse(str).asJsonObject

        nama.text = json.get("nama").asString
        noTelp.text = json.get("noTelp").asString
        alamat.text = json.get("alamat").asString
        email.text = json.get("email").asString
        var r = json.get("role").asString

        if (r == "0"){
            role.text = "Regar (Recycle Garbage)"
        } else if (r == "1"){
            role.text = "Figar (Find Garbage)"
        }

        Glide
            .with(requireContext())
            .load(json.get("foto").asString)
            .centerCrop()
            .placeholder(R.drawable.profil)
            .into(imgProfil)

        btnKeluar.setOnClickListener {
            presenter.keluar()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    fun finishActivity(){
        if(activity != null) {
            activity!!.finish()
        }
    }

    override fun showSucess(msg: String) {
        if (msg.contains("keluar")){
            Toast.makeText(requireContext(), "Behasil keluar", Toast.LENGTH_SHORT).show()
            startActivity(LoginActivity.newIntent(contex))
            finishActivity()
        }
    }

    override fun showError(msg: String) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contex = context!!
    }
}
