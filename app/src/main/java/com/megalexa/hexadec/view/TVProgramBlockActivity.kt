package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.WorkflowRemoveBlockConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.tvprogramview.*
import kotlinx.android.synthetic.main.feedrssview.*
import kotlinx.android.synthetic.main.feedrssview.textBlockConf
import kotlinx.android.synthetic.main.feedrssview.titleInsTextConf
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class TVProgramBlockActivity : AppCompatActivity(),MainContract.BlockView, AdapterView.OnItemSelectedListener {


    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow : Workflow
    private var channelValue:String = ""
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0

    override fun setPresenter(presenter: MainContract.BlockContract) {
       this.presenter = presenter
    }
    var list_of_programs = arrayOf("Rai 1", "Rai 2", "Rai 3", "Rai 4", "Rai 5", "Rete 4", "CANALE 5", "ITALIA 1", "ITALIA 2", "LA 5", "MEDIASET EXTRA", "LA 7", "RAI MOVIE", "RAI PREMIUM", "RAI YOYO", "CIELO", "REALTIME", "LA7D")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tvprogramview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@TVProgramBlockActivity))
        tvSpinner!!.onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_programs)

        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set Adapter to Spinner
        tvSpinner!!.adapter = aa

        tvprogramBlockbtnSalvab.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,channelValue,"TVPROGRAM")
            val intent=Intent(this@TVProgramBlockActivity, AddBlocksActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            intent.putExtra("configuration", "no")
            startActivity(intent)
        }
        tvprogramBlockbtnSalvaw.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,channelValue,"TVPROGRAM")
            Thread{
                startToast(getResources().getString(R.string.loadingDb))
                val json = JSONObject()
                json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", newWorkflow.getWorkflowName()).put("WelcomeText",newWorkflow.getWelcomeText()).put("SuggestedTime",newWorkflow.getSuggestedTime())
                WorkflowConnection.postOperation(json)
                //correct
                presenter.saveBlock(newWorkflow)
                HexaDec.workflowList.add(newWorkflow)
                // reload hexadec
                val workflows= ArrayList<Workflow>()
                val count = WorkflowConnection.getOperation(listOf(Pair("IDUser",HexaDec.user.getId()))).getJSONArray("Items").length()
                for(i in 0 until count){
                    workflows.add(WorkflowConnection.convertFromJSON(WorkflowConnection.getOperation(listOf(Pair("IDUser",HexaDec.user.getId()))).getJSONArray("Items").getJSONObject(i)))
                }
                HexaDec.hexadec(workflows)
                startActivity(Intent(this@TVProgramBlockActivity, ViewActivity::class.java))
            }.start()
        }
        tvprogramBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@TVProgramBlockActivity, NewWorkflowActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }

    }
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
       startToast("Selected" + list_of_programs[position])
        channelValue = list_of_programs[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@TVProgramBlockActivity, TVProgramBlockActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onStart() {
        super.onStart()
        var conf : String = intent.getStringExtra("configuration")
        getIntents(conf)
        setCustomLayout(conf)
        selectSpinnerItemByValue(tvSpinner, newValue)


        saveChangesTV.setOnClickListener {
            if(channelValue != currentWorkflow.getBlockAt(currentBlockPosition).getConfig()) {
                val intent = Intent(this@TVProgramBlockActivity, ConfigurationWorkflow::class.java)
                newValue = tvSpinner.selectedItem.toString();
                currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
                presenter.modifyBlock(currentWorkflow, currentBlockPosition)
                //currentWorkflow.getBlockAt(currentBlockPosition).

                intent.putExtra("WorkflowCorrente", currentWorkflow)
                startActivity(intent)
            }else{
                startToast(getResources().getString(R.string.modifyError))}

        }
        removeChangesTv.setOnClickListener {
            val intent=Intent(this@TVProgramBlockActivity, ViewActivity::class.java)
            //newValue = textBlockConf.text.toString()
            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            Thread {
                WorkflowRemoveBlockConnection.removeBlock(currentWorkflow, currentBlockPosition)

                //currentWorkflow.getBlockAt(currentBlockPosition).
            }.start()
            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }

        //newWorkflow = intent.getSerializableExtra("WorkFlow") as Workflow
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@TVProgramBlockActivity,message, Toast.LENGTH_LONG).show()
        }
    }

    private fun selectSpinnerItemByValue(spinner: Spinner, value: String){
        val adapter  = spinner.adapter
        for (index in 0 until adapter.count)
            if(value==adapter.getItem(index)){
                spinner.setSelection(index)
            }
    }
    override fun setCustomLayout(conf:String){
        if(conf == "yes"){
            saveChangesTV.visibility = View.VISIBLE
            saveChangesTV.isClickable = true

            cancelChangesTV.visibility = View.VISIBLE
            cancelChangesTV.isClickable = true

            removeChangesTv.visibility = View.VISIBLE
            removeChangesTv.isClickable = true

            tvprogramBlockbtnSalvab.visibility = View.INVISIBLE
            tvprogramBlockbtnSalvab.isClickable = false

            tvprogramBlockbtnSalvaw.visibility = View.INVISIBLE
            tvprogramBlockbtnSalvaw.isClickable = false

            tvprogramBlockbtnAnnulla.visibility = View.INVISIBLE
            tvprogramBlockbtnAnnulla.isClickable = false
        }
        else{
            saveChangesTV.visibility = View.INVISIBLE
            saveChangesTV.isClickable = false

            cancelChangesTV.visibility = View.INVISIBLE
            cancelChangesTV.isClickable = false

            removeChangesTv.visibility = View.INVISIBLE
            removeChangesTv.isClickable = false

            tvprogramBlockbtnSalvab.visibility = View.VISIBLE
            tvprogramBlockbtnSalvab.isClickable = true

            tvprogramBlockbtnSalvaw.visibility = View.VISIBLE
            tvprogramBlockbtnSalvaw.isClickable = true

            tvprogramBlockbtnAnnulla.visibility = View.VISIBLE
            tvprogramBlockbtnAnnulla.isClickable = true

        }
    }
    override fun getIntents(conf: String){
        if(conf=="yes") {
            currentWorkflow = intent.getSerializableExtra("CurrentWorkflow") as Workflow
            newValue = intent.getStringExtra("newTextValue")
            currentBlockPosition = intent.getIntExtra("Position", -1)
        }
        else
            newWorkflow = intent.getSerializableExtra("WorkFlow") as Workflow
    }
}