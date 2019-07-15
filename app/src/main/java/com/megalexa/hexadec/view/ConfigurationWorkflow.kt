package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexadecUser
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.presenter.adapter.recyclerViewConfigurationAdapter
import kotlinx.android.synthetic.main.workflow_view.*
import android.R.id
import android.content.Context
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import androidx.core.view.get
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.WorkflowRemoveBlockConnection
import com.megalexa.hexadec.presenter.contract.ConfigurationPresenter
import com.megalexa.hexadec.presenter.contract.MainContract
import com.megalexa.hexadec.presenter.recyclerViewWorkflowAdapter
import com.megalexa.hexadec.utils.ApplicationContext
import com.megalexa.hexadec.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.feedrss_configuration_item.*
import kotlinx.android.synthetic.main.text_configuration_item.*
import kotlinx.android.synthetic.main.workflow_configuration_view.*
import kotlinx.android.synthetic.main.workflow_item.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject

class ConfigurationWorkflow: AppCompatActivity(), MainContract.ConfigurationView {


    internal lateinit var presenter: MainContract.ConfigurationContract
    private lateinit var  workName: Workflow
    private lateinit var recyclerView: RecyclerView
    private  var rows: MutableList<recyclerViewConfigurationAdapter.RowType> = mutableListOf<recyclerViewConfigurationAdapter.RowType>()
    private  var currentWorkflowPosition: Int = 0

    val context: Context = this

    override fun setPresenter(presenter: MainContract.ConfigurationContract) {
        this.presenter=presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workflow_configuration_view)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setPresenter(ConfigurationPresenter(this@ConfigurationWorkflow))
        workName  =  intent.getSerializableExtra("WorkflowCorrente") as Workflow
        currentWorkflowPosition  =  intent.getIntExtra("position",-1) as Int
        val TextWorkName = findViewById<TextView>(R.id.worknameConfiguration)
        TextWorkName.text = workName.getWorkflowName()
        initRecycler()
        initRecyclerListener()

        deleteWorkflowConf.setOnClickListener {
            val intent=Intent(this@ConfigurationWorkflow, ViewActivity::class.java)
            Thread{
                val json = JSONObject()
                json.put("IDUser",HexaDec.user.getId()).put("WorkflowName", workName.getWorkflowName())
                WorkflowConnection.deleteOperation(json)
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
                startActivity(intent)
            }.start()


        }
        backWorkflowList.setOnClickListener {
            val intent=Intent(this@ConfigurationWorkflow, ViewActivity::class.java)
            startActivity(intent)
        }

        modifyWokflow.setOnClickListener {
            val intent=Intent(this@ConfigurationWorkflow, NewWorkflowActivity::class.java)
            intent.putExtra("configuration", "yes")
            intent.putExtra("CurrentWorkflow", workName)
            intent.putExtra("Position", currentWorkflowPosition)
            startActivity(intent)
        }

