package com.megalexa.hexadec.presenter

import android.util.Log
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.block.*
import com.megalexa.hexadec.presenter.contract.MainContract
import org.json.JSONObject
import java.util.ArrayList

class AddBlockPresenter (view: MainContract.AddBlockView): MainContract.AddBlockContract {
    private var view: MainContract.AddBlockView? = view

    override fun saveWorkflow(workflow: Workflow) {
        Thread {
            val json = JSONObject()
            json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", workflow.getWorkflowName()).put("WelcomeText",workflow.getWelcomeText()).put("SuggestedTime",workflow.getSuggestedTime())
            WorkflowConnection.postOperation(WorkflowConnection.convertToJSON(workflow))
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
                    "INSTAGRAM"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("URLValue", workflow.getBlocks()[w].getConfig())
                        BlockInstagramConnection.postOperation(json)
                    }
                    "FILTRO"->{
                        json.put("IDUser", HexaDec.user.getId())
                        json.put("WorkflowName", workflow.getWorkflowName())
                        json.put("TextValue", workflow.getBlocks()[w].getConfig())
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

    override fun onDestroy() {
        this.view = null
    }
}