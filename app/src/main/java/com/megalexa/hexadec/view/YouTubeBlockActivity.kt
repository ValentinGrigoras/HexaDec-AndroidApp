package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.textview.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class YouTubeBlockActivity : AppCompatActivity(),MainContract.BlockView {
    override fun getIntents(conf: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCustomLayout(conf: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow :Workflow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.youtubeview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@YouTubeBlockActivity))
        sBlockbtnSalvab.setOnClickListener {
            val intent=Intent(this@YouTubeBlockActivity, AddBlocksActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            intent.putExtra("configuration","no")
            startActivity(intent)
        }
        sBlockbtnSalvaw.setOnClickListener {
            //newWorkflow=presenter.addBlock(newWorkflow,findViewById<EditText>(R.id.textBlockConf).text.toString(),"YOUTUBE")
            Thread{
                val json = JSONObject()
                json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", newWorkflow.getWorkflowName()).put("WelcomeText",newWorkflow.getWelcomeText()).put("SuggestedTime",newWorkflow.getSuggestedTime())
                WorkflowConnection.postOperation(json)
                //correct
                presenter.saveBlock(newWorkflow)
                HexaDec.workflowList.add(newWorkflow)
                // reload hexadec
                val workflows= ArrayList<Workflow>()
                val count = WorkflowConnection.getOperation(listOf(Pair("IDUser", HexaDec.user.getId()))).getJSONArray("Items").length()
                for(i in 0 until count){
                    workflows.add(
                        WorkflowConnection.convertFromJSON(
                            WorkflowConnection.getOperation(listOf(Pair("IDUser",
                                HexaDec.user.getId()))).getJSONArray("Items").getJSONObject(i)))
                }
                HexaDec.hexadec(workflows)
                startActivity(Intent(this@YouTubeBlockActivity, ViewActivity::class.java))
            }.start()
        }
        sBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@YouTubeBlockActivity, NewWorkflowActivity::class.java)
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
            startActivity(Intent(this@YouTubeBlockActivity, YouTubeBlockActivity::class.java))
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
        newWorkflow = intent.getSerializableExtra("WorkFlow") as Workflow
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@YouTubeBlockActivity,message, Toast.LENGTH_LONG).show()
        }
    }
}