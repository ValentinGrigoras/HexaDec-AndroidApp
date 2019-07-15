package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.block.Block
import com.megalexa.hexadec.model.block.BlockFeedRss
import com.megalexa.hexadec.model.block.BlockTVProgram
import org.json.JSONObject

object BlockTVProgramConnection : BlockConnection() {

    override fun convertFromJSON(jsonObject: JSONObject): BlockTVProgram {
        return BlockTVProgram(jsonObject.getJSONObject("config").getString("ProgramValue"))
    }

    override fun <BlockTVProgram> convertToJSON(t: BlockTVProgram): JSONObject {
        val blockTVProgram= t as com.megalexa.hexadec.model.block.BlockTVProgram
        val allBlock = JSONObject()
        allBlock.put("ProgramValue", blockTVProgram.getConfig())
        return allBlock
    }

    override val resource: String
        get() = "blocks/tvprogram"
}
