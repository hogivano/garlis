package id.trydev.garlis.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import id.trydev.garlis.R
import id.trydev.garlis.ui.fragment.HomeFragment
import id.trydev.garlis.ui.fragment.ProfileFragment
import id.trydev.garlis.ui.fragment.TransaksiFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigaion: AHBottomNavigation
    private var lastPosition: Int? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var item1 = AHBottomNavigationItem(
            "Home",
            R.drawable.ic_home_black_24dp
        )

        var item2 = AHBottomNavigationItem(
            "Profil",
            R.drawable.ic_person_black_24dp
        )

        var item3 = AHBottomNavigationItem(
            "Garbage",
            R.drawable.waste
        )

        bottomNavigaion = bottom_navigation
        bottomNavigaion.addItem(item3)
        bottomNavigaion.addItem(item1)
        bottomNavigaion.addItem(item2)

        bottomNavigaion.setCurrentItem(1)
        bottomNavigaion.setOnTabSelectedListener({position, wasSelected ->
            wasSelected.apply {
                bottomNavigaion.setCurrentItem(position, wasSelected)
                changeFragment(position)
            }
        })

//        var list: ArrayList<Geo>

        val homeFragment = HomeFragment.newInstance()
        replaceFregment(homeFragment)
    }

    companion object {
        fun newIntent(context: Context): Intent{
            return Intent(context, MainActivity::class.java)
        }
    }

    private fun changeFragment(position: Int){
        if (lastPosition != position){
            lastPosition = position
            when(position){
                0 -> {
                    fragment(TransaksiFragment.newInstance())
                }
                1 -> {
                    fragment(HomeFragment.newInstance())
                }
                2 -> {
                    fragment(ProfileFragment.newInstance())
                }
            }
        }
    }

    private fun fragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer, fragment
        ).commit()
    }

    private fun replaceFregment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
