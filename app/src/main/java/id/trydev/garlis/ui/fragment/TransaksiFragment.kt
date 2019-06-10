package id.trydev.garlis.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.gigamole.navigationtabstrip.NavigationTabStrip

import id.trydev.garlis.R
import kotlinx.android.synthetic.main.fragment_transaksi.*

class TransaksiFragment : Fragment(), ViewPager.OnPageChangeListener,
    NavigationTabStrip.OnTabStripSelectedIndexListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationTabStrip.setTitles("Sampahku", "Katalog")
        navigationTabStrip.setTabIndex(0, true)
        navigationTabStrip.setTitleSize(35f)
        navigationTabStrip.setStripColor(Color.RED)
        navigationTabStrip.setStripWeight(6f)
        navigationTabStrip.setStripFactor(2f)
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE)
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM)
        navigationTabStrip.setCornersRadius(3f)
        navigationTabStrip.setAnimationDuration(300)
        navigationTabStrip.setInactiveColor(R.color.textBlack)
        navigationTabStrip.setActiveColor(R.color.colorPrimary)
        navigationTabStrip.setOnPageChangeListener(this)
        navigationTabStrip.setOnTabStripSelectedIndexListener(this)

        val listSampahkuFragment = ListSampahkuFragment.newInstance()
        replaceFregment(listSampahkuFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transaksi, container, false)
    }

    companion object {
        fun newInstance() = TransaksiFragment()
    }

    private fun fragment(fragment: Fragment){
        getChildFragmentManager().beginTransaction().replace(
            R.id.fragmentContainer, fragment
        ).commit()
    }

    private fun replaceFregment(fragment: Fragment){
        val fragmentTransaction = getChildFragmentManager().beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun changeFragment(position: Int){
        when(position){
            0 -> {
                fragment(ListSampahkuFragment.newInstance())
            }
            1 -> {
                fragment(ListKatalogFragment.newInstance())
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onEndTabSelected(title: String?, index: Int) {
    }

    override fun onStartTabSelected(title: String?, index: Int) {
        changeFragment(index)
    }
}
