package com.ahmetefeozenc.projedeneme

import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ahmetefeozenc.projedeneme.AppHelper.Companion.toastmsg
import com.ahmetefeozenc.projedeneme.databinding.ActivityCurrencyAccountBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class CurrencyAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCurrencyAccountBinding
    private lateinit var myDataBase: MyDataBase
    private lateinit var tc: String
    private lateinit var denemetxt: TextView
    private lateinit var lastUpdateTxt: TextView
    private lateinit var komisyonfiyat: TextView
    private lateinit var toplamfiyat: TextView
    private lateinit var dovizadettxt: EditText
    private lateinit var dovizonaylabtn: Button
    private lateinit var usdalbtn: Button
    private lateinit var usdsatbtn: Button
    private lateinit var geridonbtn: Button
    private var usdToTryRate by Delegates.notNull<Double>()
    private var komisyonfiyati by Delegates.notNull<Double>()
    lateinit var islem: String
    lateinit var tlbakiye:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        denemetxt = binding.denemetxt
        lastUpdateTxt = binding.lastUpdateTxt
        myDataBase = MyDataBase()
        myDataBase.VeriTabaniOlustur(this)
        tc = intent.getStringExtra("tc").toString()
        komisyonfiyat=binding.komisyonfiyat
        toplamfiyat=binding.toplamfiyat
        dovizadettxt=binding.dovizadettxt
        dovizonaylabtn=binding.dovizonaylabtn
        usdalbtn=binding.usdalbtn
        usdsatbtn=binding.usdsatbtn
        tlbakiye=binding.tlbakiye
        geridonbtn=binding.geridonbtn3


        Gizle()
        val apiKey = "e4c9a75948fde28f8436ea79"
        val baseCurrencyCode = "USD"
        val apiUrl = "https://v6.exchangerate-api.com/v6/$apiKey/latest/$baseCurrencyCode"

        FetchExchangeRatesTask().execute(apiUrl)

        usdSatBtnClick()
        usdAlBtnClick()
        dovizadettxt()
        dovizOnaylaBtnClick()
        geriDonBtnClick()

    }

    inner class FetchExchangeRatesTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            val apiUrl = params[0]
            return try {
                val url = URL(apiUrl)
                val urlConnection = url.openConnection() as HttpURLConnection
                try {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line).append('\n')
                    }
                    response.toString()
                } finally {
                    urlConnection.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("JSON_REQUEST_ERROR", "Error making HTTP request: ${e.message}")
                "error"
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            if (result != "error" && result.isNotEmpty()) {
                Log.d("JSON_RESPONSE", result)

                try {
                    val jsonObject = JSONObject(result)

                    if (jsonObject.has("result") && jsonObject.getString("result") == "success") {
                        val conversionRates = jsonObject.getJSONObject("conversion_rates")
                        usdToTryRate = conversionRates.getDouble("TRY") // USD to TRY döviz kuru

                        denemetxt.text = "$usdToTryRate TL"

                        // Son güncelleme tarihini alma
                        val lastUpdateUnix = jsonObject.getLong("time_last_update_unix")
                        val lastUpdateUtc = jsonObject.getString("time_last_update_utc")

                        // Unix tarihini daha okunabilir bir formata çevirme
                        val lastUpdateDate = java.util.Date(lastUpdateUnix * 1000)
                        val lastUpdateFormatted = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(lastUpdateDate)

                        lastUpdateTxt.text = "$lastUpdateFormatted"
                    } else {
                        val errorType = jsonObject.optString("error-type", "Bilinmeyen Hata")
                        Log.e("JSON_PARSING_ERROR", "Error Type: $errorType")
                        denemetxt.text = "Döviz kuru alınırken hata oluştu. Hata Türü: $errorType"
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("JSON_PARSING_ERROR", "Error parsing JSON response: ${e.message}")
                    denemetxt.text = "Döviz kuru alınırken hata oluştu. JSON yanıtı ayrıştırılırken hata."
                }
            } else {
                toastmsg(this@CurrencyAccountActivity,"Lütfen internet bağlantınızı kontrol ediniz")
                finish()
            }
        }
    }
    fun updateKomisyonFiyati(kur: Double, alMi: Boolean):Double {
        // USD Al butonuna basıldıysa komisyon fiyatını %3 fazla alın
        // USD Sat butonuna basıldıysa komisyon fiyatını %3 az alın
        val komisyonOrani = if (alMi) 0.03 else -0.03
        val komisyonFiyati =kur+(kur * komisyonOrani)

        komisyonfiyat.text = "Komisyon Fiyatı: ${komisyonFiyati.formatBakiye()} TL"
        return komisyonFiyati

    }
    fun Double.formatBakiye(): String {
        // Double değeri TL cinsinden bir dizeye dönüştürmek için bir örnek format
        return String.format(Locale.getDefault(), "%.2f", this) // Bu örnekte, 2 ondalık basamak gösteriliyor
    }
    fun BakiyeYazdir(bilgi:String){
        var doviz=""
        var hesapbakiye= 0.0
        if(bilgi=="bakiye"){
            doviz="TL"
        }
        else if(bilgi=="dolar"){
            doviz="$"
        }
        if (myDataBase.KullaniciBilgiGetir("kullanicilar",tc,bilgi)!=null){
            hesapbakiye=myDataBase.KullaniciBilgiGetir("kullanicilar",tc,bilgi)!!.toDouble()
        }
        tlbakiye.text="Bakiyeniz: "+ AppHelper.formatBakiye(hesapbakiye!!.toDouble()) +" $doviz"
    }
    fun Gizle(){
        komisyonfiyat.visibility = View.GONE
        toplamfiyat.visibility = View.GONE
        dovizadettxt.visibility = View.GONE
        dovizonaylabtn.visibility = View.GONE
        tlbakiye.visibility = View.GONE
    }
    fun Goster(){
        komisyonfiyat.visibility = View.VISIBLE
        toplamfiyat.visibility = View.VISIBLE
        dovizadettxt.visibility = View.VISIBLE
        dovizonaylabtn.visibility = View.VISIBLE
        tlbakiye.visibility = View.VISIBLE
    }
    fun usdSatBtnClick(){
        usdsatbtn.setOnClickListener {
            // USD Sat butonuna basıldığında
            Goster()
            BakiyeYazdir("dolar")
            val usdKuru = usdToTryRate
            updateKomisyonFiyati(usdKuru, false)
            komisyonfiyati=updateKomisyonFiyati(usdKuru, false)
            dovizadettxt.text.clear()
            islem="sat"
        }
    }
    fun usdAlBtnClick(){
        usdalbtn.setOnClickListener {
            // USD Al butonuna basıldığında
            Goster()
            BakiyeYazdir("bakiye")
            val usdKuru = usdToTryRate
            updateKomisyonFiyati(usdKuru, true)
            komisyonfiyati=updateKomisyonFiyati(usdKuru, true)
            dovizadettxt.text.clear()
            islem="al"
        }
    }
    fun dovizadettxt(){
        dovizadettxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val dovizAdet = s.toString().toDoubleOrNull()
                if (dovizAdet != null) {
                    val toplamFiyat = (komisyonfiyati) * dovizAdet

                    toplamfiyat.text = "Toplam Fiyat: ${toplamFiyat.formatBakiye()} TL"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    fun dovizOnaylaBtnClick(){
        dovizonaylabtn.setOnClickListener {
            val dovizAdet = dovizadettxt.text.toString().toDoubleOrNull()
            if (dovizAdet != null) {
                val toplamFiyat = (komisyonfiyati) * dovizAdet

                if (islem=="al") {
                    // Hesabında yeterli TL var mı kontrol et
                    if (myDataBase.KullaniciBilgiGetir("kullanicilar",tc,"bakiye")!!.toDouble()>=toplamFiyat) {
                        // İşlemi gerçekleştir
                        myDataBase.BakiyeEksilt(this@CurrencyAccountActivity,tc,toplamFiyat)
                        myDataBase.DolarEkle(tc,dovizAdet)
                        myDataBase.DolarIslemiYap(tc,"1",dovizAdet,toplamFiyat)
                        toastmsg(this@CurrencyAccountActivity, "Döviz alımı başarıyla gerçekleşti")
                        finish()
                        return@setOnClickListener
                    } else {
                        toastmsg(this@CurrencyAccountActivity, "Yetersiz TL bakiyesi")
                    }
                }

                else if (islem=="sat") {
                    // Hesabında yeterli USD var mı kontrol et
                    if (myDataBase.KullaniciBilgiGetir("kullanicilar",tc,"dolar")!!.toDouble()>=dovizAdet) {
                        // İşlemi gerçekleştir
                        myDataBase.BakiyeEkle(tc,toplamFiyat)
                        myDataBase.DolarEksilt(tc,dovizAdet)
                        myDataBase.DolarIslemiYap(tc,"0",dovizAdet,toplamFiyat)
                        toastmsg(this@CurrencyAccountActivity, "Döviz satışı başarıyla gerçekleşti")
                        finish()
                        return@setOnClickListener
                    } else {
                        toastmsg(this@CurrencyAccountActivity, "Yetersiz USD bakiyesi")
                    }
                } else {
                    toastmsg(this@CurrencyAccountActivity, "Geçersiz işlem")
                }
            } else {
                toastmsg(this@CurrencyAccountActivity, "Lütfen geçerli bir döviz miktarı giriniz.")
            }
        }
    }
    fun geriDonBtnClick(){
        geridonbtn.setOnClickListener(){
            finish()
        }
    }
}

