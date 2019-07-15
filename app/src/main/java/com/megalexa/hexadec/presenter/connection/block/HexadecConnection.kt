package com.megalexa.hexadec.presenter

import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import org.json.JSONObject

object HexadecConnection: Connection() {

    override val resource: String
        get() = "all"


    override fun convertFromJSON(jsonObject: JSONObject): HexaDec {

        val jsonUser=JSONObject()
        val jsonWorkflow=JSONObject()

        jsonUser.put("userID",jsonObject.get("userID"))
        jsonUser.put("name",jsonObject.get("name"))
        jsonUser.put("email",jsonObject.get("email"))

        jsonWorkflow.put("workflowList","workflowList")

        val user= HexadecUserConnection.convertFromJSON(jsonUser)
        val workflows=ArrayList<Workflow>()

        val keys = jsonObject.getJSONObject("workflowList").keys()
        for (item in keys){
            val workflow = JSONObject()
            workflow.put("workflowName", item)
            workflow.put("workflow", jsonObject.getJSONObject("workflowList").getJSONArray(item))
            workflows.add(WorkflowConnection.convertFromJSON(workflow))
        }
        HexaDec.user= user
        HexaDec.workflowList=workflows
        return HexaDec.getInstance()
    }

    override fun <HexaDec> convertToJSON(t: HexaDec): JSONObject {
        return JSONObject()
    }
}