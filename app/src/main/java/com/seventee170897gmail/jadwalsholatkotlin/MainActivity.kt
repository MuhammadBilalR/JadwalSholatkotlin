package com.seventee170897gmail.jadwalsholatkotlin

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private var listKota: MutableList<Kota>? = null
    private var mKotaAdapter: ArrayAdapter<Kota>? = null
    //mendeskripsikan kelas lis kota dan m kota adapter yang diambil dari sebuah class kota dimana variabel kota memilki nilai null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//mendekstriprikan tampilan nantinay yang mana diambil dari sebuah class xml yaitu activity main
        listKota = ArrayList<Kota>()
        mKotaAdapter = ArrayAdapter<Kota>(this, android.R.layout.simple_spinner_item, listKota)
        mKotaAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kotaA.adapter = mKotaAdapter
        kotaA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
//
            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val kota21 = mKotaAdapter!!.getItem(position)
                loadJadwal(kota21.id)
            }

        }

        loadKota()
    }

    private fun loadJadwal(id: Int?) {
        try {
            val id_kota = id.toString()

            val current = SimpleDateFormat("yyyy-MM-dd")
            val tanggal = current.format(Date())

            var url = "https://api.banghasan.com/sholat/format/json/jadwal/kota/$id_kota/tanggal/$tanggal"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {

                    Log.d("JadwalData", result)
                    try {
                        val jsonObj = JSONObject(result)
                        val objJadwal = jsonObj.getJSONObject("jadwal")
                        val obData = objJadwal.getJSONObject("data")

                        jam_tgl.text = obData.getString("tanggal")
                        jam_subuh.text = obData.getString("subuh")
                        jam_dzuhur.text = obData.getString("dzuhur")
                        jam_ashar.text = obData.getString("ashar")
                        jam_maghrib.text = obData.getString("maghrib")
                        jam_isya.text = obData.getString("isya")

                        Log.d("dataJadwal", obData.toString())

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            })
            task.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun loadKota() {
        try {
            var url = "https://api.banghasan.com/sholat/format/json/kota"
            val task = ClientAsyncTask(this, object : ClientAsyncTask.OnPostExecuteListener {
                override fun onPostExecute(result: String) {

                    Log.d("KotaData", result)
                    try {
                        val jsonObj = JSONObject(result)
                        val jsonArray = jsonObj.getJSONArray("kota")
                        var kota: Kota? = null
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            kota = Kota()
                            kota!!.id = obj.getInt("id")
                            kota!!.nama = obj.getString("nama")
                            listKota!!.add(kota)
                        }
                        mKotaAdapter!!.notifyDataSetChanged()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            })
            task.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
