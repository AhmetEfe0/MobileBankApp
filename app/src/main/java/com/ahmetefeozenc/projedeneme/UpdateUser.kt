package com.ahmetefeozenc.projedeneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.FragmentUpdateUserBinding

class UpdateUser : Fragment() {
    private var _binding: FragmentUpdateUserBinding? = null
    private val binding get() = _binding!!
    lateinit var adguncelletxt: EditText
    lateinit var soyadguncelletxt: EditText
    lateinit var bilgiguncellebtn: Button
    lateinit var myDataBase:MyDataBase
    lateinit var tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(requireContext())
        tc = arguments?.getString("tc").toString()
        guncelleButtonClick()

    }
    fun bindingEkle(){
        adguncelletxt=binding.adguncelletxt
        soyadguncelletxt=binding.soyadguncelletxt
        bilgiguncellebtn=binding.bilgiguncellebtn
        myDataBase = MyDataBase()
    }
    fun guncelleButtonClick(){
        bilgiguncellebtn.setOnClickListener(){
            val guncelad = adguncelletxt.text.toString()
            val guncelsoyad = soyadguncelletxt.text.toString()

            if (guncelad.isNotEmpty() && guncelsoyad.isNotEmpty()) {
                myDataBase.KullaniciAdSoyadGuncelle(tc.toString(), guncelad, guncelsoyad)
                toastmsg(requireContext(),"Kullanıcının adı ve soyadı başarı bir şekilde güncellendi")
            } else if (guncelad.isNotEmpty()) {
                myDataBase.KullaniciAdSoyadGuncelle(tc.toString(), guncelad, "")
                toastmsg(requireContext(),"Kullanıcının adı başarı bir şekilde güncellendi")
            } else if (guncelsoyad.isNotEmpty()) {
                myDataBase.KullaniciAdSoyadGuncelle(tc.toString(), "", guncelsoyad)
                toastmsg(requireContext(),"Kullanıcının soyadı başarı bir şekilde güncellendi")
            }
        }
    }
}