package com.ahmetefeozenc.projedeneme

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.IntentOluştur
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.formatBakiye
import com.ahmetefeozenc.projedeneme.databinding.ActivityCurrencyAccountBinding
import com.ahmetefeozenc.projedeneme.databinding.FragmentHomeBinding

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var textView4: TextView
    lateinit var bakiyetxt: TextView
    lateinit var hesapcard: CardView
    lateinit var dovizcard: CardView
    lateinit var myDataBase:MyDataBase
    lateinit var tc: String
    lateinit var hesaplartxtbtn:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingEkle()
        tc=arguments?.getString("tc").toString()
        isimBakiyeYazdır()
        hesapCardClick()
        dovizCardClick()

        hesaplartxtbtn.setOnClickListener(){
            val intent = Intent(requireContext(), AcountsPage::class.java)
            intent.putExtra("tc", tc)
            startActivity(intent)
        }

    }
    fun bindingEkle(){
        textView4 = binding.textView4
        hesapcard = binding.hesapcard
        dovizcard = binding.dovizcard
        myDataBase = MyDataBase()
        bakiyetxt=binding.bakiyetxt
        myDataBase.VeriTabaniOlustur(requireContext())
        hesaplartxtbtn=binding.hesaplartxtbtn
    }
    fun isimBakiyeYazdır(){
        if (tc != null) {
            val ad = myDataBase.KullaniciBilgiGetir("kullanicilar",tc, "ad")
            val bakiye = myDataBase.KullaniciBilgiGetir("kullanicilar",tc, "bakiye")
            textView4.text = "İyi Günler "+ ad

            if (bakiye==null){
                bakiyetxt.text="0 TL"
            }
            else{
                bakiyetxt.text=formatBakiye(bakiye.toDouble())+" TL"
            }
        }
    }
    fun hesapCardClick(){
        hesapcard.setOnClickListener {
            val intent = Intent(requireContext(), TransferHistoryActivity::class.java)
            intent.putExtra("tc", tc)
            startActivity(intent)
        }
    }
    fun dovizCardClick(){
        dovizcard.setOnClickListener {
            val intent = Intent(requireContext(), CurrencyAccountActivity::class.java)
            intent.putExtra("tc", tc)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        isimBakiyeYazdır() // Bakiye her onResume çağrıldığında güncellenecek
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}