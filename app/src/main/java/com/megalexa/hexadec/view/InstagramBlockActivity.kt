package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.APIInstagramConnection
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.WorkflowRemoveBlockConnection
import com.megalexa.hexadec.presenter.adapter.AuthenticationDialog
import com.megalexa.hexadec.presenter.adapter.AuthenticationListener
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.instagramview.*
import kotlinx.android.synthetic.main.textview.*
import kotlinx.android.synthetic.main.textview.sBlockbtnAnnulla
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvab
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvaw
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class InstagramBlockActivity : AppCompatActivity(),MainContract.BlockView,AuthenticationListener {

    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow :Workflow
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.instagramview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@InstagramBlockActivity))
        setListener()
        val connectButton= findViewById<Button>(R.id.btn_Instagram_login)
        if( HexaDec.user.getApiInstagram() != ""){
            connectButton.visibility = View.GONE
        }else{
            connectButton.visibility = View.VISIBLE
        }
    }

    override fun onTokenReceived(auth_token: String?) {
        if (auth_token == null)
            return
        HexaDec.user.setApiInstagram(auth_token)
        Thread{
            val json = JSONObject()
            json.put("IDUser",HexaDec.user.getId())
            json.put("APIInstagram",HexaDec.user.getApiInstagram())
            APIInstagramConnection.postOperation(json)
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@InstagramBlockActivity, InstagramBlockActivity::class.java))
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
        if(intent.hasExtra("configuration")){
            var conf : String = intent.getStringExtra("configuration")
            getIntents(conf)
            setCustomLayout(conf)
        }
        saveChangesInstagram.setOnClickListener {
            val intent=Intent(this@InstagramBlockActivity, ConfigurationWorkflow::class.java)
            newValue = "default"

            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            presenter.modifyBlock(currentWorkflow,currentBlockPosition)
            //currentWorkflow.getBlockAt(currentBlockPosition).

            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@InstagramBlockActivity,message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListener(){
        sBlockbtnSalvab.setOnClickListener {
            if (HexaDec.user.getApiInstagram() != "") {
                newWorkflow=presenter.addBlock( newWorkflow, "BloccoInstagram","INSTAGRAM")
                val intent = Intent(this@InstagramBlockActivity, AddBlocksActivity::class.java)
                intent.putExtra("Workflow", newWorkflow)
                intent.putExtra("configuration", "no")
                startActivity(intent)
            }
            else{
                startToast(getResources().getString(R.string.erroreIg))
            }
        }

        sBlockbtnSalvaw.setOnClickListener {
            if (HexaDec.user.getApiInstagram() != "") {
                newWorkflow = presenter.addBlock(
                    newWorkflow, "BloccoInstagram",
                    "INSTAGRAM"
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
                    startActivity(Intent(this@InstagramBlockActivity, ViewActivity::class.java))
                }.start()
            }else{
                startToast(getResources().getString(R.string.erroreIg))
            }
        }
        sBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@InstagramBlockActivity, NewWorkflowActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }
        btn_Instagram_login.setOnClickListener{
            val authenticationDialog = AuthenticationDialog(this, this)
            authenticationDialog.setCancelable(true)
            authenticationDialog.show()
        }
        removeChangesInstagram.setOnClickListener {
            val intent=Intent(this@InstagramBlockActivity, ViewActivity::class.java)
            //newValue = textBlockConf.text.toString()
            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            Thread {
                WorkflowRemoveBlockConnection.removeBlock(currentWorkflow, currentBlockPosition)

                //currentWorkflow.getBlockAt(currentBlockPosition).
            }.start()
            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
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
            saveChangesInstagram.visibility = View.VISIBLE
            saveChangesInstagram.isClickable = true

            cancelChangesInstagram.visibility = View.VISIBLE
            cancelChangesInstagram.isClickable = true

            removeChangesInstagram.visibility = View.VISIBLE
            removeChangesInstagram.isClickable = true

            sBlockbtnSalvab.visibility = View.INVISIBLE
            sBlockbtnSalvab.isClickable = false

            sBlockbtnSalvaw.visibility = View.INVISIBLE
            sBlockbtnSalvaw.isClickable = false

            sBlockbtnAnnulla.visibility = View.INVISIBLE
            sBlockbtnAnnulla.isClickable = false
        }
        else{
            saveChangesInstagram.visibility = View.INVISIBLE
            saveChangesInstagram.isClickable = false

            cancelChangesInstagram.visibility = View.INVISIBLE
            cancelChangesInstagram.isClickable = false

            removeChangesInstagram.visibility = View.INVISIBLE
            removeChangesInstagram.isClickable = false

            sBlockbtnSalvab.visibility = View.VISIBLE
            sBlockbtnSalvab.isClickable = true

            sBlockbtnSalvaw.visibility = View.VISIBLE
            sBlockbtnSalvaw.isClickable = true

            sBlockbtnAnnulla.visibility = View.VISIBLE
            sBlockbtnAnnulla.isClickable = true
        }
    }
}