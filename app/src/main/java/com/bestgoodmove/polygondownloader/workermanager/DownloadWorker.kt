package com.bestgoodmove.polygondownloader.workermanager

import android.content.Context
import android.os.SystemClock.sleep
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bestgoodmove.polygondownloader.workermanager.parser.RssParser

class DownloadWorker(ctx: Context, params: WorkerParameters): Worker(ctx,params) {
    override fun doWork(): Result {
//        sleep(7000)
       Log.d("DownloadWorker","worker completed")
        getRss()
       return Result.success()
    }

    fun getRss() {

        var rssParser = RssParser()
//                  var rssChannel = rssParser.parse("https://www.techrepublic.com/rssfeeds/downloads/")
        var rssChannel = rssParser.parse("https://www.polygon.com/rss/index.xml")


    }
}