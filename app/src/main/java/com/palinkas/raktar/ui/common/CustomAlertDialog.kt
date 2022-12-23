package com.palinkas.raktar.ui.common

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.palinkas.raktar.R

class CustomAlertDialog : DialogFragment() {
    private var positiveButtonCallback: ((dialogInterface: DialogInterface, i: Int) -> Unit)? = null
    private var negativeButtonCallback: ((dialogInterface: DialogInterface, i: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val title = arguments?.getString(TITLE)
        val message = arguments?.getString(MESSAGE)
        var positiveButtonText = arguments?.getString(POSITIVE_BUTTON_TEXT)
        var negativeButtonText = arguments?.getString(NEGATIVE_BUTTON_TEXT)

        if (positiveButtonText == null || positiveButtonText == "") {
            positiveButtonText = getString(R.string.yes)
        }
        if (negativeButtonText == null || negativeButtonText == "") {
            negativeButtonText = getString(R.string.cancel)
        }

        val builder = AlertDialog.Builder(requireActivity())

        if (title != null) builder.setTitle(title)
        if (message != null) builder.setMessage(message)

        if (positiveButtonCallback == null) {
            positiveButtonText = getString(R.string.ok)
            builder.setPositiveButton(positiveButtonText) { _, _ ->
                dismiss()
            }
        } else {
            positiveButtonCallback?.let {
                builder.setPositiveButton(positiveButtonText) { dialogInterface, i ->
                    it(dialogInterface, i)
                }
            }
        }

        if (negativeButtonCallback == null) {
            negativeButtonText = getString(R.string.cancel)
            builder.setNegativeButton(negativeButtonText) { _, _ ->
                dismiss()
            }
        } else {
            negativeButtonCallback?.let {
                builder.setNegativeButton(negativeButtonText) { dialogInterface, i ->
                    it(dialogInterface, i)
                }
            }
        }

        return builder.create()
    }

    companion object {

        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val POSITIVE_BUTTON_TEXT = "positive_button_text"
        private const val NEGATIVE_BUTTON_TEXT = "negative_button_text"

        @JvmStatic
        fun createDialog(
            title: String? = null, message: String? = null,
            positiveButtonText: String? = null,
            negativeButtonText: String? = null,
            positiveButtonCallback: ((dialogInterface: DialogInterface, i: Int) -> Unit)? = null,
            negativeButtonCallback: ((dialogInterface: DialogInterface, i: Int) -> Unit)? = null
        ): CustomAlertDialog {
            val customAlertDialog = CustomAlertDialog()

            customAlertDialog.positiveButtonCallback = positiveButtonCallback
            customAlertDialog.negativeButtonCallback = negativeButtonCallback

            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(MESSAGE, message)
            args.putString(POSITIVE_BUTTON_TEXT, positiveButtonText)
            args.putString(NEGATIVE_BUTTON_TEXT, negativeButtonText)
            customAlertDialog.arguments = args

            return customAlertDialog
        }
    }
}