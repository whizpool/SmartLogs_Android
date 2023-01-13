package com.example.supportsystemdemo

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.whizpool.supportsystem.SLog
import com.whizpool.supportsystem.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    var uriList: List<Uri>? = null

    val multiple = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
//        LogBuilder.sendReport(this, it)
        uriList = it
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val file = File(uri.path) //create path from uri

                val split = file.path.split(":") //split the path.

                val filePath = split[1] //assign it to a string(your choice).

//                Toast.makeText(this, file, Toast.LENGTH_LONG)
//                    .show()

//                shareFileOnGmail(it,"image")

                findViewById<ImageView>(R.id.imageView).apply {
                    setImageURI(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SLog
            .setTitle("Bug report")
            .setEmail("mail@mail.com")
            .setSubjectToEmail("Email subject")
            .setTitleFontSize(18f) // size in sp
            .setSendButtonFontSize(18f) // size in sp
            .setTextViewBackgroundColor(getMyColor(com.whizpool.supportsystem.R.color.gray))
            .setSendBtnImage(getMyDrawable(R.drawable.ic_launcher_background)!!, false)
            .setDialogTypeface(getMyFont(R.font.allan))
            .setLineColor(getMyColor(R.color.purple_200))
            .setKnobColor(getMyColor(R.color.purple_200))
//            .setMainBackgroundColor(getMyColor(R.color.purple_200))
            .setDialogButtonTextColor(getMyColor(com.whizpool.supportsystem.R.color.gray))
            .setSendButtonBackgroundColor(getMyColor(R.color.white))
//            .dialogEditTextBackground(getMyDrawable(R.drawable.ic_launcher_background))
            .build(this) // must call this first time




        SLog
            .setDefaultTag("tag")
            .setLogFileName("log file")
            .setDaysForLog(4)             // number of last working days for collecting logs
            .setPassword("123456")        // log file password
            .hideReportDialogue(false)
            .build(this)          // must call this first time

//        LogBuilder.addAttachment(listOf(Uri))
//
//        LogBuilder.sendReport(this, listOf(Uri))

//        SLog.initialization(this)


        findViewById<Button>(R.id.save).setOnClickListener {
            SLog.summaryLog(
                tag = "Debug with tag",
                text = "msg",
                shouldSave = true,
                exception = null)

        }

        findViewById<Button>(R.id.crash).setOnClickListener {

//            shareFileOnGmail("content://com.android.providers.media.documents/document/image%3A62647".toUri(),"image")

//            startForResult.launch("image/*")
//            multiple.launch("image/*")
            SLog.detailLogs(
                tag = "Crash",
                text = "App crashed....",
                shouldSave = true,
                exception = IllegalStateException("exception"))
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
            Toast.makeText(this@MainActivity, "Caught $exception", Toast.LENGTH_SHORT).show()

            exception.message?.let { SLog.logError("Delete files Exception", text = it) }
        }
        findViewById<Button>(R.id.delete).setOnClickListener {
            lifecycleScope.launch(handler) {
                SLogFileUtils.deleteFiles {

                    (it as Button).text = "deleted"
                    Toast.makeText(this@MainActivity, "files deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.send).setOnClickListener {

            SLog.sendReport(this@MainActivity, uriList)
        }

//        val d = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
//
//        val scope = CoroutineScope(Job() + Main)
//        val j: Job = scope.launch {
//
//            withContext(d) {
//                ensureActive()
//            }
//            coroutineScope {  }
//        }

//        newSingleThreadContext()
    }

}