package com.megalexa.hexadec.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.WorkflowConnection
import kotlinx.android.synthetic.main.activity_new_workflow.*

import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class NewWorkflowActivity: AppCompatActivity(){
    private lateinit var newWorkflow: Workflow
    private var suggestedTime ="morning"

    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""

    var conf= ""
    private  var currentBlockPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_workflow)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra("configuration")) {
             conf = intent.getStringExtra("configuration")
        }
        getIntents(conf)
        setData(conf)

        if(intent.hasExtra("Workflow")){
            newWorkflow=intent.getSerializableExtra("Workflow") as Workflow
            findViewById<EditText>(R.id.WorkflowName).setText(newWorkflow.getWorkflowName())
            findViewById<EditText>(R.id.WorkflowWelcomeText).setText(newWorkflow.getWelcomeText())
        }


         AddConfigurationBlock.setOnClickListener {
             if(findViewById<EditText>(R.id.WorkflowName).text.toString().isNotEmpty()) {
                 newWorkflow = Workflow(findViewById<EditText>(R.id.WorkflowName).text.toString(), "", "", 0)
                 newWorkflow.setWelcomeText(findViewById<EditText>(R.id.WorkflowWelcomeText).text.toString())
                 newWorkflow.setSuggestedTime(suggestedTime)
                 val intent = Intent(this@NewWorkflowActivity, AddBlocksActivity::class.java)
                 intent.putExtra("Workflow", newWorkflow)
                 intent.putExtra("configuration", "no")
                 startActivity(intent)
             }else{
                 startToast(getResources().getString(R.string.workflowName))
             }
        }
        AddWorkflowCancelButton.setOnClickListener {
            val intent=Intent(this@NewWorkflowActivity, ViewActivity::class.java)
            startActivity(intent)
        }

        ModifyWorkflowCancelButton.setOnClickListener {
            val intent=Intent(this@NewWorkflowActivity, ViewActivity::class.java)

            startActivity(intent)
        }
        if(intent.hasExtra("configuration")) {
            var conf: String = intent.getStringExtra("configuration")
            if (conf == "yes") {
                saveModifyWorkflow.visibility = View.VISIBLE
                saveModifyWorkflow.isClickable = true

                ModifyWorkflowCancelButton.visibility = View.VISIBLE
                ModifyWorkflowCancelButton.isClickable = true

                AddWorkflowCancelButton.visibility = View.INVISIBLE
                AddWorkflowCancelButton.isClickable = false

                AddConfigurationBlock.visibility = View.INVISIBLE
                AddConfigurationBlock.isClickable = false


            } else if (conf == "no") {
                saveModifyWorkflow.visibility = View.INVISIBLE
                saveModifyWorkflow.isClickable = false

                ModifyWorkflowCancelButton.visibility = View.INVISIBLE
                ModifyWorkflowCancelButton.isClickable = false

                AddWorkflowCancelButton.visibility = View.VISIBLE
                AddWorkflowCancelButton.isClickable = true

                AddConfigurationBlock.visibility = View.VISIBLE
                AddConfigurationBlock.isClickable = true
                }

        }

        InfoDay.tooltipText = resources.getString(R.string.infoDay)

        saveModifyWorkflow.setOnClickListener {

            if (suggestedTime != currentWorkflow.getSuggestedTime() || WorkflowWelcomeText.text.toString() != newValue) {
                Thread {
                    val json = JSONObject()
                    json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", currentWorkflow.getWorkflowName())
                        .put("WelcomeText", WorkflowWelcomeText.text.toString())
                        .put("SuggestedTime", suggestedTime)
                    WorkflowConnection.putOperation(json)

                    // reload hexadec
                    val workflows = ArrayList<Workflow>()
                    val count = WorkflowConnection.getOperation(listOf(Pair("IDUser", HexaDec.user.getId())))
                        .getJSONArray("Items").length()
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
                    startActivity(Intent(this@NewWorkflowActivity, ViewActivity::class.java))
                }.start()
            }
            else{
                startToast(getResources().getString(R.string.modifyError))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@NewWorkflowActivity, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onRadioButtonClicked(view: View) = when(view as RadioButton) {
        morning -> {
            suggestedTime = "morning"

        }
        noon ->{
            suggestedTime = "noon"

        }
        afternoon ->{
            suggestedTime = "afternoon"

        }
        evening ->{
            suggestedTime = "evening"

        }
        night ->{
            suggestedTime = "night"

        }
        else ->{
            suggestedTime= "morning"

        }
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@NewWorkflowActivity,message, Toast.LENGTH_LONG).show()
        }
    }

    fun getIntents(conf: String) {

        if (intent.hasExtra("configuration")) {
            if (conf == "yes") {
                currentWorkflow = intent.getSerializableExtra("CurrentWorkflow") as Workflow
                newValue = currentWorkflow.getWelcomeText()
                currentBlockPosition = intent.getIntExtra("Position", -1)
            }

        }

    }

    private  fun setData(conf: String){
        if(conf == "yes"){
            WorkflowName.setText(currentWorkflow.getWorkflowName())
            WorkflowName.isFocusable = false
            WorkflowName.setTextColor(Color.GRAY)
            WorkflowWelcomeText.setText(currentWorkflow.getWelcomeText())

            when(currentWorkflow.getSuggestedTime()){
                "morning" -> morning.isChecked = true
                "noon" -> noon.isChecked = true
                "afternoon" -> afternoon.isChecked = true
                "evening" -> evening.isChecked = true
                "night" -> night.isChecked = true
            }

        }
    }
}