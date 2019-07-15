package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockMail
import org.json.JSONObject

object BlockMailConnection : BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BlockMail {
        return BlockMail(jsonObject.getJSONObject("config").getString("MAIL"))
    }

    override fun <BlockMail> convertToJSON(t: BlockMail): JSONObject {
        val textBlock = t as com.megalexa.hexadec.model.block.BlockMail
        val allBlock = JSONObject()
        allBlock.put("URLValue", textBlock.getConfig())
        return allBlock
    }
    override val resource: String
        get() = "blocks/mail"
}