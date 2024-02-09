package com.ahmetefeozenc.projedeneme

import TransactionAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.formatBakiye
import com.ahmetefeozenc.projedeneme.databinding.ActivityTransferHistoryBinding

class TransferHistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityTransferHistoryBinding
    lateinit var myDataBase: MyDataBase
    lateinit var bakiyetxt: TextView
    lateinit var gecmisUyariText: TextView
    lateinit var geridonbtn: Button
    lateinit var recyclerView: RecyclerView
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTransferHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter(mutableListOf())
        recyclerView.adapter = transactionAdapter

        tc = intent.getStringExtra("tc").toString()
        bakiyeYazdir()
        islemYazdir()
        geriButtonClick()

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
    fun bindingEkle(){
        myDataBase = MyDataBase()
        bakiyetxt = binding.bakiyetxt
        geridonbtn = binding.geridonbtn
        recyclerView = binding.recyclerView
        gecmisUyariText=binding.gecmisUyariText
    }
    fun bakiyeYazdir(){
        val bakiye=myDataBase.KullaniciBilgiGetir("kullanicilar", tc.toString(), "bakiye")
        if(bakiye==null){
            bakiyetxt.text ="0 TL"
        }else{
            bakiyetxt.text= formatBakiye(bakiye.toDouble()) +" TL"
        }
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
    fun geriButtonClick(){
        geridonbtn.setOnClickListener {
            finish()
        }
    }
}
