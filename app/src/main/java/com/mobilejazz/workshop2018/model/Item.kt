package com.mobilejazz.workshop2018.model

import java.io.Serializable

data class Item(val id: Int, val by: String?, val title: String?, val text: String?, val type: String, val time: Int, val url: String?, val kids:
List<Int>?) : Serializable