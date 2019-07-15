package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockRadio
import org.json.JSONObject

object BlockRadioConnection : BlockConnection(){
    override fun convertFromJSON(jsonObject: JSONObject): BlockRadio {
        return BlockRadio(jsonObject.getJSONObject("config").getString("TEXT"))
    }
    override fun <BlockRadio> convertToJSON(t: BlockRadio): JSONObject {
        val textBlock = t as com.megalexa.hexadec.model.block.BlockRadio
        val allBlock = JSONObject()
        allBlock.put("TextValue", textBlock.getConfig())
        return allBlock
    }
    override val resource: String
        get() = "blocks/radio"
}