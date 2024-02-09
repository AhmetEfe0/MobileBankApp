package com.ahmetefeozenc.projedeneme

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.IntentOluştur
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var adtxt: EditText
    lateinit var soyadtxt: EditText
    lateinit var tctxt: EditText
    lateinit var sifretxt: EditText
    lateinit var sifretekrartxt: EditText
    lateinit var sozlesmekontrol: CheckBox
    lateinit var kayitbtn: Button
    lateinit var database: SQLiteDatabase
    lateinit var kullanicilarliste: ArrayList<Kullanicilar>
    lateinit var myDataBase:MyDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMain2Binding.inflate(layoutInflater)
        var view =binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        bindigEkle()
        myDataBase.VeriTabaniOlustur(this)
        kayitButtonClick()


    }
    private fun KullaniciEkle(){
        var ad=adtxt.text.toString()
        var soyad=soyadtxt.text.toString()
        var tc=tctxt.text.toString()
        var sifre=sifretxt.text.toString()
        var sifretekrar=sifretekrartxt.text.toString()

        if(ad.isBlank() || soyad.isBlank() || tc.isBlank() || sifre.isBlank() || sifretekrar.isBlank()){
            toastmsg(this,"Lütfen bütün bilgileri doldurunuz.")
        }
        else{
            if(tc.length!=11){
                toastmsg(this,"TC numaranız 11 haneli olmalıdır.")
            }
            else{
                if (sifre!=sifretekrar){
                    toastmsg(this,"Girilen şifreler aynı olmalıdır")
                }
                else{
                    if(sozlesmekontrol.isChecked){
                        if (myDataBase.KullaniciVarMi("kullanicilar",tc)) {
                            toastmsg(this, "Bu TC numarasına sahip bir kullanıcı kayıtlı.")
                        } else {
                            myDataBase.VeritabaninaEkle("kullanicilar",ad, soyad, tc, sifre)
                            IntentOluştur(this, MainActivity3::class.java)
                        }
                    }
                    else{
                        toastmsg(this,"Lütfen sözleşmeyi onaylayın.")
                    }
                }
            }
        }
    }
    private fun kayitButtonClick(){
        kayitbtn.setOnClickListener(){
            KullaniciEkle()
        }
    }
    fun bindigEkle(){
        adtxt=binding.adtxt
        soyadtxt=binding.soyadtxt
        tctxt=binding.tctxt
        sifretxt=binding.sifretxt
        sifretekrartxt=binding.sifretekrartxt
        sozlesmekontrol=binding.sozlesmekontrol
        kayitbtn=binding.kayitbtn
        kullanicilarliste= ArrayList()
        myDataBase=MyDataBase()
    }

}