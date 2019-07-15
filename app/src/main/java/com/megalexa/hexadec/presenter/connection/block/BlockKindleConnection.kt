package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockKindle
import org.json.JSONObject

object BlockKindleConnection : BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BlockKindle {
        return BlockKindle(jsonObject.getJSONObject("config").getString("Kindle"))
    }

    override fun <BlockKindle> convertToJSON(t: BlockKindle): JSONObject {
        val kindleBlock = t as com.megalexa.hexadec.model.block.BlockKindle
        val allBlock = JSONObject()
        allBlock.put("URLValue", kindleBlock.getConfig())
        return allBlock
    }

    override val resource: String
        get() = "blocks/kindle"
}