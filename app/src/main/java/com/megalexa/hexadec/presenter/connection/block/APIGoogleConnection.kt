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

object APIGoogleConnection: Connection() {

    override val resource: String
        get() = "api-link/google"

    override fun convertFromJSON(jsonObject: JSONObject): BlockInterface {
        return Block("MAIL","Default").getBlock()
    }


    override fun <Workflow> convertToJSON(t: Workflow): JSONObject {
        val jsonObject= JSONObject()
        return jsonObject
    }

}
