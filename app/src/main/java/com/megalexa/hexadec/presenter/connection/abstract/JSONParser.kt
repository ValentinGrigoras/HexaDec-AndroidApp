package com.megalexa.hexadec.presenter

import org.json.JSONObject

interface JSONParser {

    fun <T> convertToJSON(t:T): JSONObject

    open fun convertFromJSON(jsonObject: JSONObject):Any

}