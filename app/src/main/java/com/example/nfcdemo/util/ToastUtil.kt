package com.example.nfcdemo.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.nfcdemo.R

object ToastUtil {

    const val SUCCESS = 1
    const val ERROR = 2
    const val WARNING = 3
    const val INFO = 4

    private val iconMap = mapOf(
        SUCCESS to R.drawable.ic_success,
        ERROR to R.drawable.ic_error,
        WARNING to R.drawable.ic_warning,
        INFO to R.drawable.ic_info
    )

    private val colorMap = mapOf(
        SUCCESS to Color.parseColor("#4CAF50"),  // 绿色
        ERROR to Color.parseColor("#F44336"),    // 红色
        WARNING to Color.parseColor("#FFC107"),  // 黄色
        INFO to Color.parseColor("#2196F3")      // 蓝色
    )

    fun show(context: Context, message: String, type: Int = INFO) {
        Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100.dpToPx(context))
            view = createToastView(context, message, type)
        }.show()
    }

    private fun createToastView(context: Context, message: String, type: Int): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(24.dpToPx(context), 16.dpToPx(context), 24.dpToPx(context), 16.dpToPx(context))
            gravity = Gravity.CENTER_VERTICAL
            background = createBackground(type)
            elevation = 8.dpToPx(context).toFloat()  // 增加阴影效果

            addView(createIconView(context, type))
            addView(createTextView(context, message,type))
        }
    }

    private fun createIconView(context: Context, type: Int): ImageView {
        return ImageView(context).apply {
            setImageResource(iconMap[type] ?: R.drawable.ic_info)
            setColorFilter(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(32.dpToPx(context), 32.dpToPx(context)).apply {
                marginEnd = 12.dpToPx(context)
            }
        }
    }

    private fun createTextView(context: Context, message: String,type: Int): TextView {
        return TextView(context).apply {
            text = message
            setTextColor(Color.WHITE)
            textSize = 15f
            maxLines = 2
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(colorMap[type] ?: Color.parseColor("#2196F3"))
        }
    }

    private fun createBackground(type: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f
            setColor(colorMap[type] ?: Color.parseColor("#2196F3"))
            setStroke(2, adjustAlpha(colorMap[type] ?: Color.BLACK, 0.3f))
        }
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).toInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
