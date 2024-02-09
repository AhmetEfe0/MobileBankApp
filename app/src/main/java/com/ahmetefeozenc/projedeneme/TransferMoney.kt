package com.ahmetefeozenc.projedeneme

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.formatBakiye
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.FragmentTransferBinding
import com.ahmetefeozenc.projedeneme.databinding.FragmentTransferMoneyBinding

class TransferMoney : Fragment() {
    private var _binding: FragmentTransferMoneyBinding? = null
    private val binding get() = _binding!!
    lateinit var myDataBase:MyDataBase
    lateinit var textView18: TextView
    lateinit var textView19: TextView
    lateinit var gonderilecekparatxt: EditText
    lateinit var gonderbtn: Button
    lateinit var gonderilecektc: String
    lateinit var k_tc: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(requireContext())
        TextViewKısıtla()
        gonderilecektc = arguments?.getString("gonderilecektc").toString()
        k_tc = arguments?.getString("tc").toString()
        isimBakiyeYazdir()
        gonderButtonClick()

    }
    fun TextViewKısıtla(){
        gonderilecekparatxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().contains(".")) {
                    val decimalPlaces = editable.toString().substringAfter(".").length
                    if (decimalPlaces > 2) {
                        gonderilecekparatxt.setText(editable.toString().substring(0, editable.toString().indexOf(".") + 3))
                        gonderilecekparatxt.setSelection(gonderilecekparatxt.text.length)
                    }
                }
            }
        })
    }
    fun bindingEkle(){
        myDataBase = MyDataBase()
        textView18=binding.textView18
        textView19=binding.textView19
        gonderilecekparatxt=binding.gonderilecekparatxt
        gonderbtn=binding.gonderbtn
    }
    fun isimBakiyeYazdir(){
        val AdSoyad=myDataBase.KullaniciBilgiGetir("kullanicilar",gonderilecektc.toString(),"ad")+" "+myDataBase.KullaniciBilgiGetir("kullanicilar",gonderilecektc.toString(),"soyad")
        val hesapbakiye=myDataBase.KullaniciBilgiGetir("kullanicilar",k_tc.toString(),"bakiye")
        textView18.text="Gönderliecek Kişi: "+AdSoyad
        textView19.text="Bakiyeniz: "+formatBakiye(hesapbakiye!!.toDouble())+" TL"
    }
    fun gonderButtonClick(){
        gonderbtn.setOnClickListener(){
            val para=gonderilecekparatxt.text.toString()

            if (para.isNotEmpty()){
                if (myDataBase.KullaniciBilgiGetir("kullanicilar",k_tc.toString(),"bakiye")?.toDouble() == 0.0){
                    toastmsg(requireContext(),"Bakiyeniz 0 TL")
                } else {
                    val bakiye = myDataBase.KullaniciBilgiGetir("kullanicilar",k_tc.toString(),"bakiye")?.toDouble() ?: 0.0
                    if (bakiye >= para.toDouble()){

                        myDataBase.BakiyeEksilt(requireContext(),k_tc.toString(),para.toDouble())
                        myDataBase.BakiyeEkle(gonderilecektc.toString(),para.toDouble())
                        myDataBase.ParaTransferiYap(k_tc.toString(),gonderilecektc.toString(),para.toDouble())
                        val hesapbakiye=myDataBase.KullaniciBilgiGetir("kullanicilar",k_tc.toString(),"bakiye")
                        textView19.text="Bakiyeniz: "+formatBakiye(hesapbakiye!!.toDouble())+" TL"
                        toastmsg(requireContext(),"Para başarılı bir şekilde gönderildi")
                    } else {
                        toastmsg(requireContext(),"Yeterli bakiyeniz bulunmamaktadır.")
                    }
                }
            }else{
                toastmsg(requireContext(),"Lütfen geçerli bir para miktarı giriniz.")
            }
        }
    }
}