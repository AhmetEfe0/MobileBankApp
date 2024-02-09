package com.ahmetefeozenc.projedeneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ahmetefeozenc.projedeneme.databinding.FragmentUserInfoBinding
import androidx.fragment.app.FragmentManager
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.formatBakiye

class UserInfo : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    lateinit var geribtn: Button
    lateinit var yenilebtn: Button
    lateinit var guncellebtn: Button
    lateinit var paraeklebtn: Button
    lateinit var ksilbtn: Button
    lateinit var islemlerbtn: Button
    lateinit var textView11: TextView
    lateinit var textView12: TextView
    lateinit var textView15: TextView
    lateinit var myDataBase:MyDataBase
    lateinit var tc: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindingEkle()
        myDataBase.VeriTabaniOlustur(requireContext())
        tc = arguments?.getString("tc").toString()

        bilgileriYazdir()


        paraEkleButtonClick()
        guncelleButtonClick()
        ksilButtonClick()
        islemlerButtonClick()
        yenileButtonClick()
        geriButtonClick()

    }
    fun bindingEkle(){
        textView11=binding.textView11
        textView12=binding.textView12
        textView15=binding.textView15
        myDataBase = MyDataBase()
        paraeklebtn=binding.paraeklebtn
        yenilebtn=binding.yenilebtn
        guncellebtn=binding.guncellebtn
        geribtn=binding.geribtn
        ksilbtn=binding.ksilbtn
        islemlerbtn=binding.islemlerbtn
    }
    fun bilgileriYazdir(){
        textView12.text=tc
        textView11.text=myDataBase.KullaniciBilgiGetir("kullanicilar",tc.toString(),"ad")+" "+myDataBase.KullaniciBilgiGetir("kullanicilar",tc.toString(),"soyad")
        val bakiye = myDataBase.KullaniciBilgiGetir("kullanicilar",tc.toString(), "bakiye")
        if (bakiye==null){
            textView15.text="0 TL"
        }
        else{
            textView15.text=formatBakiye(bakiye.toDouble()) +" TL"
        }
    }
    fun fragmetOluştur(fragment: Fragment){
        val fragmentManager: FragmentManager = childFragmentManager
        val userInfoFragment = fragment
        val bundle = Bundle()
        bundle.putString("tc", tc)
        userInfoFragment.arguments = bundle

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, userInfoFragment)
        fragmentTransaction.commit()
    }
    fun paraEkleButtonClick(){
        paraeklebtn.setOnClickListener() {
            fragmetOluştur(AddMoney())
        }
    }
    fun guncelleButtonClick(){
        guncellebtn.setOnClickListener(){
            fragmetOluştur(UpdateUser())
        }
    }
    fun ksilButtonClick(){
        ksilbtn.setOnClickListener(){
            fragmetOluştur(DeleteUser())
        }
    }
    fun islemlerButtonClick(){
        islemlerbtn.setOnClickListener(){
            fragmetOluştur(History())
        }
    }
    fun yenileButtonClick(){
        yenilebtn.setOnClickListener(){
            textView11.text=myDataBase.KullaniciBilgiGetir("kullanicilar",tc.toString(),"ad")+" "+myDataBase.KullaniciBilgiGetir("kullanicilar",tc.toString(),"soyad")
            val bakiye = myDataBase.KullaniciBilgiGetir("kullanicilar",tc.toString(), "bakiye")
            if (bakiye==null){
                textView15.text="0 TL"
            }
            else{
                textView15.text= formatBakiye(bakiye.toDouble()) +" TL"
            }
        }
    }
    fun geriButtonClick(){
        geribtn.setOnClickListener(){
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.frame_layout, User())
            fragmentTransaction?.commit()
        }
    }
}

