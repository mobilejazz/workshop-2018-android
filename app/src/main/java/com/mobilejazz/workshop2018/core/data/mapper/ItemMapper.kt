package com.mobilejazz.workshop2018.core.data.mapper

import com.mobilejazz.kotlin.core.repository.mapper.Mapper
import com.mobilejazz.workshop2018.core.data.model.ItemEntity
import com.mobilejazz.workshop2018.core.data.model.ItemIdsEntity
import com.mobilejazz.workshop2018.core.domain.model.Item
import com.mobilejazz.workshop2018.core.domain.model.ItemIds

class ItemEntityToItemMapper : Mapper<ItemEntity, Item> {

  override fun map(from: ItemEntity): Item = Item(from.id, from.by ?: "by: unknown", from.title, from.text, from.kids)

}

class ItemIdsEntityToItemIdsMapper : Mapper<ItemIdsEntity, ItemIds> {
  override fun map(from: ItemIdsEntity): ItemIds = ItemIds(from.ids)
}

class ItemIdsToItemIdsEntityMapper : Mapper<ItemIds, ItemIdsEntity> {
  override fun map(from: ItemIds): ItemIdsEntity = ItemIdsEntity(from.ids)
}