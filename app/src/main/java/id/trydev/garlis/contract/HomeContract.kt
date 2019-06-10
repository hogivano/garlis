package id.trydev.garlis.contract

import id.trydev.garlis.base.IBaseView

interface HomeContract{
    interface View: IBaseView {
        fun showSuccess(msg: String)
        fun showError(msg: String, errorCode: Int)
        fun showData(list: List<Map<String, Any>>)
    }

    interface Presenter {
        fun requestHomeData()
    }
}