package com.ahmetefeozenc.projedeneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.FragmentTransferBinding

class Transfer : Fragment() {
    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!
    lateinit var transfertctxt: EditText
    lateinit var kontrolbtn: Button
    lateinit var myDataBase:MyDataBase
    lateinit var k_tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(requireContext())
        k_tc=arguments?.getString("tc").toString()
        kontrolButtonClick()

    }
    fun bindingEkle(){
        transfertctxt=binding.transfertctxt
        kontrolbtn=binding.kontrolbtn
        myDataBase = MyDataBase()
    }
    fun kontrolButtonClick(){
        kontrolbtn.setOnClickListener(){
            val transfertc=transfertctxt.text.toString()
            if(transfertc.length==11){
                if (myDataBase.KullaniciVarMi("kullanicilar",transfertc)){
                    val fragmentManager: FragmentManager = childFragmentManager
                    val userInfoFragment = TransferMoney()
                    val bundle = Bundle()
                    bundle.putString("gonderilecektc", transfertc)
                    bundle.putString("tc", k_tc)
                    userInfoFragment.arguments = bundle

                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frame_layout, userInfoFragment)
                    fragmentTransaction.commit()
                }else{
                    toastmsg(requireContext(),"Kullanıcı bulunamadı")
                }
            }
            else{
                toastmsg(requireContext(),"Tc numarası 11 haneli olmalıdır.")
            }
        }
    }

}