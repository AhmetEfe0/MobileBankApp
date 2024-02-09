package com.ahmetefeozenc.projedeneme

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.IntentOluştur
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    lateinit var myDataBase:MyDataBase
    lateinit var giristctxt: EditText
    lateinit var girissifretxt: EditText
    lateinit var girisbtn: Button
    lateinit var database: SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain3Binding.inflate(layoutInflater)
        var view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        giristctxt = binding.giristctxt
        girissifretxt = binding.girissifretxt
        girisbtn = binding.girisbtn

        myDataBase = MyDataBase()
        GirisButton()

    }
    fun GirisVeriKontrol() {
        var tc = giristctxt.text.toString()
        var sifre = girissifretxt.text.toString()

        if (myDataBase.VeriTabaniOlustur(applicationContext) != null) {

            if (myDataBase.KullaniciVarMi("kullanicilar", tc) && myDataBase.KullaniciBilgiGetir("kullanicilar", tc, "sifre") == sifre) {
                val intent = Intent(this, MainActivity4::class.java)
                intent.putExtra("tc", giristctxt!!.text.toString())
                startActivity(intent)
                finish()
            }

            else if (myDataBase.KullaniciVarMi("yoneticiler", tc) && myDataBase.KullaniciBilgiGetir("yoneticiler", tc, "sifre") == sifre) {
                val intent = Intent(this, MainActivity5::class.java)
                intent.putExtra("tc", giristctxt!!.text.toString())
                startActivity(intent)
                finish()
            } else {
                toastmsg(this, "Hatalı tc veya şifre")
            }
        }
    }
    fun GirisButton(){
        girisbtn.setOnClickListener {
            GirisVeriKontrol()
        }
    }
}
