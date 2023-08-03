package com.example.workmanagerdemo1.ui.theme

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerdemo1.MainActivity
import java.text.SimpleDateFormat
import java.util.Date

class UploadWorker(context:Context, params : WorkerParameters) : Worker(context, params){
    // To send output data from worker class to the MainActivity class, we need to define a
    // a constant in the work class

    companion object {
        const val KEY_WORKER = "key_worker"
    }
    override fun doWork(): Result {
        try {
            val count : Int = inputData.getInt(MainActivity.KEY_COUNT_VALUE, 0)
            for (i in 0 until count) {
                /*Enqueued
                * Running
                * Succeeded
                */
                Log.i("MYTAG", "Uploading $i")
            }

            // Code to get the finished time
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            // Data object to send output data
            val outputData = Data.Builder()
                .putString(KEY_WORKER,currentDate)
                .build()

            // Go to MainActivity to write codes to receive the String result
            return Result.success(outputData)
        } catch (e:Exception){
            return Result.failure()
        }
    }
}