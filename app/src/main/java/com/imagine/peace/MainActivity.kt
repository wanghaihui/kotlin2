package com.imagine.peace

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.imagine.ffmpeg.FFmpegNative
import com.imagine.peace.utils.AesEncryptionUtil
import com.imagine.peace.utils.FFmpegUtils
import com.imagine.peace.utils.LrcParser
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    private val requestPermissions: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkSelfPermission()

        FFmpegNative.setDebug(true)

        val json = "{" + " \"page\":1," + " \"token\":\"b15d89580d2e4f7b04e4ce87a78f3c45\"" + "}"

        Log.d("MainActivity", json)
        Log.d("MainActivity", AesEncryptionUtil.encrypt(json,
                AesEncryptionUtil.ENCRYPTION_KEY,
                AesEncryptionUtil.ENCRYPTION_IV))

        Log.d("MainActivity", FFmpegUtils.FFMPEG_PATH)

        // raw资源拷到SD卡上
        FFmpegUtils.moveFontToSDCard(this, "lyrics_cn_bold.otf")
        FFmpegUtils.moveLogoToSDCard(this, "ic_logo.png")

        sample_text.setOnClickListener {
            // 在这里执行
            if (FFmpegUtils.generateGifVideo(FFmpegUtils.FFMPEG_PATH + "/input.gif") == 0) {
                if (FFmpegUtils.generateTenSecondVideo() == 0) {
                    // 添加歌词
                    val lrcList = LrcParser.getInstance().getLrcList(File(FFmpegUtils.FFMPEG_PATH + "/input.lrc"))
                    for (i in 0 until lrcList.size - 1) {
                        if (lrcList[i].time >= 30000 && lrcList[i].time <= 40000) {

                        }
                    }

                    // 添加字幕


                    /*val commands2 = arrayOfNulls<String>(6)
                    commands2[0] = "ffmpeg"
                    commands2[1] = "-i"
                    commands2[2] = FFmpegUtils.FFMPEG_PATH + "/outputBase.mp4"

                    commands2[3] = "-vf"
                    commands2[4] = "[in]" +
                            "drawtext=fontfile=\'" + FFmpegUtils.FFMPEG_PATH +
                            "/lyrics_cn_bold.otf\':text='aaa':fontcolor=white:fontsize=40:box=1:boxcolor=black:boxborderw=5:x=36:y=(h-100), " +
                            "drawtext=fontfile=\'" + FFmpegUtils.FFMPEG_PATH +
                            "/lyrics_cn_bold.otf\':text='bbb':fontcolor=white:fontsize=30:box=1:boxcolor=black:boxborderw=5:x=36:y=(h-60)[out]"

                    commands2[5] = FFmpegUtils.FFMPEG_PATH + "/outputLrc.mp4"
                    val result3 = FFmpegNative.runCommand(commands2)*/

                    /*if (result3 == 0) {
                        val commands = arrayOfNulls<String>(6)
                        commands[0] = "ffmpeg"

                        commands[1] = "-i"
                        commands[2] = FFmpegUtils.FFMPEG_PATH + "/outputLrc.mp4"

                        commands[3] = "-vf"
                        commands[4] = "movie=" + FFmpegUtils.FFMPEG_PATH + "/ic_logo.png[watermark];[in][watermark]overlay=main_w-overlay_w-10:main_h-overlay_h-10[out]"

                        commands[5] = FFmpegUtils.FFMPEG_PATH + "/outputLogo.mp4"
                        val result4 = FFmpegNative.runCommand(commands)
                    }*/
                    /*val commands1 = arrayOfNulls<String>(4)
                    commands1[0] = "ffmpeg"
                    commands1[1] = "-i"
                    commands1[2] = FFmpegUtils.FFMPEG_PATH + "/lyric.srt"
                    commands1[3] = FFmpegUtils.FFMPEG_PATH + "/lyric.ass"
                    val result5 = FFmpegNative.runCommand(commands1)*/

                    FFmpegUtils.setFontConfigEnv()

                    val commands = arrayOfNulls<String>(6)
                    commands[0] = "ffmpeg"
                    commands[1] = "-i"
                    commands[2] = FFmpegUtils.FFMPEG_PATH + "/input.mp4"
                    commands[3] = "-vf"
                    commands[4] = "subtitles=" + FFmpegUtils.FFMPEG_PATH + "/lyric.srt"
                    commands[5] = FFmpegUtils.FFMPEG_PATH + "/outputSrt.mp4"
                    val result4 = FFmpegNative.runCommand(commands)

                    if (result4 == 0) {

                    }

                }
            }
        }
    }

    private fun checkSelfPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = getPermissions()
            // 未获取到权限
            if (permissions.size != 0) {
                requestPermissions(permissions.toTypedArray(), requestPermissions)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getPermissions(): ArrayList<String> {
        val permissions = ArrayList<String>()

        /*if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO)
        }*/

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        /*if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }*/
        return permissions
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == requestPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val hasStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                val hasPhonePermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                val hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                val hasAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

                if (!hasStoragePermission
                        || !hasPhonePermission
                        || !hasCameraPermission
                        || !hasAudioPermission) {

                }
            }
        }
    }
}
