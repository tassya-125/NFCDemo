package com.example.nfcdemo.util

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.Manifest
import android.graphics.Color
import android.view.Window
import androidx.appcompat.app.AlertDialog

object AlarmUtil {

    private var mediaPlayer: MediaPlayer? = null

    /**
     * 触发报警（震动 + 声音 + 闪光）
     */
    fun triggerAlarm(context: Context, window: Window, duration: Long = 2000) {
        vibratePhone(context, duration)  // 震动
        playAlarmSound(context)          // 播放声音
        toggleFlashlight(context, true)  // 开启闪光灯
        showAlertDialog(context)         // 显示弹窗
        changeBackgroundColor(window, Color.RED) // UI变红色
    }

    /**
     * 停止报警（停止声音 + 关闭闪光灯）
     */
    fun stopAlarm(context: Context) {
        stopAlarmSound()
        toggleFlashlight(context, false)
    }

    /**
     * 让手机震动
     */
    fun vibratePhone(context: Context, duration: Long = 1000) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    /**
     * 播放报警声音
     */
    fun playAlarmSound(context: Context) {
        stopAlarmSound() // 先停止可能正在播放的音频
//        mediaPlayer = MediaPlayer.create(context, R.ra)
//        mediaPlayer?.start()
    }

    /**
     * 停止报警声音
     */
    fun stopAlarmSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * 开启/关闭闪光灯
     */
    fun toggleFlashlight(context: Context, state: Boolean) {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return // 没有权限时，直接返回，避免崩溃
        }
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        cameraManager.setTorchMode(cameraId, state)
    }

    /**
     * 显示警告弹窗
     */
    fun showAlertDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("⚠ 警告")
            .setMessage("检测到异常情况！")
            .setPositiveButton("确定", null)
            .show()
    }

    /**
     * 改变背景颜色（UI 反馈）
     */
    fun changeBackgroundColor(window: Window, color: Int) {
        window.decorView.setBackgroundColor(color)
    }

}
