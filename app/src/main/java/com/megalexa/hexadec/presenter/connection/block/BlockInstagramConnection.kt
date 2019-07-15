package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockInstagram
import org.json.JSONObject

object BlockInstagramConnection : BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BlockInstagram {
        return BlockInstagram(jsonObject.getJSONObject("config").getString("INSTAGRAM"))
    }

    override fun <BlockInstagram> convertToJSON(t: BlockInstagram): JSONObject {
        val textBlock = t as com.megalexa.hexadec.model.block.BlockInstagram
        val allBlock = JSONObject()
        allBlock.put("URLValue", textBlock.getConfig())
        return allBlock
    }
    override val resource: String
        get() = "blocks/instagram"
}