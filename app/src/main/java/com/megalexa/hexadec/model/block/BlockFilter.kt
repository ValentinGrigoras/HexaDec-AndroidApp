package com.megalexa.hexadec.model.block

class BlockFilter constructor(private var configuration:String): BlockInterface {
    private var limit= configuration


    override fun getConfig(): String {
        return limit
    }

    override fun getNameBlock(): String{
        return "FILTER"
    }

    override fun setConfig(configuration: String) {
        limit=configuration
    }
}