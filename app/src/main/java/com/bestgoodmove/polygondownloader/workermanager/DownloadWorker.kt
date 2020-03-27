package com.bestgoodmove.polygondownloader.workermanager

import android.content.Context
import android.os.SystemClock.sleep
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloadWorker(ctx: Context, params: WorkerParameters): Worker(ctx,params) {
    override fun doWork(): Result {
//        sleep(7000)
       Log.d("DownloadWorker","worker completed")
       return Result.success()
    }
}