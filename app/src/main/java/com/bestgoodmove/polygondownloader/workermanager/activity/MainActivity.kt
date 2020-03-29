package com.bestgoodmove.polygondownloader.workermanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bestgoodmove.polygondownloader.R
import com.bestgoodmove.polygondownloader.workermanager.DownloadWorker
import com.bestgoodmove.polygondownloader.workermanager.parser.RssParser
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object{
        private const val tag = "POLYGON_DOWNLOAD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        scheduleWork()
       // getRss()
    }

    fun getRss() {

        var rssParser = RssParser()
//                  var rssChannel = rssParser.parse("https://www.techrepublic.com/rssfeeds/downloads/")
        var rssChannel = rssParser.parse("https://www.polygon.com/rss/index.xml")


    }

    fun scheduleWork(){
        val constrain = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val workmanager = WorkManager.getInstance(application)
        val request = PeriodicWorkRequestBuilder<DownloadWorker>(1, TimeUnit.HOURS)
            .setConstraints(constrain)
            .addTag(tag)
            .build()

        workmanager.cancelAllWorkByTag(tag)
        workmanager.enqueue(request)


    }
}
