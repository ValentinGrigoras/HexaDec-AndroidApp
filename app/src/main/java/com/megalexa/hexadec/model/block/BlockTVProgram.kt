package com.megalexa.hexadec.model.block

class BlockTVProgram constructor(configuration:String): BlockInterface {
    private var programValue= configuration

    override fun getConfig(): String {
        return programValue
    }

    override fun getNameBlock(): String{
        return "TVPROGRAM"
    }

    override fun setConfig(configuration:String){
        programValue=configuration
    }
}