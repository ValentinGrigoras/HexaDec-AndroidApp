package com.megalexa.hexadec.model

import com.megalexa.hexadec.model.block.BlockInterface
import kotlin.collections.ArrayList



class HexaDec private constructor(private var workflowList: ArrayList<Workflow>, private var user : HexadecUser){

    companion object {
        @Volatile private var instance: HexaDec?= null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: HexaDec(workflowList, user).also { instance = it }
        }
        var workflowList =ArrayList<Workflow>()
        var user= HexadecUser("initialId","initialName","initialEmail")
        fun hexadec(workflowList: ArrayList<Workflow>) = apply{ Companion.workflowList = workflowList }
        fun user(user: HexadecUser) = apply{ Companion.user =user}

        fun getWorkflowByName( name: String): Workflow?{
            for(wf in workflowList)
            {
                if(name==wf.getWorkflowName())
                    return wf
            }
            return null
        }
        fun removeWorkflow(position:Int):Boolean{
            return if(HexaDec.workflowList.size > position ){
                HexaDec.workflowList.removeAt(position)
                true
            }else{
                false
            }
        }
    }

    fun saveWorkflow(workflowName: String, blockList: ArrayList<BlockInterface>, create:String, modify:String, counter:Int){
        val workflow = Workflow(workflowName,create,modify,counter)
        workflow.setBlocks(blockList)
        workflowList.add(workflow)
    }


    fun addWorkflow(wl: Workflow){
        workflowList.add(wl)
    }

    fun getWorkflowList(): ArrayList<Workflow> {
        return workflowList
    }

    fun getUser() : HexadecUser {
        return user
    }

    fun setUser(user: HexadecUser){
        this.user = user
    }

    fun getBlock(user: HexadecUser, w: String) : ArrayList<BlockInterface>? {
        for(item in workflowList){
            if(item.getWorkflowName() == w){
                return item.getBlocks(user)
            }
        }
        return null
    }

    fun isWorkflowIn(wl: String) : Boolean{
        var isIn  = false
        for(workflow in workflowList){
            if(workflow.getWorkflowName() == wl){
                isIn = true
            }
        }
        return isIn
    }

    fun setInstance(param : HexaDec) {
        this.user = param.user
        this.workflowList= param.workflowList
    }



}