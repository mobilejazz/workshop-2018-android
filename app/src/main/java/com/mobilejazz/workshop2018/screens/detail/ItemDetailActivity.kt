package com.mobilejazz.workshop2018.screens.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.MenuItem
import com.mobilejazz.workshop2018.R
import com.mobilejazz.workshop2018.model.Item
import com.mobilejazz.workshop2018.network.Network
import com.mobilejazz.workshop2018.screens.ItemsAdapter
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {

  companion object {
    const val ITEM_KEY = "item-key"

    fun getIntent(context: Context, item: Item): Intent = Intent(context, ItemDetailActivity::class.java).apply { putExtra(ITEM_KEY, item) }
  }

  private val adapter by lazy {
    ItemsAdapter(listener = {
      // Nothing to do
    }, displayAllContent = true)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_item_detail)

    title = "Hacker News"

    supportActionBar?.setDisplayHomeAsUpEnabled(true)


    val item = intent.extras.get(ITEM_KEY) as Item

    activity_detail_item_title_tv.text = item.title


    activity_detail_items_rv.layoutManager = LinearLayoutManager(this)
    activity_detail_items_rv.adapter = adapter


    item.by?.let {
      activity_detail_item_by_tv.text = "by: $it"
    }

    item.text?.let {
      activity_detail_item_description_tv.text = Html.fromHtml(it)
    }

    item.kids?.let {
      activity_detail_items_comments_tv.text = "LOADING COMMENTS"

      loadComments(it)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  private fun loadComments(ids: List<Int>) {
    val items: MutableList<Item> = mutableListOf()
    var failCounter = 0

    ids.forEach {
      Network.itemById(it, success = {
        items.add(it)

        if ((items.size + failCounter) == ids.size) {
          adapter.reloadData(items)

          activity_detail_items_comments_tv.text = "${ids.size} COMMENTS"
        }

      }, failure = {
        failCounter++
      })
    }
  }

}
