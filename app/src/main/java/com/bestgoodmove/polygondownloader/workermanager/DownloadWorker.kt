package com.bestgoodmove.polygondownloader.workermanager

import android.content.Context
import android.os.SystemClock.sleep
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bestgoodmove.polygondownloader.workermanager.parser.RssParser
import com.bestgoodmove.polygondownloader.workermanager.provider.Constants

class DownloadWorker(ctx: Context, params: WorkerParameters): Worker(ctx,params) {

    override fun doWork(): Result {
//        sleep(7000)
       Log.d("DownloadWorker","worker completed")
        getRss()
       return Result.success()
    }

    fun getRss() {

        var rssParser = RssParser()
        var lstContent = rssParser.parse("https://www.polygon.com/rss/index.xml")
        if (lstContent != null) {
            // Cleanup work
            applicationContext.contentResolver.delete(Constants.URL,null,null);
            for (tuple in lstContent){
                Log.d("tag", tuple.get(Constants.TITLE) as String?)
                applicationContext.contentResolver.insert(Constants.URL, tuple)
            }
        }
    }
}