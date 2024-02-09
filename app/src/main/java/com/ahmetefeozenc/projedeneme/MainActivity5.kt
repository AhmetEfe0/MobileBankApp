package com.ahmetefeozenc.projedeneme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ahmetefeozenc.projedeneme.databinding.ActivityMain5Binding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity5 : AppCompatActivity() {
    lateinit var binding: ActivityMain5Binding
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var textview17: TextView
    lateinit var myDataBase:MyDataBase
    lateinit var tc: String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain5Binding.inflate(layoutInflater)
        var view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(this)
        tc = intent.getStringExtra("tc").toString()
        replaceFragment(User())
        ClickBottom()
        İsimYaz()





    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
    fun ClickBottom(){
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.user->replaceFragment(User())
                R.id.emp->replaceFragment(Employee())
                else->{
                }
            }
            true
        }
    }
    fun İsimYaz(){
        val ad = myDataBase.KullaniciBilgiGetir("yoneticiler",tc, "ad")

        if (ad!=null){
            textview17.text="Merhaba "+ad
        }else{
            textview17.text="Merhaba"
        }
    }
    fun bindingEkle(){
        bottomNavigationView=binding.bottomNavigationView
        textview17=binding.textView17
        myDataBase = MyDataBase()
    }
}