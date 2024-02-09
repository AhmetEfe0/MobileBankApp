package com.ahmetefeozenc.projedeneme

import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.text.NumberFormat
import java.util.Locale

class AppHelper {
    companion object {
        fun toastmsg(context: Context, message: String) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.show()
        }
        fun IntentOluştur(context: Context, targetActivity: Class<*>) {
            val intent = Intent(context, targetActivity)
            context.startActivity(intent)
        }
        fun formatBakiye(bakiye: Double): String {
            val locale = Locale("tr", "TR")
            val numberFormat = NumberFormat.getNumberInstance(locale)
            numberFormat.minimumFractionDigits = 2
            numberFormat.maximumFractionDigits = 2

            return numberFormat.format(bakiye)
        }

    }

}