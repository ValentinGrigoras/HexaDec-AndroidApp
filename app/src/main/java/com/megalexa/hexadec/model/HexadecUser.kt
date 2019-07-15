package com.megalexa.hexadec.model
import java.io.Serializable

class HexadecUser constructor(private val id: String ,private val name: String,private val email:String) : Serializable{
    private var pin = String()
    private  var apiInstagram:String = ""
    private  var apiGoogle:String = ""

    init{
        val n1 = (0..9).random()
        val n2 = (0..9).random()
        val n4 = (0..9).random()
        val n3 = (0..9).random()
        pin = n1.toString()+n2.toString()+n3.toString()+n4.toString()
    }

    fun getName():String{
        return name
    }
    fun getId():String{
        return id
    }
    fun getEmail():String{
        return email
    }

    fun setPin(security:String){
        pin = security
    }

    fun getPin():String{
        return pin
    }

    fun setApiInstagram(api:String){
        apiInstagram = api
    }

    fun setApiGoogle(api:String){
        apiGoogle=api
    }

    fun getApiInstagram():String{
        return apiInstagram
    }

    fun getApiGoogle():String{
        return apiGoogle
    }

}