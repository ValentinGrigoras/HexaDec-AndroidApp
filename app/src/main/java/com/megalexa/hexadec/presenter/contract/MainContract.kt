/*
 Nome file: MainContract.kt
 Type: Interface
 Data creazione: 06/05/2019

 Descrizione del file:
 Autore: Andrea Chinello
 Versione: v1.0.0
 Registro modifiche:
 Andrea, 01/06/2019, Definizione SettingContract e BlockContract
 Valentin, 20/05/2019, Definizione WorkflowContract
 Andrea, 08/05/2019, Definizione LoginContract
*/

package com.megalexa.hexadec.presenter.contract

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.megalexa.hexadec.model.Workflow

interface MainContract {

    interface LoginContract:BasePresenter{
        fun updateUser(userId:String,userName:String,userEmail:String)
        fun setUpWorkflow()
    }
    interface  LoginView:BaseView<LoginContract>{
    }

    interface WorkflowContract:BasePresenter{
        fun popolateView(context: Context, view: RecyclerView)
        fun updateWList()
    }
    interface  WorkflowView:BaseView<WorkflowContract>{
        fun updateView()
    }

    interface AddBlockContract:BasePresenter{
        fun saveWorkflow(workflow: Workflow)
        fun saveBlock(workflow: Workflow)
    }
    interface  AddBlockView:BaseView<AddBlockContract>{
    }

    interface SettingContract:BasePresenter{
        fun updatePin(newPin:String)
    }
    interface  SettingView:BaseView<SettingContract>{
    }

    interface BlockContract:BasePresenter {
        fun saveWorkflow(workflow: Workflow)
        fun addBlock(workflow: Workflow, configuration:String,type:String):Workflow
        fun saveBlock(workflow: Workflow)
        fun modifyBlock(workflow: Workflow, position: Int)

    }

    interface  BlockView:BaseView<BlockContract> {
        fun setCustomLayout(conf: String)
        fun getIntents(conf: String)
    }

    interface ConfigurationContract:BasePresenter {
    fun getType(conf: String): String

    }

    interface  ConfigurationView:BaseView<ConfigurationContract> {

    }
}