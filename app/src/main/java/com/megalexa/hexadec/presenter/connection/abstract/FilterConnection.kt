package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockFilter
import org.json.JSONObject

object FilterConnection: BlockConnection() {
    override fun convertFromJSON(jsonObject: JSONObject):BlockFilter {
        val limit = jsonObject.getJSONObject("config").getInt("filter").toString()
        return BlockFilter(limit)
    }

    override fun <BlockFilter> convertToJSON(t: BlockFilter): JSONObject {
        val filter = t as com.megalexa.hexadec.model.block.BlockFilter
        val allBlock = JSONObject()
        allBlock.put("blockType", "Filter" )
        allBlock.put("relatedBlock", "TEXT" )//example
        allBlock.put("value", filter.getConfig())
        return allBlock
    }

    override val resource: String
        get() = "blocks/filter"
}