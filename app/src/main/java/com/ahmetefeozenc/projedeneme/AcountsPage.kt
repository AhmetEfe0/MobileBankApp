package com.ahmetefeozenc.projedeneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ahmetefeozenc.projedeneme.databinding.ActivityAcountsPageBinding

class AcountsPage : AppCompatActivity() {
    lateinit var binding: ActivityAcountsPageBinding
    lateinit var tlhesapcard: CardView
    lateinit var dolarhesapcard: CardView
    lateinit var geridonbtn:Button
    lateinit var tc: String
    lateinit var myDataBase:MyDataBase
    lateinit var tlbakiyetxt:TextView
    lateinit var dolarbakiyetxt:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAcountsPageBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        tlhesapcard=binding.tlhesapcard
        dolarhesapcard=binding.dolarhesapcard
        geridonbtn=binding.geridonbtn2
        tc = intent.getStringExtra("tc").toString()
        myDataBase = MyDataBase()
        myDataBase.VeriTabaniOlustur(this)
        tlbakiyetxt=binding.tlbakiyetxt
        dolarbakiyetxt=binding.dolarbakiyetxt

        geridonbtn.setOnClickListener(){
            finish()
        }
        tlhesapcard.setOnClickListener(){
            val intent = Intent(this, TransferHistoryActivity::class.java)
            intent.putExtra("tc", tc)
            startActivity(intent)
        }
        dolarhesapcard.setOnClickListener(){
            val intent = Intent(this, DolarHistoryActivity::class.java)
            intent.putExtra("tc", tc)
            startActivity(intent)
        }
        BakiyeYazdir()

    }
    fun BakiyeYazdir(){
        if (tc != null) {
            val bakiye = myDataBase.KullaniciBilgiGetir("kullanicilar",tc, "bakiye")
            val dolarbakiye = myDataBase.KullaniciBilgiGetir("kullanicilar",tc, "dolar")


            if (bakiye==null){
                tlbakiyetxt.text="0 TL"
            }
            else{
                tlbakiyetxt.text= AppHelper.formatBakiye(bakiye.toDouble()) +" TL"
            }
            if (dolarbakiye==null){
                dolarbakiyetxt.text="0 $"
            }
            else{
                dolarbakiyetxt.text= AppHelper.formatBakiye(dolarbakiye.toDouble()) +" $"
            }
        }
    }
}