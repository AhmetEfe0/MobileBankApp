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
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.FragmentAddMoneyBinding

class AddMoney : Fragment() {
    private var _binding: FragmentAddMoneyBinding? = null
    private val binding get() = _binding!!
    lateinit var addmoneytxt: EditText
    lateinit var myDataBase:MyDataBase
    lateinit var eklebtn: Button
    lateinit var tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        tc = arguments?.getString("tc").toString()
        TextViewKısıtla()
        ekleButtonClick()

    }
    fun bindingEkle(){
        addmoneytxt=binding.addmoneytxt
        myDataBase = MyDataBase()
        eklebtn=binding.eklebtn
        myDataBase.VeriTabaniOlustur(requireContext())
    }
    fun ekleButtonClick(){
        eklebtn.setOnClickListener(){
            val para=addmoneytxt.text.toString()
            if(para!=null){
                myDataBase.BakiyeEkle(tc,para.toDouble())
                toastmsg(requireContext(),"Başarılı bir şekilde para eklendi")
            }else{
               toastmsg(requireContext(),"Lütfen geçerli bir miktar giriniz.")
            }

        }
    }
    fun TextViewKısıtla(){
        addmoneytxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().contains(".")) {
                    val decimalPlaces = editable.toString().substringAfter(".").length
                    if (decimalPlaces > 2) {
                        addmoneytxt.setText(editable.toString().substring(0, editable.toString().indexOf(".") + 3))
                        addmoneytxt.setSelection(addmoneytxt.text.length)
                    }
                }
            }
        })
    }
}