package com.ahmetefeozenc.projedeneme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ahmetefeozenc.projedeneme.databinding.ActivityMain4Binding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity4 : AppCompatActivity() {
    lateinit var binding: ActivityMain4Binding
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain4Binding.inflate(layoutInflater)
        var view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        bottomNavigationView=binding.bottomNavigationView
        tc = intent.getStringExtra("tc").toString()
        replaceFragment(Home())
        bottomNavigation()
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putString("tc", tc)
        fragment.arguments = bundle

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    fun bottomNavigation(){
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->replaceFragment(Home())
                R.id.transfer->replaceFragment(Transfer())
                else->{

                }
            }
            true
        }
    }

}