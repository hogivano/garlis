package id.trydev.garlis.base

interface IPresenter<in V: IBaseView> {
    fun attachView(mRootView: V)
    fun detachView()
}