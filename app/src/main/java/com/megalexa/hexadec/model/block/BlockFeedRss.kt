package com.megalexa.hexadec.model.block

class BlockFeedRss constructor(configuration:String): BlockInterface,Filtrable {
    private var url= configuration

    override fun getConfig(): String {
        return url
    }

    override fun getNameBlock(): String{
        return "FEEDRSS"
    }

    override fun setConfig(configuration:String){
        url=configuration
    }
}