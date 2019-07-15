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

object WorkflowConnection: Connection() {

    override val resource: String
        get() = "workflow"

    override fun convertFromJSON(jsonObject: JSONObject): Workflow {
        Log.d("jsonLoad",jsonObject.toString())
        val w= Workflow(jsonObject.getString("WorkflowName").toString(),jsonObject.getString("CreationDate").toString(),jsonObject.getString("ModifyDate").toString(),Integer.parseInt(jsonObject.getString("Iteraction").toString()))
        val suggTime = jsonObject.getString("SuggestedTime").toString()
        val welcomeT = jsonObject.getString("WelcomeText").toString()
        w.setSuggestedTime(suggTime)
        w.setWelcomeText(welcomeT)
        return w
    }

    override fun <Workflow> convertToJSON(t: Workflow): JSONObject {
        val workflow= t as com.megalexa.hexadec.model.Workflow
        val json=JSONObject()
        json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", workflow.getWorkflowName()).put("WelcomeText",workflow.getWelcomeText()).put("SuggestedTime",workflow.getSuggestedTime())
        Log.d("jsonSave",json.toString())
        return json
    }

    private fun convertBlock(block: BlockInterface):JSONObject{
        var result=JSONObject()
        when(block) {
            is BlockText -> result= BlockTextConnection.convertToJSON(block)
            is BlockFeedRss -> result= BlockFeedRssConnection.convertToJSON(block)
            is BlockWeather -> result= BlockWeatherConnection.convertToJSON(block)
          }
        return result
    }

    override fun getOperation(params:List<Pair<String,String>>):JSONObject {
        var json= JSONArray()
        val query = StringBuilder()
        for (item in params) {
            query.append(URLEncoder.encode(item.first,"UTF-8")+"="+ URLEncoder.encode(item.second,"UTF-8")+ "&")
        }
        val string=query.substring(0,query.length-1)
        val url= "${getURL()}$resource/"
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
