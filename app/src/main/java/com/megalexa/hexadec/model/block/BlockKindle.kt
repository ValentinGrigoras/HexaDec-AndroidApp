package com.megalexa.hexadec.model.block

class BlockKindle constructor(private var configuration:String): BlockInterface {
    private var pdf= configuration

    override fun getConfig(): String {
        return pdf
    }

    override fun getNameBlock(): String{
        return "KINDLE"
    }

    override fun setConfig(configuration:String){
        pdf = configuration
    }
}