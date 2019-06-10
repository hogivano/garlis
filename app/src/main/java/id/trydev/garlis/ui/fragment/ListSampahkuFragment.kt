package id.trydev.garlis.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.solver.widgets.Helper
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager

import id.trydev.garlis.R
import id.trydev.garlis.contract.ListSampahkuContract
import id.trydev.garlis.presenter.ListSampahkuPresenter
import kotlinx.android.synthetic.main.fragment_list_sampahku.*
import id.trydev.garlis.ui.activity.BuatSampahActivity
import id.trydev.garlis.ui.adapter.SampahkuAdapter
import io.grpc.LoadBalancer

class ListSampahkuFragment : Fragment(), ListSampahkuContract.View {

    private lateinit var presenter: ListSampahkuPresenter
    private var adapter: SampahkuAdapter? = null
    private lateinit var contex: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ListSampahkuPresenter()
        presenter.attachView(this)
        presenter.getSampahku()

        add.setOnClickListener {
            val intent = BuatSampahActivity.newIntent(requireContext())
            startActivity(intent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_sampahku, container, false)
    }

    companion object {
        fun newInstance() = ListSampahkuFragment()
    }

    override fun showSuccess(msg: String) {
    }

    override fun showError(msg: String) {

    }

    override fun data(list: List<Map<String, Any>>) {
        rv.apply {
            layoutManager = LinearLayoutManager(contex)
            adapter = SampahkuAdapter(list, context)
        }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contex = context!!
    }
}
