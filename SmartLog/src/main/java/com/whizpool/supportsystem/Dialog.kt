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
import com.whizpool.supportsystem.utils.spToPx


class Dialog(
    private val context: Context,
) {

    var alertDialogCallbacks: AlertDialogCallbacks? = null

    private val binding by lazy {
        SendDialogBinding.inflate(LayoutInflater.from(context))
    }

    private lateinit var dialog: BottomSheetDialog
    fun showAlert() {
//        binding = SendDialogBinding.inflate(LayoutInflater.from(context))

        dialog = BottomSheetDialog(context).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            setContentView(binding.root)
//            setTitle(titleName)

            EdgeToEdgeUtils.applyEdgeToEdge(window!!, true)

            behavior.apply {
                halfExpandedRatio = 0.95f
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                skipCollapsed = true
            }
        }


//        val dialogMain = dialog.findViewById<MaterialCardView>(R.id.dialog_main)
//        val title = dialog.findViewById<TextView>(R.id.title)
//        title.text = titleName
//        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
//        val reason = dialog.findViewById<EditText>(R.id.reason)
//        val send = dialog.findViewById<MaterialButton>(R.id.send)
//        val showDetails = dialog.findViewById<MaterialButton>(R.id.showDetails)
        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.90).toInt()


        dialog.window?.apply {
            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
//        if (mainDialogBackground != null) {
//            dialogMain.setBackgroundDrawable(mainDialogBackground)
//        }


        with(binding) {

//            dialogMain.layoutParams.apply {
//                this.width = width
//                this.height = height
//            }.also {
//                dialogMain.layoutParams = it
//            }

            reason.requestFocus()

            bottomSheetHandle.setOnClickListener {
                dialog.dismiss()
            }


            send.setOnClickListener {

                if (reason.text?.isNotEmpty() == true) {
                    dialog.dismiss()

                    alertDialogCallbacks?.sendButtonClick(reason.text.toString())
//                Toast.makeText(context, "Dismissed..!!", Toast.LENGTH_SHORT).show()
                } else {
                    reason.error = SLog.emptyAlertMessage
//                    alertDialog(context)
                }
            }

            showDetails.setOnClickListener {
//                detailsLayout.isVisible = !detailsLayout.isVisible
//            alertDialogCallbacks.skipButtonClick()
//            dialog.dismiss()
                //sendLog(context, SendType.WITHOUT_MESSAGE)


            }
        }

        dialog.show()

    }

    interface AlertDialogCallbacks {
        fun sendButtonClick(message: String)
        fun skipButtonClick()
    }

//    private fun alertDialog(context: Context) {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
//        builder.setTitle("Alert!")
//        builder.setMessage(SLog.emptyAlertMessage)
//        builder.setNegativeButton("Ok"
//        ) { dialog, which -> dialog.dismiss() }
//        val diag: AlertDialog = builder.create()
//        diag.show()
//    }


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
                reasonLayout.typeface = it
                send.typeface = it
            }
        }
    }

    fun setInputDrawable(drawable: Drawable?) = apply {
        binding.reasonLayout.background = drawable
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
            binding.title.textSize = context.spToPx(it)
        }
    }

    fun setButtonTextSize(sp: Float?) = apply {
        sp?.let {
            binding.send.textSize = context.spToPx(it)
        }
    }

    fun setSendButtonBackgroundColor(@ColorInt color: Int?) = apply {
        color?.let {
            binding.send.backgroundTintList = ColorStateList.valueOf(it)
        }
    }

}