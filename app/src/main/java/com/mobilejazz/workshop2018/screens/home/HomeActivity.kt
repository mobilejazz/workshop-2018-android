package com.mobilejazz.workshop2018.screens.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.mobilejazz.kotlin.core.domain.interactor.GetAllInteractor
import com.mobilejazz.kotlin.core.repository.operation.NetworkSyncOperation
import com.mobilejazz.kotlin.core.repository.operation.StorageSyncOperation
import com.mobilejazz.kotlin.core.repository.query.StringKeyQuery
import com.mobilejazz.kotlin.core.threading.extensions.onCompleteUi
import com.mobilejazz.workshop2018.R
import com.mobilejazz.workshop2018.core.domain.interactor.GetItemsByIdInteractor
import com.mobilejazz.workshop2018.screens.ItemsAdapter
import com.mobilejazz.workshop2018.screens.detail.ItemDetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

  private val adapter by lazy {
    ItemsAdapter(listener = {
      startActivity(ItemDetailActivity.getIntent(this, it))
    }, displayAllContent = false)
  }

  @Inject lateinit var getItemsByIdInteractor: GetItemsByIdInteractor
  @Inject lateinit var getAskStoriesInteractor: GetAllInteractor<Int>


  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    title = "Hacker News"

    val linearLayoutManager = LinearLayoutManager(this)
    activity_home_items_rv.layoutManager = linearLayoutManager
    activity_home_items_rv.addItemDecoration(DividerItemDecoration(this, linearLayoutManager.orientation))
    activity_home_items_rv.adapter = adapter

    activity_home_swipe_refresh_srl.setOnRefreshListener {
      reloadData(true)
    }

    reloadData(false)
  }

  private fun reloadData(pullToRefresh: Boolean) {
    activity_home_swipe_refresh_srl.isRefreshing = true

    getAskStoriesInteractor(StringKeyQuery("ask-stories"), if(pullToRefresh) NetworkSyncOperation else StorageSyncOperation).onCompleteUi(onFailure = {
      // nothing to do
    }, onSuccess = {
      getItemsByIdInteractor(it).onCompleteUi(onSuccess = {
        adapter.reloadData(it)

        activity_home_swipe_refresh_srl.isRefreshing = false
      }, onFailure = {
        // nothing to do
      })
    })
  }
}
