package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.HexadecUser
import org.json.JSONObject



object HexadecUserConnection: Connection() {

    override fun convertFromJSON(jsonObject: JSONObject): HexadecUser {
        return  HexadecUser(jsonObject.get("userID").toString(),jsonObject.get("name").toString(),jsonObject.get("email").toString())
    }

    override fun <User> convertToJSON(t: User): JSONObject {
        val userJ = JSONObject()
        val user= t as com.megalexa.hexadec.model.HexadecUser
        userJ.put("IDUser", user.getId())
        userJ.put("Username", user.getName())
        userJ.put("Mail", user.getEmail())
        userJ.put("PIN",user.getPin())
        return userJ
    }
    override val resource: String
        get() = "user"
}