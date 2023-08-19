package com.example.volleytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var server_url = "http://150.136.175.102:3000"
    private lateinit var textView:TextView
    private lateinit var etName:EditText
    private lateinit var etEmail:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.tvGreeting)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)

        val volleyGetTest = findViewById<Button>(R.id.btnGetTest)
        volleyGetTest.setOnClickListener {
            getTest(it)
        }
        val volleyPostTest = findViewById<Button>(R.id.btnPostTest)

        volleyPostTest.setOnClickListener {
            postTest(it)
        }
    }

    private fun postTest(it: View?) {
        var url = "http://150.136.175.102:9090/students"

        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("Name", etName.text.toString());
        jsonObject.put("Email", etEmail.text.toString());

        val postRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener {
            val responseMsg: String = "response : " + it.toString()
            textView.text = responseMsg

        }, Response.ErrorListener {
            textView.text = it.toString()
            Toast.makeText(applicationContext,"Fail to get response..", Toast.LENGTH_SHORT).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String,String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }
        }
        postRequest.setRetryPolicy(object: RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }
            override fun retry(error: VolleyError){

            }
        })
        queue.add(postRequest)

    }

    fun getTest(view: View)
    {
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