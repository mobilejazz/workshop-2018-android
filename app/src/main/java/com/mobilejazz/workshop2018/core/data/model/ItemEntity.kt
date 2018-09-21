package com.mobilejazz.workshop2018.core.data.model

import com.mobilejazz.kotlin.core.repository.validator.vastra.strategies.timestamp.TimestampValidationStrategyDataSource
import java.util.*
import java.util.concurrent.TimeUnit

data class ItemEntity(val id: Int,
                      val by: String?,
                      val title: String?,
                      val text: String?,
                      val type: String,
                      val time: Int,
                      val url: String?,
                      val kids: List<Int>?,
                      private val lu: Date = Date(),
                      private val et: Long = TimeUnit.MINUTES.toMillis(1)) : TimestampValidationStrategyDataSource(lu, et)