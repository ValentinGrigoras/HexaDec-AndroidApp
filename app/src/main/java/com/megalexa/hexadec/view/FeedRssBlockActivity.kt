package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.WorkflowRemoveBlockConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.feedrss_configuration_item.*
import kotlinx.android.synthetic.main.feedrssview.*
import kotlinx.android.synthetic.main.feedrssview.textBlockConf
import kotlinx.android.synthetic.main.feedrssview.titleInsTextConf
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.ArrayList

class FeedRssBlockActivity : AppCompatActivity(),MainContract.BlockView {


    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow : Workflow
    private var url:String = ""
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0

    override fun setPresenter(presenter: MainContract.BlockContract) {
       this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedrssview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@FeedRssBlockActivity))

        feedrssBlockbtnSalvab.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,url,"FEEDRSS")
            val intent=Intent(this@FeedRssBlockActivity, AddBlocksActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            intent.putExtra("configuration", "no")
            startActivity(intent)
        }

        feedrssBlockbtnSalvaw.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,url,"FEEDRSS")
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
                val count = WorkflowConnection.getOperation(listOf(Pair("IDUser",HexaDec.user.getId()))).getJSONArray("Items").length()
                for(i in 0 until count){
                    workflows.add(WorkflowConnection.convertFromJSON(WorkflowConnection.getOperation(listOf(Pair("IDUser",HexaDec.user.getId()))).getJSONArray("Items").getJSONObject(i)))
                }
                HexaDec.hexadec(workflows)
                startActivity(Intent(this@FeedRssBlockActivity, ViewActivity::class.java))
            }.start()
        }

        feedrssBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@FeedRssBlockActivity, NewWorkflowActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }

        cancelChangesFeed.setOnClickListener {
            val intent=Intent(this@FeedRssBlockActivity, ConfigurationWorkflow::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }

        removeChangesFeed.setOnClickListener {
            val intent=Intent(this@FeedRssBlockActivity, ViewActivity::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@FeedRssBlockActivity, FeedRssBlockActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun onRadioButtonClicked(view: View) = when(view as RadioButton) {
        FeedCustom -> {
            titleInsTextConf.visibility = View.VISIBLE
            textBlockConf.visibility = View.VISIBLE
            url = textBlockConf.text.toString()
        }
        radio2 ->{
            val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
            url = if(currentLocale.toString() == "en"){
                "https://www.eyefootball.com/football_news.xml"
            } else{
                "https://www.gazzetta.it/rss/serie-b.xml"
            }
        }
        radio3 ->{
            url = "https://www.gazzetta.it/rss/motociclismo.xml"
        }
        FeedFormula ->{
            url = "https://www.gazzetta.it/rss/formula-1.xml"
        }
        FeedBasket ->{
            url = "https://www.gazzetta.it/rss/basket.xml"
        }
        else ->{
            titleInsTextConf.visibility = View.GONE
            textBlockConf.visibility = View.GONE
            url = "https://www.gazzetta.it/rss/serie-a.xml"
        }
    }

    override fun onStart() {
        super.onStart()
        var conf : String = intent.getStringExtra("configuration")

        getIntents(conf)
        setCustomLayout(conf)


        saveChangesFeed.setOnClickListener {
            val intent=Intent(this@FeedRssBlockActivity, ConfigurationWorkflow::class.java)
            newValue = url

            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            presenter.modifyBlock(currentWorkflow,currentBlockPosition)
            //currentWorkflow.getBlockAt(currentBlockPosition).

            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@FeedRssBlockActivity,message, Toast.LENGTH_LONG).show()
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
    override fun setCustomLayout(conf: String) {
        if (conf == "yes") {
            feedrssBlockbtnSalvab.visibility = View.INVISIBLE
            feedrssBlockbtnSalvab.isClickable = true

            feedrssBlockbtnSalvaw.visibility = View.INVISIBLE
            feedrssBlockbtnSalvaw.isClickable = true

            feedrssBlockbtnAnnulla.visibility = View.INVISIBLE
            feedrssBlockbtnAnnulla.isClickable = false


            saveChangesFeed.visibility = View.VISIBLE
            saveChangesFeed.isClickable = true

            cancelChangesFeed.visibility = View.VISIBLE
            cancelChangesFeed.isClickable = true

            removeChangesFeed.visibility = View.VISIBLE
            removeChangesFeed.isClickable = true


        } else {
            feedrssBlockbtnSalvab.visibility = View.VISIBLE
            feedrssBlockbtnSalvab.isClickable = true

            feedrssBlockbtnSalvaw.visibility = View.VISIBLE
            feedrssBlockbtnSalvaw.isClickable = true

            feedrssBlockbtnAnnulla.visibility = View.VISIBLE
            feedrssBlockbtnAnnulla.isClickable = false

            saveChangesFeed.visibility = View.INVISIBLE
            saveChangesFeed.isClickable = false

            cancelChangesFeed.visibility = View.INVISIBLE
            cancelChangesFeed.isClickable = false

            removeChangesFeed.visibility = View.INVISIBLE
            removeChangesFeed.isClickable = false

        }
        when(newValue){
            "https://www.eyefootball.com/football_news.xml" -> radio2.isChecked = true
            "https://www.gazzetta.it/rss/serie-b.xml" -> radio2.isChecked = true
            "https://www.gazzetta.it/rss/motociclismo.xml" -> radio3.isChecked = true
            "https://www.gazzetta.it/rss/formula-1.xml" -> FeedFormula.isChecked = true
            "https://www.gazzetta.it/rss/basket.xml" -> FeedBasket.isChecked = true
        }
    }
}
