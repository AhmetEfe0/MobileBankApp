package com.ahmetefeozenc.projedeneme

import UserAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.FragmentUserBinding

class User : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    lateinit var liste: RecyclerView
    lateinit var myDataBase:MyDataBase
    lateinit var isimtxt: EditText
    lateinit var arabtn: Button
    lateinit var UserAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(requireContext())

        liste.layoutManager=LinearLayoutManager(requireContext())
        UserAdapter = UserAdapter(emptyList(), myDataBase, requireFragmentManager())
        liste.adapter = UserAdapter

        araButtonClick()


    }
    fun bindingEkle(){
        liste=binding.liste
        myDataBase = MyDataBase()
        isimtxt=binding.isimtxt
        arabtn=binding.arabtn
    }
    fun araButtonClick(){
        arabtn.setOnClickListener() {
            val isim = isimtxt.text.toString().trim()
            if (isim.isNotEmpty()) {
                val tcList = myDataBase.KullaniciGetir("kullanicilar", isim, "tc")
                if (tcList.isNotEmpty()) {
                    UserAdapter.updateData(tcList)
                } else {
                    UserAdapter.clearData()
                    toastmsg(requireContext(), "Kullanıcı bulunamadı")
                }
            } else {
                toastmsg(requireContext(), "Lütfen aramak istediğiniz ismi giriniz")
            }
        }
    }

}