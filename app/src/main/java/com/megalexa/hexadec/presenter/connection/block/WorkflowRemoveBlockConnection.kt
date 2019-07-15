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

object WorkflowRemoveBlockConnection: Connection() {

    override val resource: String
        get() = "workflow/removeblocks"

    fun removeBlock(workflow: Workflow,position:Int):Boolean{
        Log.d("workflow size",workflow.getWorkflowSize().toString())
        Log.d("Position",position.toString())
        return if (workflow.getWorkflowSize()>position){
            val json= JSONObject()
            json.put("IDUser",HexaDec.user.getId()).put("WorkflowName",workflow.getWorkflowName()).put("Index",position)
            this.deleteOperation(json)
            //HexaDec.getWorkflowByName(workflow.getWorkflowName())?.removeBlockFromWorkflow(position)
            true
        }else{
            false
        }
    }

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

}
