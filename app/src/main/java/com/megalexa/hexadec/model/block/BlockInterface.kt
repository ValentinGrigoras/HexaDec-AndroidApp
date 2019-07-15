package com.megalexa.hexadec.model.block

import java.io.Serializable

interface BlockInterface:Serializable {
    fun getConfig(): String
    fun getNameBlock():String
    fun setConfig(configuration:String)
}