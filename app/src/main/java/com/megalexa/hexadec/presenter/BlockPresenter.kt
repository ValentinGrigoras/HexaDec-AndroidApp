package com.megalexa.hexadec.presenter

import android.util.Log
import android.view.View
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.block.*
import com.megalexa.hexadec.presenter.contract.MainContract
import org.json.JSONObject
import java.util.ArrayList

 class BlockPresenter (view: MainContract.BlockView): MainContract.BlockContract {

     private var view: MainContract.BlockView? = view


    override fun saveWorkflow(workflow: Workflow) {
        Thread {
            val json = JSONObject()
            json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", workflow.getWorkflowName()).put("WelcomeText",workflow.getWelcomeText()).put("SuggestedTime",workflow.getSuggestedTime())
            WorkflowConnection.postOperation(json)
        }.start()
    }

    override fun saveBlock(workflow: Workflow) {
        Thread {
            Thread.sleep(20)
            val json = JSONObject()
            val blocks = workflow.getBlocks()
            for (w in 0 until blocks.size) {
                when(blocks[w].getNameBlock()){
                    "TEXT"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("TextValue", workflow.getBlocks()[w].getConfig())
                        BlockTextConnection.postOperation(json)
                    }
                    "FEEDRSS"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockFeedRssConnection.postOperation(json)
                    }
                    "WEATHER"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("CityValue", workflow.getBlocks()[w].getConfig())
                        BlockWeatherConnection.postOperation(json)
                    }
                    "TVPROGRAM"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("ProgrValue", workflow.getBlocks()[w].getConfig())
                        Log.d("son qui",json.toString())
                        BlockTVProgramConnection.postOperation(json)
                    }
                    "INSTAGRAM"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockInstagramConnection.postOperation(json)
                    }
                    "FILTER"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("FilterValue", workflow.getBlocks()[w].getConfig())
                        BlockFilterConnection.postOperation(json)
                    }
                    "SECURITY"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("PIN", workflow.getBlocks()[w].getConfig())
                        BlockSecurityConnection.postOperation(json)
                    }
                    "YOUTUBEMUSIC"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockYouTubeMusicConnection.postOperation(json)
                    }
                    "YOUTUBE"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockYouTubeConnection.postOperation(json)
                    }
                    "TELEGRAM"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("profile", workflow.getBlocks()[w].getConfig())
                        BlockTelegramConnection.postOperation(json)
                    }
                    "RADIO"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockRadioConnection.postOperation(json)
                    }
                    "KINDLE"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockKindleConnection.postOperation(json)
                    }
                    //facoltativi
                    "FACEBOOK"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("profile", workflow.getBlocks()[w].getConfig())
                        BlockFacebookConnection.postOperation(json)
                    }
                    "SLACK"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("profile", workflow.getBlocks()[w].getConfig())
                        BlockSlackConnection.postOperation(json)
                    }
                    "CALENDARIO"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("profile", workflow.getBlocks()[w].getConfig())
                        BlockTextConnection.postOperation(json)
                    }
                    "CINEMA"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("url", workflow.getBlocks()[w].getConfig())
                        BlockTextConnection.postOperation(json)
                    }
                    "TRASPORTI"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("TextValue", workflow.getBlocks()[w].getConfig())
                        BlockTextConnection.postOperation(json)
                    }
                    "MAIL"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("MailValue", workflow.getBlocks()[w].getConfig())
                        BlockMailConnection.postOperation(json)
                    }
                    "LISTA"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("TextValue", workflow.getBlocks()[w].getConfig())
                        BlockTextConnection.postOperation(json)
                    }
                }
            }
        }.start()
    }

    override fun addBlock(workflow: Workflow, configuration: String, type: String): Workflow {
        try {
            when (type) {
                "TEXT" -> {
                    workflow.addBlockToWorkflow(BlockText(configuration))
                }
                "FEEDRSS" -> {
                    workflow.addBlockToWorkflow(BlockFeedRss(configuration))
                }
                "INSTAGRAM" -> {
                    workflow.addBlockToWorkflow(BlockInstagram(configuration))
                }
                "FILTER" -> {
                    workflow.addBlockToWorkflow(BlockFilter(configuration))
                }
                "WEATHER" -> {
                    workflow.addBlockToWorkflow(BlockWeather(configuration))
                }
                "SECURITY" -> {
                    workflow.addBlockToWorkflow(BlockSecurity(configuration))
                }
                "YOUTUBEMUSIC" -> {
                    workflow.addBlockToWorkflow(BlockYouTubeMusic(configuration))
                }
                "YOUTUBE" -> {
                    workflow.addBlockToWorkflow(BlockYouTube(configuration))
                }
                "TELEGRAM" -> {
                    workflow.addBlockToWorkflow(BLockTelegram(configuration))
                }
                "RADIO" -> {
                    workflow.addBlockToWorkflow(BlockRadio(configuration))
                }
                "KINDLE" -> {
                    workflow.addBlockToWorkflow(BlockKindle(configuration))
                }
                "FACEBOOK" -> {
                    workflow.addBlockToWorkflow(BlockText(configuration))
                }
                "SLACK" -> {
                    workflow.addBlockToWorkflow(BlockText(configuration))
                }
                "CALENDARIO" -> {
                    workflow.addBlockToWorkflow(BlockText(configuration))
                }
                "CINEMA" -> {
                    workflow.addBlockToWorkflow(BlockText(configuration))
                }
                "TVPROGRAM" -> {
                    workflow.addBlockToWorkflow(BlockTVProgram(configuration))
                }
                "LISTA" -> {
                    workflow.addBlockToWorkflow(BlockText(configuration))
                }
                "MAIL" -> {
                    workflow.addBlockToWorkflow(BlockMail(configuration))
                }

            }
            return workflow
        }catch (e:Exception){
            return workflow
        }
    }
    override fun modifyBlock(workflow: Workflow, position: Int) {
        Thread {

            val json = JSONObject()
            val block = workflow.getBlockAt(position)

            json.put("IDUser", HexaDec.user.getId())

            json.put("WorkflowName", workflow.getWorkflowName())

            when(block.getNameBlock()) {
                "TEXT" -> {
                    json.put("TextValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockTextConnection.putOperation(json)

                }
                "FEEDRSS" -> {
                    json.put("URLValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockFeedRssConnection.putOperation(json)
                }
                "WEATHER" -> {
                    json.put("CityValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockWeatherConnection.putOperation(json)
                }
                "TVPROGRAM" -> {
                    json.put("ProgrValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockTVProgramConnection.putOperation(json)
                }
                "INSTAGRAM" -> {

                    json.put("URLValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockInstagramConnection.putOperation(json)
                }
                "FILTER" -> {
                    json.put("FilterValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockFilterConnection.putOperation(json)
                }
                "SECURITY" -> {
                    json.put("PIN", block.getConfig())

                    BlockSecurityConnection.putOperation(json)
                }
                "YOUTUBEMUSIC" -> {
                    json.put("URLValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockYouTubeMusicConnection.putOperation(json)
                }
                "YOUTUBE" -> {
                    json.put("URLValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockYouTubeConnection.putOperation(json)
                }
                "TELEGRAM" -> {
                    json.put("profile", block.getConfig())
                    json.put("Index", position.toString())
                    BlockTelegramConnection.putOperation(json)
                }
                "RADIO" -> {
                    json.put("URLValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockRadioConnection.putOperation(json)
                }
                "KINDLE" -> {
                    json.put("URLValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockKindleConnection.putOperation(json)
                }
                "MAIL" -> {
                    json.put("MailValue", block.getConfig())
                    json.put("Index", position.toString())
                    BlockMailConnection.putOperation(json)
                }
            }

        }.start()
    }
    private fun setUpWorkflow(){
        val workflows= ArrayList<Workflow>()
        val count = WorkflowConnection.getOperation(listOf(Pair("IDUser", HexaDec.user.getId()))).getJSONArray("Items").length()
        for(i in 0 until count){
            workflows.add(
                WorkflowConnection.convertFromJSON(WorkflowConnection.getOperation(listOf(Pair("IDUser",HexaDec.user.getId()))).getJSONArray("Items").getJSONObject(i)))
        }
        HexaDec.hexadec(workflows)
    }

    override fun onDestroy() {
        this.view = null
    }
}