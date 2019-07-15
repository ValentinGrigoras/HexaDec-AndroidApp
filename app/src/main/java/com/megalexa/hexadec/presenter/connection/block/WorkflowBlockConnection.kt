package com.megalexa.hexadec.presenter

import android.util.Log
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.block.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

object WorkflowBlockConnection: Connection() {

    override val resource: String
        get() = "workflow/getblocks"

    override fun convertFromJSON(jsonObject: JSONObject): BlockInterface {
        Log.d("jsonLoad",jsonObject.toString())
        var name = jsonObject.getString("BlockName").toString()
        var conf :String = "default"
        when(name.toUpperCase()) {
            "SECURITY"-> conf = jsonObject.getString("PIN").toString()
            "TEXT"-> conf = jsonObject.getString("TextValue").toString()
            "FEEDRSS"-> conf = jsonObject.getString("URLValue").toString()
            "INSTAGRAM"-> conf = jsonObject.getString("URLValue").toString()
            "KINDLE"-> conf = jsonObject.getString("URLValue").toString()
            "RADIO"-> conf = jsonObject.getString("RadioValue").toString()
            "TELEGRAM"-> conf = jsonObject.getString("NumberValue").toString()
            "YOUTUBE"-> conf = jsonObject.getString("URLValue").toString()
            "YOUTUBEMUSIC"-> conf = jsonObject.getString("URLValue").toString()
            "WHEATER"-> conf = jsonObject.getString("CityValue").toString()
            "FILTER"-> conf = jsonObject.getString("FilterValue").toString()
            "MAIL"-> conf = jsonObject.getString("MailValue").toString()
            "PROGRAMMAZIONETV"-> conf = jsonObject.getString("ProgrValue").toString()
            "MAIL"-> conf = jsonObject.getString("MailValue").toString()
        }
        val w =Block(name,conf)
        return w.getBlock()
    }


    override fun <Workflow> convertToJSON(t: Workflow): JSONObject {
        val workflow= t as com.megalexa.hexadec.model.Workflow
        val jsonObject= JSONObject()
        jsonObject.put("IDUser", HexaDec.user.getId()).put("WorkflowName", workflow.getWorkflowName()).put("WelcomeText",workflow.getWelcomeText())
        Log.d("jsonSave",jsonObject.toString())
        return jsonObject
    }

    override fun getOperation(params:List<Pair<String,String>>):JSONObject {
        var json= JSONArray()
        val query = StringBuilder()
        for (item in params) {
            query.append(URLEncoder.encode(item.first,"UTF-8")+"="+ URLEncoder.encode(item.second,"UTF-8")+ "&")
        }
        val string=query.substring(0,query.length-1)
        val url= "${getURL()}$resource"
        val myURL = URL("$url?$string")
        with(myURL.openConnection() as HttpsURLConnection) {
            setRequestProperty("Content-Type", "application/json")
            requestMethod = "GET"
            BufferedReader(InputStreamReader(inputStream)).use {
                val result = StringBuffer()
                var iLine = it.readLine()
                while (iLine != null) {
                    result.append(iLine)
                    iLine = it.readLine()
                }
                return JSONObject(result.toString())
            }
        }
    }
}
