package id.trydev.garlis.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.trydev.garlis.R

class ListKatalogkuFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_katalogku, container, false)
    }

    companion object {
        fun newInstance() = ListKatalogkuFragment()
    }
}
