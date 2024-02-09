package com.ahmetefeozenc.projedeneme


import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import java.util.List


class MyDataBase {
    lateinit var database: SQLiteDatabase

    fun VeriTabaniOlustur(context: Context): SQLiteDatabase {
        if (!::database.isInitialized) {
            database = context.openOrCreateDatabase("myDataBase", AppCompatActivity.MODE_PRIVATE, null)
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS kullanicilar(id INTEGER PRIMARY KEY, ad VARCHAR, soyad VARCHAR, tc VARCHAR, sifre VARCHAR, bakiye DOUBLE, dolar DOUBLE)"
            )
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS yoneticiler(id INTEGER PRIMARY KEY, ad VARCHAR, soyad VARCHAR, tc VARCHAR, sifre VARCHAR)"
            )
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS islemler(id INTEGER PRIMARY KEY, sender_tc VARCHAR, receiver_tc VARCHAR, amount DOUBLE, transaction_date DATETIME)"
            )
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS dolarislemler(id INTEGER PRIMARY KEY,tc VARCHAR, islem VARCHAR, dolaramount DOUBLE, tlamount DOUBLE, transaction_date DATETIME)"
            )
        }
        return database
    }

    fun VeritabaninaEkle(tabloAdi: String, ad: String, soyad: String, tc: String, sifre: String) {
        val values = ContentValues()
        values.put("ad", ad)
        values.put("soyad", soyad)
        values.put("tc", tc)
        values.put("sifre", sifre)

        database.insertOrThrow(tabloAdi, null, values)
    }

    fun KullaniciBilgiGetir(tabloAdi: String, tc: String, bilgiAdi: String): String? {
        val cursor = database.rawQuery("SELECT * FROM $tabloAdi WHERE tc=?", arrayOf(tc))
        var bilgi: String? = null

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(bilgiAdi)

            when (cursor.getType(columnIndex)) {
                Cursor.FIELD_TYPE_STRING -> bilgi = cursor.getString(columnIndex)
                Cursor.FIELD_TYPE_FLOAT -> bilgi = cursor.getDouble(columnIndex).toString()

                else -> bilgi = null
            }
        }

        cursor.close()
        return bilgi
    }
    fun KullaniciGetir(tabloAdi: String, aramaKelimesi: String, bilgiAdi: String): MutableList<String> {
        val users = mutableListOf<String>()
        val cursor = database.rawQuery(
            "SELECT * FROM $tabloAdi WHERE LOWER(ad || ' ' || soyad) LIKE LOWER(?)",
            arrayOf("%$aramaKelimesi%")
        )

        try {
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(bilgiAdi)
                do {
                    val tc = cursor.getString(columnIndex)
                    users.add(tc)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }
        return users
    }


    fun KullaniciVarMi(tabloAdi: String, tc: String): Boolean {
        val cursor = database.rawQuery("SELECT * FROM $tabloAdi WHERE tc=?", arrayOf(tc))
        val kullaniciVar = cursor.moveToFirst()
        cursor.close()
        return kullaniciVar
    }
    fun BakiyeEkle(tc: String, eklenecekbakiye: Double) {
        val currentBalance = KullaniciBilgiGetir("kullanicilar", tc, "bakiye")?.toDoubleOrNull() ?: 0.0
        val newBalance = currentBalance + eklenecekbakiye

        val values = ContentValues()
        values.put("bakiye", newBalance)

        database.update("kullanicilar", values, "tc=?", arrayOf(tc))
    }
    fun KullaniciAdSoyadGuncelle(tc: String, guncelad: String, guncelsoyad: String) {
        val values = ContentValues()

        // Eğer sadece ad veya sadece soyad girilmişse, sadece ilgili alanı güncelle
        if (guncelad.isNotEmpty() && guncelsoyad.isNotEmpty()) {
            values.put("ad", guncelad)
            values.put("soyad", guncelsoyad)
        } else if (guncelad.isNotEmpty()) {
            values.put("ad", guncelad)
        } else if (guncelsoyad.isNotEmpty()) {
            values.put("soyad", guncelsoyad)
        }

        // Veritabanında güncelleme işlemi
        database.update("kullanicilar", values, "tc=?", arrayOf(tc))
    }
    fun KullaniciSil(tabloAdi: String, tc: String){
        database.delete(tabloAdi, "tc=?", arrayOf(tc))
    }
    fun BakiyeEksilt(context:Context, tc: String, eksilenecekBakiye: Double) {
        val currentBalance = KullaniciBilgiGetir("kullanicilar", tc, "bakiye")?.toDoubleOrNull() ?: 0.0

        if (eksilenecekBakiye <= currentBalance) {
            val newBalance = currentBalance - eksilenecekBakiye

            val values = ContentValues()
            values.put("bakiye", newBalance)

            database.update("kullanicilar", values, "tc=?", arrayOf(tc))
        } else {
            toastmsg(context,"Bakiyeniz Yetersiz.")
        }
    }
    fun ParaTransferiYap(gonderenTc: String, alanTc: String, miktar: Double) {
        val values = ContentValues()
        values.put("sender_tc", gonderenTc)
        values.put("receiver_tc", alanTc)
        values.put("amount", miktar)
        values.put("transaction_date", System.currentTimeMillis())

        database.insertOrThrow("islemler", null, values)
    }
    fun KullaniciIslemGecmisi(tc: String): MutableList<String> {
        val transactions = mutableListOf<String>()
        val cursor = database.rawQuery(
            "SELECT * FROM islemler WHERE sender_tc=? OR receiver_tc=?",
            arrayOf(tc, tc)
        )

        try {
            if (cursor != null && cursor.moveToFirst()) {
                val amountIndex = cursor.getColumnIndex("amount")
                val senderIndex = cursor.getColumnIndex("sender_tc")
                val receiverIndex = cursor.getColumnIndex("receiver_tc")
                val dateIndex = cursor.getColumnIndex("transaction_date")

                do {
                    val sender = cursor.getString(senderIndex)
                    val receiver = cursor.getString(receiverIndex)
                    val amount = cursor.getDouble(amountIndex)
                    val date = cursor.getLong(dateIndex)

                    val transactionInfo = "Gönderen: $sender, Alıcı: $receiver, Miktar: $amount, Tarih: $date"
                    transactions.add(transactionInfo)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        return transactions
    }
    fun KullaniciDolarIslemGecmisi(tc: String): MutableList<String> {
        val transactions = mutableListOf<String>()
        val cursor = database.rawQuery(
            "SELECT * FROM dolarislemler WHERE tc=?",
            arrayOf(tc)
        )

        try {
            if (cursor != null && cursor.moveToFirst()) {

                val dolaramountIndex = cursor.getColumnIndex("dolaramount")
                val tlamountIndex = cursor.getColumnIndex("tlamount")
                val islemIndex = cursor.getColumnIndex("islem")
                val dateIndex = cursor.getColumnIndex("transaction_date")

                do {
                    val dolaramount = cursor.getDouble(dolaramountIndex)
                    val tlamount = cursor.getDouble(tlamountIndex)
                    val islem = cursor.getString(islemIndex)
                    val date = cursor.getLong(dateIndex)

                    val transactionInfo = "İşlem: $islem, Dolar Miktarı: $dolaramount, TL Miktarı: $tlamount, Tarih: $date"
                    transactions.add(transactionInfo)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
        }

        return transactions
    }
    fun DolarIslemiYap(tc: String, islem: String, dolaramount: Double, tlamount: Double) {
        val values = ContentValues()
        values.put("tc", tc)
        values.put("islem", islem)
        values.put("dolaramount", dolaramount)
        values.put("tlamount", tlamount)
        values.put("transaction_date", System.currentTimeMillis())

        database.insertOrThrow("dolarislemler", null, values)
    }
    fun DolarEkle(tc: String, eklenecekBakiyeDolar: Double) {
        val currentBalanceDolar = KullaniciBilgiGetir("kullanicilar", tc, "dolar")?.toDoubleOrNull() ?: 0.0
        val newBalanceDolar = currentBalanceDolar + eklenecekBakiyeDolar

        val values = ContentValues()
        values.put("dolar", newBalanceDolar)

        database.update("kullanicilar", values, "tc=?", arrayOf(tc))
    }
    fun DolarEksilt(tc: String, eksiltilecekBakiyeDolar: Double) {
        val currentBalanceDolar = KullaniciBilgiGetir("kullanicilar", tc, "dolar")?.toDoubleOrNull() ?: 0.0
        val newBalanceDolar = currentBalanceDolar - eksiltilecekBakiyeDolar

        val values = ContentValues()
        values.put("dolar", newBalanceDolar)

        database.update("kullanicilar", values, "tc=?", arrayOf(tc))
    }
}