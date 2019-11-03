package id.kotlin.movie.presentation.home

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import id.kotlin.movie.data.HomeDatasource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class HomeViewModel(
    private val callback: HomeViewModelCallback,
    private val datasource: HomeDatasource
) : BaseObservable(), HomeView {

  var progressBarVisibility: Int = View.GONE
    @Bindable get

  private val disposables: CompositeDisposable = CompositeDisposable()

  override fun discoverMovie() {
    showLoading()

    datasource.discoverMovie()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ response ->
          hideLoading()
          callback.onResponse(response.results.filterNotNull())
        }, { error ->
          hideLoading()
          callback.onFailure(error)
        }).addTo(disposables)
  }

  override fun onDetach() {
    disposables.clear()
  }

  private fun showLoading() {
    progressBarVisibility = View.VISIBLE
    notifyPropertyChanged(BR.progressBarVisibility)
  }

  private fun hideLoading() {
    progressBarVisibility = View.GONE
    notifyPropertyChanged(BR.progressBarVisibility)
  }
}