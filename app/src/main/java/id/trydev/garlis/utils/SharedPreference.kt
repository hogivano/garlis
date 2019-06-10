package id.trydev.garlis.utils


import android.content.SharedPreferences

class SharedPreference constructor(sharedPreferences: SharedPreferences) {
    var pref: SharedPreferences = sharedPreferences

    fun getString(key: String) : String{
       return pref.getString(key, null)
    }
}
