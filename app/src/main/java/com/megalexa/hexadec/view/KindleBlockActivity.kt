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
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.kindleview.*
import kotlinx.android.synthetic.main.textview.*
import kotlinx.android.synthetic.main.textview.sBlockbtnAnnulla
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvab
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvaw
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class KindleBlockActivity : AppCompatActivity(),MainContract.BlockView {

    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow :Workflow
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kindleview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@KindleBlockActivity))
        sBlockbtnSalvab.setOnClickListener {
            if(findViewById<EditText>(R.id.kindleBlockConf).text.toString().trim('.').endsWith("pdf") && findViewById<EditText>(R.id.kindleBlockConf).text.toString().trim(':').startsWith("https") ) {
                newWorkflow = presenter.addBlock(
                    newWorkflow,
                    findViewById<EditText>(R.id.kindleBlockConf).text.toString(),
                    "KINDLE"
                )
                val intent = Intent(this@KindleBlockActivity, AddBlocksActivity::class.java)
                intent.putExtra("Workflow", newWorkflow)
                intent.putExtra("configuration", "no")
                startActivity(intent)
            }else{
                startToast(getResources().getString(R.string.errKindle))
            }
        }
        sBlockbtnSalvaw.setOnClickListener {
            if(findViewById<EditText>(R.id.kindleBlockConf).text.toString().trim('.').endsWith("pdf") && findViewById<EditText>(R.id.kindleBlockConf).text.toString().trim(':').startsWith("https") ) {
                newWorkflow = presenter.addBlock(
                    newWorkflow,
                    findViewById<EditText>(R.id.kindleBlockConf).text.toString(),
                    "KINDLE"
                )
                //presenter.saveWorkflow(newWorkflow)
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
                    startActivity(Intent(this@KindleBlockActivity, ViewActivity::class.java))
                }.start()
            }else
            {
                startToast(getResources().getString(R.string.errKindle))
            }

        }
        sBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@KindleBlockActivity, NewWorkflowActivity::class.java)
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
            startActivity(Intent(this@KindleBlockActivity, KindleBlockActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setPresenter(presenter: MainContract.BlockContract) {
        this.presenter=presenter
    }

    override fun onStart() {
        super.onStart()
        var conf : String = intent.getStringExtra("configuration")

        setCustomLayout(conf)
        getIntents(conf)

        kindleBlockConf.setText(newValue)

        saveChangesKindle.setOnClickListener {
            val intent=Intent(this@KindleBlockActivity, ConfigurationWorkflow::class.java)
            newValue = kindleBlockConf.text.toString()
            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            presenter.modifyBlock(currentWorkflow,currentBlockPosition)
            //currentWorkflow.getBlockAt(currentBlockPosition).

            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@KindleBlockActivity,message, Toast.LENGTH_LONG).show()
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
            saveChangesKindle.visibility = View.VISIBLE
            saveChangesKindle.isClickable = true

            cancelChangesKindle.visibility = View.VISIBLE
            cancelChangesKindle.isClickable = true

            removeChangesKindle.visibility = View.VISIBLE
            removeChangesKindle.isClickable = true

            sBlockbtnSalvab.visibility = View.INVISIBLE
            sBlockbtnSalvab.isClickable = false

            sBlockbtnSalvaw.visibility = View.INVISIBLE
            sBlockbtnSalvaw.isClickable = false

            sBlockbtnAnnulla.visibility = View.INVISIBLE
            sBlockbtnAnnulla.isClickable = false
        }
        else{
            saveChangesKindle.visibility = View.INVISIBLE
            saveChangesKindle.isClickable = false

            cancelChangesKindle.visibility = View.INVISIBLE
            cancelChangesKindle.isClickable = false

            removeChangesKindle.visibility = View.INVISIBLE
            removeChangesKindle.isClickable = false

            sBlockbtnSalvab.visibility = View.VISIBLE
            sBlockbtnSalvab.isClickable = true

            sBlockbtnSalvaw.visibility = View.VISIBLE
            sBlockbtnSalvaw.isClickable = true

            sBlockbtnAnnulla.visibility = View.VISIBLE
            sBlockbtnAnnulla.isClickable = true

        }
    }

}