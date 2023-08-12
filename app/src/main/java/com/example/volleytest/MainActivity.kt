package com.example.volleytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var server_url = "http://150.136.175.102:3000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tvGreeting)
        val volleyGetTest = findViewById<Button>(R.id.btnGetTest)
        volleyGetTest.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url  = server_url + "/api/json?name=ben&phone=1234567&password=123"
            val jsonOBJ = JSONObject()
            jsonOBJ.put("dd", 2)
            val jsonRequest =  JsonObjectRequest(Request.Method.GET, url, jsonOBJ, {
                    response ->
                run {
                    val jsonArray = response.getJSONArray("students")
                    for( i in 1..jsonArray.length())
                    {
                        val json = jsonArray.getJSONObject(i-1)
                        Log.i("MYTAG", json.getString("name"))
                    }
                    textView.text = response.toString()
                    queue.stop()
                }

            },{
                textView.text = "Wrong $it"
                queue.stop()

            })
            queue.add(jsonRequest)

        }
    }
}