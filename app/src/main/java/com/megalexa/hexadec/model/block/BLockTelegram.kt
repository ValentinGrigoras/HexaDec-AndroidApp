package com.megalexa.hexadec.model.block

class BLockTelegram  constructor(private var configuration:String): BlockInterface {
    private var profile= configuration

    override fun getConfig(): String {
        return profile
    }

    override fun getNameBlock(): String{
        return "TELEGRAM"
    }

    override fun setConfig(configuration:String){
        profile = configuration
    }
}