package com.ahmetefeozenc.projedeneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.FragmentDeleteUserBinding

class DeleteUser : Fragment() {
    private var _binding: FragmentDeleteUserBinding? = null
    private val binding get() = _binding!!
    lateinit var myDataBase:MyDataBase
    lateinit var kullanicisilbtn: Button
    lateinit var tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeleteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        tc = arguments?.getString("tc").toString()
        DeleteUserButtonClick()

    }
    fun bindingEkle(){
        myDataBase = MyDataBase()
        kullanicisilbtn=binding.kullanicisilbtn
        myDataBase.VeriTabaniOlustur(requireContext())
    }
    fun DeleteUserButtonClick(){
        kullanicisilbtn.setOnClickListener(){
            myDataBase.KullaniciSil("kullanicilar",tc.toString())
            toastmsg(requireContext(),"Kullanıcı başarılı bir şekilde silindi.")
        }
    }
}