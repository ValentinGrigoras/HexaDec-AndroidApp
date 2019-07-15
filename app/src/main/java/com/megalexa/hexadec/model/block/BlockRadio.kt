package com.megalexa.hexadec.model.block

class BlockRadio constructor(private var configuration:String): BlockInterface {
    private var radio= configuration

    override fun getConfig(): String {
        return radio
    }

    override fun getNameBlock(): String{
        return "RADIO"
    }

    override fun setConfig(configuration:String){
        radio = configuration
    }
}