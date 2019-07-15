package com.megalexa.hexadec.model.block

class BlockText constructor(private var configuration:String): BlockInterface {
    private var text= configuration

    override fun getConfig(): String {
        return text
    }

     override fun getNameBlock(): String{
        return "TEXT"
    }

    override fun setConfig(configuration:String){
        text = configuration
    }
}