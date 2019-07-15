package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.feedrssview.radio2
import kotlinx.android.synthetic.main.feedrssview.radio3
import kotlinx.android.synthetic.main.radioview.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class RadioBlockActivity : AppCompatActivity(),MainContract.BlockView {
    override fun getIntents(conf: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCustomLayout(conf: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow :Workflow
    private var url:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.radioview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@RadioBlockActivity))
        radioBlockbtnSalvab.setOnClickListener {
            val intent=Intent(this@RadioBlockActivity, AddBlocksActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }
        radioBlockbtnSalvaw.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,findViewById<EditText>(R.id.textBlockConf).text.toString(),"RADIO")
            //presenter.saveWorkflow(newWorkflow)
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
                val count = WorkflowConnection.getOperation(listOf(Pair("IDUser", HexaDec.user.getId()))).getJSONArray("Items").length()
                for(i in 0 until count){
                    workflows.add(
                        WorkflowConnection.convertFromJSON(
                            WorkflowConnection.getOperation(listOf(Pair("IDUser",
                                HexaDec.user.getId()))).getJSONArray("Items").getJSONObject(i)))
                }
                HexaDec.hexadec(workflows)
                startActivity(Intent(this@RadioBlockActivity, ViewActivity::class.java))
            }.start()
        }
        radioBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@RadioBlockActivity, NewWorkflowActivity::class.java)
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
            startActivity(Intent(this@RadioBlockActivity, RadioBlockActivity::class.java))
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
            Toast.makeText(this@RadioBlockActivity,message, Toast.LENGTH_LONG).show()
        }
    }

    fun onRadioButtonClicked(view: View) {
        when(view as RadioButton) {
            radio1 ->{
                url = "https://www.gazzetta.it/rss/serie-a.xml"
            }
            radio2 ->{
                url = "https://www.gazzetta.it/rss/serie-b.xml"
            }
            radio3 ->{
                url = "https://www.gazzetta.it/rss/motociclismo.xml"
            }
        }
    }
}