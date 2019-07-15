package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.BLockTelegram
import org.json.JSONObject

object BlockTelegramConnection : BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BLockTelegram {
        return BLockTelegram(jsonObject.getJSONObject("config").getString("TEXT"))
    }

    override fun <BLockTelegram> convertToJSON(t: BLockTelegram): JSONObject {
        val textBlock = t as com.megalexa.hexadec.model.block.BLockTelegram
        val allBlock = JSONObject()
        allBlock.put("profile", textBlock.getConfig())
        return allBlock
    }
    override val resource: String
        get() = "blocks/telegram"
}