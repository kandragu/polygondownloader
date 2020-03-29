package com.bestgoodmove.polygondownloader.workermanager.provider

import android.net.Uri

object Constants {
    val AUTHORITY = "com.bestgoodmove.polygondownloader.workermanager.provider.DataProvider"
    val URL = Uri.parse("content://" + AUTHORITY + "/t1")
    val CONTENT_TYPE = "contentproviderlab.t12"
    val ID = "_ID"
    val TEXT = "MESSAGE"

     val TITLE = "title"
     val PUB_DATE = "published"
     val UPDATED_DATE = "updated"
     val CONTENT = "content"

    val TEXT_DATA = "Hello Content Providers!"
}