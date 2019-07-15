package com.megalexa.hexadec.presenter

import android.content.Context

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.megalexa.hexadec.R
import com.megalexa.hexadec.presenter.contract.MainContract
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import java.util.ArrayList


class WorkflowPresenter(view: MainContract.WorkflowView): MainContract.WorkflowContract {
    private var view:MainContract.WorkflowView? = view
    private  var wList = HexaDec.workflowList

    override fun onDestroy() {
        this.view = null
    }

    override  fun updateWList() {
        Thread {
            //Thread.sleep(10)
            val workflows = ArrayList<Workflow>()
            val count =
                WorkflowConnection.getOperation(listOf(Pair("IDUser", HexaDec.user.getId()))).getJSONArray("Items")
                    .length()
            for (i in 0 until count) {
                workflows.add(
                    WorkflowConnection.convertFromJSON(
                        WorkflowConnection.getOperation(
                            listOf(
                                Pair(
                                    "IDUser",
                                    HexaDec.user.getId()
                                )
                            )
                        ).getJSONArray("Items").getJSONObject(i)
                    )
                )
            }
            HexaDec.hexadec(workflows)
            wList = HexaDec.workflowList
            //view?.updateView()
        }.start()
    }

    override fun popolateView(context: Context, view: RecyclerView){
            val recicleview = view.findViewById(R.id.recyclerViewConf) as RecyclerView
            val llm= LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recicleview.setHasFixedSize(true)
            recicleview.layoutManager =llm
            val myAdapter = recyclerViewWorkflowAdapter(wList)
            recicleview.apply {
                adapter = myAdapter
            }
    }
}