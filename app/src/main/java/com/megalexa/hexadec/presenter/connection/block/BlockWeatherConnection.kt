package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BlockWeather
import org.json.JSONObject

object BlockWeatherConnection: BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BlockWeather {
        return BlockWeather(jsonObject.getJSONObject("config").getString("TEXT"))
    }

    override fun <BlockWeather> convertToJSON(t: BlockWeather): JSONObject {
        val textBlock = t as com.megalexa.hexadec.model.block.BlockWeather
        val allBlock = JSONObject()
        allBlock.put("city", textBlock.getConfig())
        return allBlock
    }
    override val resource: String
        get() = "blocks/weather"
}