        var trovato = false
        for(block in workName.getBlocks())
        {
            if(block.getNameBlock().toUpperCase() == "SECURITY" && !trovato){
                textViewWorkflowPin.setImageResource(R.drawable.lock)
                trovato = true
            }
        }
        if(!trovato) textViewWorkflowPin.setImageResource(R.drawable.unlock_icon)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return true
    }

    private fun initRecyclerListener() {
        var myRecycler : RecyclerView = findViewById<RecyclerView>(R.id.recyclerViewConfiguration)

        myRecycler.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                myRecycler,
                object : RecyclerItemClickListener.OnItemClickListener {

                    //Click rapido
                    override fun onItemClick(view: View, position: Int) {

                        var intent:Intent = Intent(this@ConfigurationWorkflow, ConfigurationWorkflow::class.java)

                        when(workName.getBlockAt(position).getNameBlock().toUpperCase()){
                            "TEXT" -> intent = Intent(this@ConfigurationWorkflow, BlockActivity::class.java)
                            "FEEDRSS" -> intent = Intent(this@ConfigurationWorkflow, FeedRssBlockActivity::class.java)
                            "WEATHER" -> intent = Intent(this@ConfigurationWorkflow, WeatherBlockActivity::class.java)
                            "TVPROGRAM" -> intent = Intent(this@ConfigurationWorkflow, TVProgramBlockActivity::class.java)
                            "KINDLE" -> intent = Intent(this@ConfigurationWorkflow, KindleBlockActivity::class.java)
                            "INSTAGRAM" -> intent = Intent(this@ConfigurationWorkflow, InstagramBlockActivity::class.java)
                            "MAIL" -> intent = Intent(this@ConfigurationWorkflow, GMailBlockActivity::class.java)
                            "FILTER" -> intent = Intent(this@ConfigurationWorkflow, FilterBlockActivity::class.java)

                        }
                        if(workName.getBlockAt(position).getNameBlock().toUpperCase() == "SECURITY"){
                            Toast.makeText(this@ConfigurationWorkflow,resources.getString(R.string.PinError), Toast.LENGTH_LONG).show()
                        }
                        else {
                            intent.putExtra("configuration", "yes")
                            intent.putExtra("CurrentWorkflow", workName)
                            intent.putExtra("newTextValue", workName.getBlockAt(position).getConfig())
                            intent.putExtra("Position", position)
                            //getBlockType(workName, position)
                            startActivity(intent)
                        }
                    }
                    //Click Lungo
                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@ConfigurationWorkflow, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@ConfigurationWorkflow,message, Toast.LENGTH_LONG).show()
        }
    }


    //Funzione per caricare la recycler
    private fun initRecycler() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewConfiguration)
        recyclerView.layoutManager = LinearLayoutManager(this@ConfigurationWorkflow, RecyclerView.VERTICAL, false)


        val workName  =  intent.getSerializableExtra("WorkflowCorrente") as Workflow


        //if(rows.size != workName.getWorkflowSize()) {
        rows.clear()
        var position=0
        for (block in workName.getBlocks()) {

            when (block.getNameBlock()) {
                "TEXT" -> rows.add(
                    recyclerViewConfigurationAdapter.TextRow(
                        block.getNameBlock(),
                        block.getConfig()
                    )
                )
                "FEEDRSS" -> rows.add(
                    recyclerViewConfigurationAdapter.FeedRssRow(
                        block.getNameBlock(),
                        presenter.getType(block.getConfig())
                    )
                )
                "WEATHER" -> rows.add(
                    recyclerViewConfigurationAdapter.WeatherRow(
                        block.getNameBlock(),
                        block.getConfig()
                    )
                )
                "TVPROGRAM" -> rows.add(
                    recyclerViewConfigurationAdapter.TVProgramRow(
                        block.getNameBlock(),
                        block.getConfig()
                    )
                )
                "KINDLE" -> rows.add(
                    recyclerViewConfigurationAdapter.KindleRow(
                        block.getNameBlock(),
                        block.getConfig()
                    )
                )
                "INSTAGRAM" -> rows.add(
                    recyclerViewConfigurationAdapter.InstagramRow(
                        block.getNameBlock(),
                        block.getConfig()
                    )
                )
                "MAIL" -> rows.add(
                    recyclerViewConfigurationAdapter.EmailRow(
                        block.getNameBlock(),
                        block.getConfig()
                    )
                )
                "SECURITY" -> rows.add(
                    recyclerViewConfigurationAdapter.PinRow(
                        block.getNameBlock()
                    )
                )
            }
            if(block.getNameBlock() == "FILTER"){
                rows.add(
                    recyclerViewConfigurationAdapter.FilterRow(
                        block.getNameBlock(),
                        block.getConfig(),
                        workName.getBlockAt(position-1).getNameBlock()
                    )
                )
            }
            position++
        }
        recyclerView.adapter = recyclerViewConfigurationAdapter(rows)


    }


    override fun onSupportNavigateUp(): Boolean {

        super.onBackPressed()

        return true
    }

}