package com.ahmetefeozenc.projedeneme

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.IntentOluştur
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var banktxt: TextView
    lateinit var kayitsayfabtn: Button
    lateinit var girissayfabtn: Button
    lateinit var myDataBase:MyDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        var view =binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        bindingEkle()
        KayitSayfaButton()
        GirisSayfaButton()


    }
    private fun KayitSayfaButton(){
        kayitsayfabtn.setOnClickListener {
            IntentOluştur(this,MainActivity2::class.java)
        }
    }
    private fun GirisSayfaButton(){
        girissayfabtn.setOnClickListener {
            IntentOluştur(this,MainActivity3::class.java)
        }
    }
    fun bindingEkle(){
        banktxt=binding.banktxt
        kayitsayfabtn=binding.kayitsayfabtn
        girissayfabtn=binding.girissayfabtn
        myDataBase=MyDataBase()
    }

}