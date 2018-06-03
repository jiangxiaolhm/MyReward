package com.aquaowlet.myreward.dialog

import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.util.Constant
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        val title = intent.extras.getString(Constant.INTENT_DIALOG_TITLE)
        val message = intent.extras.getString(Constant.INTENT_DIALOG_MESSAGE)
        val icon = intent.extras.getString(Constant.INTENT_DIALOG_ICON)

        setTitle(title)
        text_message.text = message
        ic_icon.iconText = icon
        when (icon) {
            getString(R.string.ic_delete) -> {
                @Suppress("DEPRECATION")
                ic_icon.iconColor = ColorStateList.valueOf(resources.getColor(R.color.color_alert))
            }
        }

        confirm_action.setOnRippleCompleteListener {
            finish()
        }

        cancel_action.setOnRippleCompleteListener {
            finish()
        }
    }
}
