package com.example.workmanagerdemo1.ui.theme

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerdemo1.MainActivity
import java.text.SimpleDateFormat
import java.util.Date

class DownloadingWorker(context:Context, params : WorkerParameters) : Worker(context, params){
    // To send output data from worker class to the MainActivity class, we need to define a
    // a constant in the work class


    override fun doWork(): Result {
        try {

            for (i in 0 .. 30000) {
                /*Enqueued
                * Running
                * Succeeded
                */
                Log.i("MYTAG", "Downloading $i")
            }

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            Log.i("MYTAG", "Completed $currentDate")

            return Result.success()
        } catch (e:Exception){
            return Result.failure()
        }
    }
}