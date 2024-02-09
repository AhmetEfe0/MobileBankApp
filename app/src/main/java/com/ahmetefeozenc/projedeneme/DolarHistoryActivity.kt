package com.ahmetefeozenc.projedeneme

import DollarTransactionAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.formatBakiye
import com.ahmetefeozenc.projedeneme.databinding.ActivityDolarHistoryBinding

class DolarHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDolarHistoryBinding
    private lateinit var myDataBase: MyDataBase
    private lateinit var dolarbakiyetxt: TextView
    private lateinit var gecmisUyariText: TextView
    private lateinit var geridonbtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dollarTransactionAdapter: DollarTransactionAdapter
    private lateinit var tc: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDolarHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingEkle()
        myDataBase.VeriTabaniOlustur(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        dollarTransactionAdapter = DollarTransactionAdapter(mutableListOf())
        recyclerView.adapter = dollarTransactionAdapter

        tc = intent.getStringExtra("tc").toString()
        bakiyeYazdir()
        islemYazdir()
        geriButtonClick()
    }

    private fun bindingEkle() {
        myDataBase = MyDataBase()
        geridonbtn=binding.geridonbtn4
        recyclerView=binding.recyclerView
        dolarbakiyetxt=binding.dolarbakiyetxt
        gecmisUyariText=binding.gecmisUyariText2
    }

    private fun bakiyeYazdir() {
        val bakiye = myDataBase.KullaniciBilgiGetir("kullanicilar", tc.toString(), "dolar")
        if (bakiye == null) {
            dolarbakiyetxt.text = "0 $"
        } else {
            dolarbakiyetxt.text = formatBakiye(bakiye.toDouble()) + " $"
        }
    }

    private fun islemYazdir() {
        val transactions = myDataBase.KullaniciDolarIslemGecmisi(tc)
        val reversedTransactions = transactions.reversed()

        if (reversedTransactions.isEmpty()) {
            gecmisUyariText.text = "Geçmiş işleminiz bulunmamaktadır."
        } else {
            if (reversedTransactions.size <= 20) {
                for (transactionInfo in reversedTransactions) {
                    addTransactionFromInfo(transactionInfo)
                }
            } else {
                for (i in reversedTransactions.size - 20 until reversedTransactions.size) {
                    val transactionInfo = reversedTransactions[i]
                    addTransactionFromInfo(transactionInfo)
                }
            }
        }
    }

    private fun addTransactionFromInfo(transactionInfo: String) {
        val parts = transactionInfo.split(", ")
        val dolaramount = parts[0].substringAfter(": ").toDouble()
        val tlamount = parts[1].substringAfter(": ").toDouble()
        val islem = parts[2].substringAfter(": ")
        val date = parts[3].substringAfter(": ")


        val transactions = DolarTransaction(dolaramount, tlamount, islem, date)
        dollarTransactionAdapter.addTransaction(transactions)
    }




    private fun geriButtonClick() {
        geridonbtn.setOnClickListener {
            finish()
        }
    }
}
