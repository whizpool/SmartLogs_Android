package com.whizpool.supportsystem

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.ColorInt
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.EdgeToEdgeUtils
import com.whizpool.supportsystem.databinding.SendDialogBinding
import com.whizpool.supportsystem.utils.getMyDrawable


class SLDialog(
    private val context: Context,
) {

    var alertDialogCallbacks: AlertDialogCallbacks? = null

    private val binding by lazy {
        SendDialogBinding.inflate(LayoutInflater.from(context))
    }

    private lateinit var dialog: BottomSheetDialog
    fun showAlert() {

        dialog = BottomSheetDialog(context).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            setContentView(binding.root)

            EdgeToEdgeUtils.applyEdgeToEdge(window!!, true)

            behavior.apply {
                halfExpandedRatio = 0.95f
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                skipCollapsed = true
            }
        }


        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.90).toInt()


        dialog.window?.apply {
            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        with(binding) {


            reason.requestFocus()

            bottomSheetHandle.setOnClickListener {
                dialog.dismiss()
            }


            send.setOnClickListener {

                val text = reason.text.toString()

                if (text.isNullOrBlank()) {
                    reason.error = SLogHelper.emptyAlertMessage
                    return@setOnClickListener
                }

                if (text.length < 10) {
                    reason.error = SLogHelper.minimumCharacterError
                    return@setOnClickListener
                }

                dialog.dismiss()
                alertDialogCallbacks?.sendButtonClick(reason.text.toString())
            }

//            showDetails.setOnClickListener {
//            }
        }

        dialog.show()

    }

    interface AlertDialogCallbacks {
        fun sendButtonClick(message: String)
        fun skipButtonClick()
    }


    fun setDialogBackgroundColor(@ColorInt backgroundColor: Int?) = apply {
        backgroundColor?.let { binding.dialogMain.setBackgroundColor(it) }
    }

    fun setDialogTitle(title: String?) = apply {
        binding.title.text = title
    }

    fun setButtonTextColor(@ColorInt color: Int?) = apply {

        color?.let {
            with(binding) {
                showDetails.setTextColor(it)
                send.setTextColor(it)
                send.iconTint = ColorStateList.valueOf(it)
            }
        }
    }

    fun setButtonIcon(icon: Drawable?, shouldNull: Boolean) = apply {

        binding.send.icon = when {
            icon == null && shouldNull -> null
            icon == null -> context.getMyDrawable(R.drawable.icon)
            else -> icon
        }
    }

    fun setFont(typeface: Typeface?) = apply {
        typeface?.let {
            with(binding) {
                title.typeface = it
                reason.typeface = it
//                reasonLayout.typeface = it
                send.typeface = it
            }
        }
    }

    fun setInputDrawable(drawable: Drawable?) = apply {
        binding.reason.background = drawable
//        binding.reasonLayout.background = drawable
    }

    fun setSeparatorColor(@ColorInt color: Int?) = apply {
        color?.let { binding.line.setBackgroundColor(it) }
    }

    fun setDialogHandleColor(@ColorInt color: Int?) = apply {
        color?.let { binding.bottomSheetHandle.background.setTint(it) }
    }

    fun setTextColor(@ColorInt color: Int?) = apply {
        color?.let {
            binding.title.setTextColor(it)
            binding.reason.setTextColor(it)
        }
    }

    fun setTitleTextSize(sp: Float?) = apply {
        sp?.let {
            binding.title.textSize = it
        }
    }

    fun setButtonTextSize(sp: Float?) = apply {
        sp?.let {
            binding.send.textSize = it
        }
    }

    fun setSendButtonBackgroundColor(@ColorInt color: Int?) = apply {
        color?.let {
            binding.send.backgroundTintList = ColorStateList.valueOf(it)
        }
    }

}