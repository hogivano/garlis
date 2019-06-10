package id.trydev.garlis.contract

import id.trydev.garlis.base.IBaseView

interface ProfilContract{
    interface View: IBaseView {
//        fun setHomeData()
        fun showSucess(msg: String)
        fun showError(msg: String)
    }

    interface Presenter {
        fun keluar()
    }
}