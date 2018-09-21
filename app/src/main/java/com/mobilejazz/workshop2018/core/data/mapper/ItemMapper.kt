package com.mobilejazz.workshop2018.core.data.mapper

import com.mobilejazz.kotlin.core.repository.mapper.Mapper
import com.mobilejazz.workshop2018.core.data.model.ItemEntity
import com.mobilejazz.workshop2018.core.domain.model.Item

class ItemEntityToItemMapper : Mapper<ItemEntity, Item> {

  override fun map(from: ItemEntity): Item = Item(from.id, from.by ?: "by: unknown", from.title, from.text, from.kids)

}