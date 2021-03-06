package dev.hitools.noah.modules.sample.detail.extend.download

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.arialyy.annotations.Download
import dev.hitools.common.extensions.toast
import dev.hitools.common.utils.download.DownloadManager
import dev.hitools.common.utils.download.DownloadStatusInfo
import dev.hitools.common.utils.download.DownloadTask
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FSampleDownloadBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment


/**
 * Created by yuhaiyang on 2020-03-05.
 */
class SampleDownloadFragment : AppBindFragment<FSampleDownloadBinding, SampleDownloadViewModel>() {
    private val url2 = "http://file.hiqidi.com/version-update/guanwang/adunpai.apk"

    //private val url = "https://adunpai.com/version-update1/1.1.1/guanwang/adunpai.apk"
    private val url = "https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk"
    override fun getLayout(): Int = R.layout.f_sample_download

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.download1.setOnClickListener { download1() }
        binding.download2.setOnClickListener { download2() }
        binding.download3.setOnClickListener { download3() }
        binding.download4.setOnClickListener { download4() }
        binding.downloadOthers.setOnClickListener { downloadOthers() }

//        // 进行注册
//        Aria.download(this).register()
    }

    private fun download1() {

        val start = System.currentTimeMillis()
        try {
            DownloadManager.newTask()
                .url(url)
                .threadNumber(1)
                .saveName("weixin1.apk")
                .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
                .setOnProgressListener { current, total -> update1(current, total, start) }
                .setOnStatusChangedListener {
                    installApp(it)
                }
                .start()
        } catch (e: Exception) {
            Log.e("yhy", "download1: e = $e")
        }

    }

    private fun update1(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：$current / $total, 耗时：$time"
        mainThread { binding.status1.text = text }
    }


    private var task2: DownloadTask? = null

    @Synchronized
    private fun download2() {
        val start = System.currentTimeMillis()
        if (task2?.isDownloading == true) {
            toast("task2 is downloading")
        }

        task2 = DownloadManager.newTask()
            .url(url)
            .threadNumber(2)
            .saveName("weixin2.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update2(current, total, start) }
            .setOnStatusChangedListener { installApp(it) }
            .start()
    }

    private fun update2(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：${current / total.toFloat()}, 耗时：$time"
        mainThread { binding.status2.text = text }
    }

    private var task3: DownloadTask? = null
    private fun download3() {
        val start = System.currentTimeMillis()
        if (task3?.isDownloading == true) {
            return
        }

        task3 = DownloadManager.newTask()
            .url(url)
            .threadNumber(3)
            .saveName("weixin3.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update3(current, total, start) }
            .setOnStatusChangedListener { installApp(it) }
            .start()
    }

    private fun update3(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：${current / total.toFloat()}, 耗时：$time"
        mainThread { binding.status3.text = text }
    }


    private fun download4() {
        val start = System.currentTimeMillis()
        DownloadManager.newTask()
            .url(url)
            .threadNumber(4)
            .saveName("weixin4.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update4(current, total, start) }
            .setOnStatusChangedListener { installApp(it) }
            .start()
    }

    private fun update4(current: Long, total: Long, startTime: Long) = mainThread {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：${current / total.toFloat()}, 耗时：$time"
        mainThread { binding.status4.text = text }
    }


    private var downloadOthersStart = 0L
    private fun downloadOthers() {
        downloadOthersStart = System.currentTimeMillis()

//        val path = requireContext().getExternalFilesDir("apk")!!
//        val file = File(path, "wx_others_${System.currentTimeMillis()}.apk")
//
//        Aria.download(this)
//            .load(url) //读取下载地址
//            .setFilePath(file.absolutePath)
//            .create()
        Test().start()
    }

    @Download.onTaskRunning
    fun running(task: com.arialyy.aria.core.task.DownloadTask) {
        Log.i(TAG, "running: task = " + task.percent)
        updateOthers(task.percent)
    }

    private fun updateOthers(current: Int) = mainThread {
        val time = (System.currentTimeMillis() - downloadOthersStart) / 1000
        val text = "下载进度：${current}, 耗时：$time"
        mainThread { binding.statusOthers.text = text }
    }


    private fun installApp(info: DownloadStatusInfo) {
        val file = info.file
        Log.i(TAG, "installApp: file = " + file?.absoluteFile)
        if (file == null) return
        val context = context ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        Log.i(TAG, "installApp: start ===" + isMainThread())
        context.startActivity(intent)
    }


    companion object {
        private const val TAG = "SampleDownloadFragment"
    }
}