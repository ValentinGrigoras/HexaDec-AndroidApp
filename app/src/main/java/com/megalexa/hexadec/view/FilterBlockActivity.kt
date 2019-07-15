package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.block.Filtrable
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.WorkflowRemoveBlockConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import com.megalexa.hexadec.view.AddBlocksActivity
import kotlinx.android.synthetic.main.filterview.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList


class FilterBlockActivity : AppCompatActivity(),MainContract.BlockView {

    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow : Workflow
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0


    override fun setPresenter(presenter: MainContract.BlockContract) {
       this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filterview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@FilterBlockActivity))

        filterBlockbtnSalvab.setOnClickListener {
            if(findViewById<EditText>(R.id.filterBlockConf).text.toString().matches("-?\\d+(\\.\\d+)?".toRegex())) {
                newWorkflow = presenter.addBlock(
                    newWorkflow,
                    findViewById<EditText>(R.id.filterBlockConf).text.toString(),
                    "FILTER"
                )
                val intent = Intent(this@FilterBlockActivity, AddBlocksActivity::class.java)
                intent.putExtra("Workflow", newWorkflow)
                intent.putExtra("configuration", "no")
                startActivity(intent)
            }
            else{
                startToast(getResources().getString(R.string.numFiltro))
            }
        }

        filterBlockbtnSalvaw.setOnClickListener {
            if(findViewById<EditText>(R.id.filterBlockConf).text.toString().matches("-?\\d+(\\.\\d+)?".toRegex())) {
                newWorkflow = presenter.addBlock(
                    newWorkflow,
                    findViewById<EditText>(R.id.filterBlockConf).text.toString(),
                    "FILTER"
                )
                Thread {
                    startToast(getResources().getString(R.string.loadingDb))
                    val json = JSONObject()
                    json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", newWorkflow.getWorkflowName())
                        .put("WelcomeText", newWorkflow.getWelcomeText())
                        .put("SuggestedTime", newWorkflow.getSuggestedTime())
                    WorkflowConnection.postOperation(json)
                    //correct
                    presenter.saveBlock(newWorkflow)
                    HexaDec.workflowList.add(newWorkflow)
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
                    startActivity(Intent(this@FilterBlockActivity, ViewActivity::class.java))
                }.start()
            }else{
                startToast(getResources().getString(R.string.numFiltro))
            }
        }

        filterBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@FilterBlockActivity, NewWorkflowActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }
        saveChangesFilter.setOnClickListener {
            val intent=Intent(this@FilterBlockActivity, ConfigurationWorkflow::class.java)
            newValue = filterBlockConf.text.toString()
            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)

            presenter.modifyBlock(currentWorkflow,currentBlockPosition)

            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }
        removeChangesFilter.setOnClickListener {
            val intent=Intent(this@FilterBlockActivity, ViewActivity::class.java)
            //newValue = textBlockConf.text.toString()
            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            Thread {
                WorkflowRemoveBlockConnection.removeBlock(currentWorkflow, currentBlockPosition)

                //currentWorkflow.getBlockAt(currentBlockPosition).
            }.start()
            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }
        cancelChangesFilter.setOnClickListener {
            val intent=Intent(this@FilterBlockActivity, ConfigurationWorkflow::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@FilterBlockActivity, FilterBlockActivity::class.java))
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
        if(intent.hasExtra("configuration")){
            var conf : String = intent.getStringExtra("configuration")

            getIntents(conf)
            setCustomLayout(conf)
            filterBlockConf.setText(newValue)
        }
//        newWorkflow = intent.getSerializableExtra("WorkFlow") as Workflow
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@FilterBlockActivity,message, Toast.LENGTH_LONG).show()
        }
    }

    override fun getIntents(conf: String) {
        if(conf=="yes") {
            currentWorkflow = intent.getSerializableExtra("CurrentWorkflow") as Workflow
            newValue = intent.getStringExtra("newTextValue")
            currentBlockPosition = intent.getIntExtra("Position", -1)
        }
        else
            newWorkflow = intent.getSerializableExtra("WorkFlow") as Workflow
    }

    override fun setCustomLayout(conf: String) {
        if(conf == "yes"){
            saveChangesFilter.visibility = View.VISIBLE
            saveChangesFilter.isClickable = true

            cancelChangesFilter.visibility = View.VISIBLE
            cancelChangesFilter.isClickable = true

            removeChangesFilter.visibility = View.VISIBLE
            removeChangesFilter.isClickable = true

            filterBlockbtnSalvab.visibility = View.INVISIBLE
            filterBlockbtnSalvab.isClickable = false

            filterBlockbtnSalvaw.visibility = View.INVISIBLE
            filterBlockbtnSalvaw.isClickable = false

            filterBlockbtnAnnulla.visibility = View.INVISIBLE
            filterBlockbtnAnnulla.isClickable = false
        }
        else{
            saveChangesFilter.visibility = View.INVISIBLE
            saveChangesFilter.isClickable = false

            cancelChangesFilter.visibility = View.INVISIBLE
            cancelChangesFilter.isClickable = false

            removeChangesFilter.visibility = View.INVISIBLE
            removeChangesFilter.isClickable = false

            filterBlockbtnSalvab.visibility = View.VISIBLE
            filterBlockbtnSalvab.isClickable = true

            filterBlockbtnSalvaw.visibility = View.VISIBLE
            filterBlockbtnSalvaw.isClickable = true

            filterBlockbtnAnnulla.visibility = View.VISIBLE
            filterBlockbtnAnnulla.isClickable = true
        }
    }

}