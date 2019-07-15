package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockText
import org.json.JSONObject

object BlockFilterConnection : BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BlockText {
        return BlockText(jsonObject.getJSONObject("config").getString("FILTER"))
    }

    override fun <BlockText> convertToJSON(t: BlockText): JSONObject {
        val textBlock = t as com.megalexa.hexadec.model.block.BlockText
        val allBlock = JSONObject()
        allBlock.put("FilterValue", textBlock.getConfig())
        return allBlock
    }
    override val resource: String
        get() = "blocks/filter"
}