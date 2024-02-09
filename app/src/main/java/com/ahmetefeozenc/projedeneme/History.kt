package com.ahmetefeozenc.projedeneme

import TransactionAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.databinding.FragmentHistoryBinding

class History : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    lateinit var myDataBase: MyDataBase
    lateinit var tc: String
    lateinit var recyclerView: RecyclerView
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var gecmisUyariText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDataBase = MyDataBase()
        myDataBase.VeriTabaniOlustur(requireContext())
        gecmisUyariText=binding.gecmisUyariText
        tc = arguments?.getString("tc").toString()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(mutableListOf())
        recyclerView.adapter = transactionAdapter
        islemYazdir()


    }
    fun islemYazdir(){
        val transactions = myDataBase.KullaniciIslemGecmisi(tc)


        val reversedTransactions = transactions.reversed()

        if (reversedTransactions.isEmpty()) {
            gecmisUyariText.text = "Geçmiş işleminiz bulunmamaktadır."
        } else {
            if (reversedTransactions.size <= 20) {
                for (transactionInfo in reversedTransactions) {
                    addTransactionFromInfo(transactionInfo, tc)
                }
            } else {
                for (i in reversedTransactions.size - 20 until reversedTransactions.size) {
                    val transactionInfo = reversedTransactions[i]
                    addTransactionFromInfo(transactionInfo, tc)
                }
            }
        }
    }
    private fun addTransactionFromInfo(transactionInfo: String, currentUserTC: String) {
        val parts = transactionInfo.split(", ")
        val sender = parts[0].substringAfter(": ")
        val receiver = parts[1].substringAfter(": ")
        val amount = getTransactionAmount(parts[2].substringAfter(": ").toDouble(), sender, currentUserTC)
        val date = parts[3].substringAfter(": ").toLong()

        val transactionType = if (sender == currentUserTC) {
            TransactionType.SENT
        } else {
            TransactionType.RECEIVED
        }

        val transaction = Transaction(getTransactionUserName(sender, receiver, transactionType, currentUserTC), amount, date.toString(), transactionType)
        transactionAdapter.addTransaction(transaction)
    }
    private fun getTransactionAmount(rawAmount: Double, sender: String, currentUserTC: String): Double {
        return if (sender == currentUserTC) {
            -rawAmount
        } else {
            rawAmount
        }
    }
    private fun getTransactionUserName(sender: String, receiver: String, transactionType: TransactionType, currentUserTC: String): String {
        return when (transactionType) {
            TransactionType.SENT -> myDataBase.KullaniciBilgiGetir("kullanicilar", receiver, "ad") +
                    " " + myDataBase.KullaniciBilgiGetir("kullanicilar", receiver, "soyad")
            TransactionType.RECEIVED -> myDataBase.KullaniciBilgiGetir("kullanicilar", sender, "ad") +
                    " " + myDataBase.KullaniciBilgiGetir("kullanicilar", sender, "soyad")
        }
    }


}
