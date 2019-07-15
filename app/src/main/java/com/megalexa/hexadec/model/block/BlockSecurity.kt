package com.megalexa.hexadec.model.block

class BlockSecurity constructor(configuration:String): BlockInterface, Security {
    private val pin= configuration

    override fun getConfig(): String {
        return pin
    }

    override fun getNameBlock(): String{
        return "SECURITY"
    }

    override fun setConfig(configuration:String){
    }
}