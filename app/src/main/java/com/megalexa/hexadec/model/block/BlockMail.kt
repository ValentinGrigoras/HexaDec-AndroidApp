package com.megalexa.hexadec.model.block

import com.megalexa.hexadec.R
import com.megalexa.hexadec.utils.ApplicationContext

class BlockMail(private var configuration:String): BlockInterface {
    private var mailValue = configuration
    private var APIKey =""

    fun setApi(api:String){
        APIKey=api
    }

    override fun getConfig(): String {
        return mailValue
    }

    override fun getNameBlock(): String{
        return "MAIL"
    }
     override fun setConfig(configuration:String){
         mailValue=configuration
     }
}
