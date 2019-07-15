package com.megalexa.hexadec.model.block

import com.megalexa.hexadec.R
import com.megalexa.hexadec.utils.ApplicationContext

class BlockYouTube constructor(private val configuration:String): BlockInterface {
    private var url= configuration
    private var APIKey =""// ApplicationContext.sContext!!.resources!!.getString(R.string.YouTube_API)


    fun setApi(api:String){
        APIKey=api
    }

    override fun getConfig(): String {
        return url
    }

    override fun getNameBlock(): String{
        return "YOUTUBE"
    }

    override fun setConfig(configuration:String){
        url = configuration
    }
}