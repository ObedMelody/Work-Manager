package com.example.workmanagerdemo1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanagerdemo1.ui.theme.CompressingWorker
import com.example.workmanagerdemo1.ui.theme.DownloadingWorker
import com.example.workmanagerdemo1.ui.theme.FilteringWorkers
import com.example.workmanagerdemo1.ui.theme.UploadWorker
import com.example.workmanagerdemo1.ui.theme.WorkManagerDemo1Theme
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

/*private operator fun Int.invoke(value: Any): Any {

}*/

class MainActivity : ComponentActivity() {

    companion object{
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener{
            setOneTimeRequest()
          //  setPeriodicWorkRequest()
        }


    }



    private fun setOneTimeRequest(){
        val textView = findViewById<TextView>(R.id.textView)


        val workManager = WorkManager.getInstance(applicationContext)

        // Set Input & Output
        val data: Data =  Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()                                // We will add this to the uploadRequest()
        val constraints = androidx.work.Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
                // If the app is not able to access the internet from its state, it wont be able to continue the progress
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()


        val filterRequest = OneTimeWorkRequest.Builder(FilteringWorkers::class.java)
            .build()
        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()
        val downloadingWorker = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()

        // When chaining parallel workers we need to add them to a mutable list

        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(downloadingWorker)
        parallelWorks.add(filterRequest)



        workManager
            .beginWith(parallelWorks)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()

       /* workManager
                // sequential chaining
            .beginWith(filterRequest)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()*/

        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                textView.text = it.state.name.toString()

                // we can't the return value from UploadWorker class if the work is not finished
                if (it.state.isFinished){
                    val data = it.outputData
                    val message = data.getString(UploadWorker.KEY_WORKER)
                    Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
                }
            })
    }

    // periodic work for background task
    private fun setPeriodicWorkRequest() {
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(DownloadingWorker::class.java, 16, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}


       /* setContent {
            WorkManagerDemo1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkManagerDemo1Theme {
        Greeting("Android")
    }
}*/
