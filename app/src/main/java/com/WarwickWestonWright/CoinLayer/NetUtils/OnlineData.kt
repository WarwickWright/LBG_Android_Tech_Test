package com.WarwickWestonWright.CoinLayer.NetUtils

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.WarwickWestonWright.CoinLayer.App
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class OnlineData(var callingActivity: AppCompatActivity): Runnable {

    interface IOnlineData { fun iOnlineData(data: String?) }

    private val iOnlineData = callingActivity as IOnlineData
    private val onlineDataRef: WeakReference<OnlineData> = WeakReference(this)
    private var onlineDataHandler: OnlineDataHandler? = null
    private val app = App.getApp()
    private lateinit var url: URL
    private var dataThread: Thread? = null

    fun getHttpDataAsString(url: URL) {
        onlineDataHandler = OnlineDataHandler(onlineDataRef, callingActivity)
        this.url = url
        dataThread = Thread(this)
        dataThread?.start()
    }

    override fun run() {
        getHttpDataAsString()
    }

    private fun getHttpDataAsString() {
        app.rpcCallInProgress = true
        var httpURLConnection: HttpURLConnection? = null
        val msg = Message()
        val bufferedReader: BufferedReader
        val sb: StringBuilder

        try {
            httpURLConnection = url?.openConnection() as HttpURLConnection
            httpURLConnection?.doInput = true
            httpURLConnection?.doOutput = false
            httpURLConnection?.useCaches = true
            httpURLConnection?.requestMethod = "GET"
            httpURLConnection?.readTimeout = 15 * 1000
            httpURLConnection?.setRequestProperty("Accept-Encoding", "x-www-form-urlencoded")
            httpURLConnection?.connect()
            bufferedReader = BufferedReader(InputStreamReader(httpURLConnection?.inputStream))
            sb = StringBuilder()
            var line: String?
            bufferedReader.useLines { lines ->
                lines.forEach { line ->
                    sb.append(line)
                }
            }
            bufferedReader.close()
            httpURLConnection?.inputStream?.close()
            msg.obj = sb.toString()
            onlineDataHandler?.handleMessage(msg)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            httpURLConnection?.disconnect()
        }
    }

    companion object {

        class OnlineDataHandler(private var onlineDataRef: WeakReference<OnlineData>, var callingActivity: AppCompatActivity) : Handler(callingActivity.mainLooper) {
            override fun handleMessage(msg: Message) {
                val oldRef: OnlineData? = onlineDataRef.get()
                if(oldRef != null) {
                    Looper.prepare()
                    post {
                        oldRef.app.rpcCallInProgress = false
                        oldRef.iOnlineData.iOnlineData(msg.obj as String)
                        oldRef.dataThread = null
                    }
                    Looper.loop()
                }
            }
        }

    }
}