package com.mobilejazz.workshop2018.screens.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.mobilejazz.workshop2018.R
import com.mobilejazz.workshop2018.model.Item
import com.mobilejazz.workshop2018.network.Network
import com.mobilejazz.workshop2018.screens.ItemsAdapter
import com.mobilejazz.workshop2018.screens.detail.ItemDetailActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

  private val adapter by lazy {
    ItemsAdapter(listener = {
      startActivity(ItemDetailActivity.getIntent(this, it))
    }, displayAllContent = false)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    title = "Hacker News"

    val linearLayoutManager = LinearLayoutManager(this)
    activity_home_items_rv.layoutManager = linearLayoutManager
    activity_home_items_rv.addItemDecoration(DividerItemDecoration(this, linearLayoutManager.orientation))
    activity_home_items_rv.adapter = adapter

    activity_home_swipe_refresh_srl.setOnRefreshListener {
      reloadData()
    }

    reloadData()
  }

  private fun reloadData() {
    activity_home_swipe_refresh_srl.isRefreshing = true

    val items: MutableList<Item> = mutableListOf()
    var failCounter = 0

    Network.askstoriesItemsIds(s = { itemsResponse ->
      for (id in itemsResponse) {
        Network.itemById(id, success = {
          items.add(it)

          if ((items.size + failCounter) == itemsResponse.size) {
            adapter.reloadData(items)
          }

          activity_home_swipe_refresh_srl.isRefreshing = false
        }, failure = {
          failCounter++
          activity_home_swipe_refresh_srl.isRefreshing = false
        })
      }
    }, f = {
      activity_home_swipe_refresh_srl.isRefreshing = false
    })
  }
}
