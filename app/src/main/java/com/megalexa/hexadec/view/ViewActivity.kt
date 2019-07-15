package com.megalexa.hexadec.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

import com.megalexa.hexadec.R
import com.megalexa.hexadec.presenter.WorkflowPresenter
import com.megalexa.hexadec.presenter.contract.MainContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.workflow_view.*
import android.content.Intent
import android.util.Log
import android.widget.SearchView
import android.app.SearchManager;
import android.view.View

import android.widget.SearchView.OnQueryTextListener;
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazon.identity.auth.device.AuthError
import com.amazon.identity.auth.device.api.Listener
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.block.Block
import com.megalexa.hexadec.presenter.WorkflowBlockConnection
import com.megalexa.hexadec.presenter.recyclerViewWorkflowAdapter
import com.megalexa.hexadec.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.workflow_item.*
import kotlinx.android.synthetic.main.workflow_item.view.*

class ViewActivity : AppCompatActivity(), MainContract.WorkflowView {
    internal lateinit var presenter: MainContract.WorkflowContract
    private lateinit var recyclerView:RecyclerView
    var myadapter : recyclerViewWorkflowAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workflow_view)
        setSupportActionBar(my_toolbar)
        supportActionBar?.run{setHomeButtonEnabled(true)}
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setPresenter(WorkflowPresenter(this@ViewActivity))
    }

    override fun updateView() {
        this@ViewActivity.runOnUiThread {

            presenter.popolateView(this@ViewActivity,recyclerViewConf)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        var searchItem = menu?.findItem(R.id.action_search)
        var searchView: SearchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myadapter?.filter?.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@ViewActivity, SettingsActivity::class.java))
            return true
        }
        if ( item.itemId == R.id.logout) {
            AuthorizationManager.signOut(applicationContext, object: Listener<Void, AuthError> {
                override fun onSuccess(response: Void?) =
                    startActivity(Intent(this@ViewActivity, MainActivity::class.java))


                override fun onError(authError: AuthError) {
                    return
                }
            })
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart(){
        super.onStart()
        presenter.updateWList()

        this@ViewActivity.runOnUiThread {
            myadapter?.notifyDataSetChanged()
            recyclerView = findViewById(R.id.recyclerViewConf)
            val llm= LinearLayoutManager(this@ViewActivity, RecyclerView.VERTICAL, false)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =llm

            myadapter = recyclerViewWorkflowAdapter(HexaDec.workflowList)
            recyclerView.apply {
                adapter = myadapter
            }
        }
        btn_new_workflow.setOnClickListener {
            startToast("Add New Workflow")
            var intent:Intent = Intent(this@ViewActivity, NewWorkflowActivity::class.java)
            intent.putExtra("configuration", "no")
            startActivity(intent)

        }
        initRecyclerListener()
        myadapter?.notifyDataSetChanged()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.updateWList()
        myadapter?.notifyDataSetChanged()
        recyclerView.adapter?.notifyDataSetChanged()

    }

    override fun onRestart() {
        super.onRestart()

    }

    override fun setPresenter(presenter: MainContract.WorkflowContract) {
        this.presenter=presenter
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@ViewActivity,message,Toast.LENGTH_LONG).show()
        }
    }


    private fun initRecyclerListener() {
        var myRecycler : RecyclerView = findViewById<RecyclerView>(R.id.recyclerViewConf)

        myRecycler.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                myRecycler,
                object : RecyclerItemClickListener.OnItemClickListener {

                    //Click rapido

                    override fun onItemClick(view: View, position: Int) {

                        var intent:Intent = Intent(this@ViewActivity, ConfigurationWorkflow::class.java)
                        Thread {
                            if(HexaDec.getWorkflowByName(HexaDec.workflowList[position].getWorkflowName())?.getWorkflowSize() == 0) {
                                val blocks = WorkflowBlockConnection.getOperation(
                                    listOf(
                                        Pair("IDUser", HexaDec.user.getId()),
                                        Pair("WorkflowName", HexaDec.workflowList[position].getWorkflowName())
                                    )
                                )
                                val data = blocks.optJSONArray("Blocks")
                                    ?.let {
                                        0.until(it.length()).map { i -> it.optJSONObject(i) }
                                    } // returns an array of JSONObject
                                    ?.map { recyclerViewWorkflowAdapter.anyBLock(it.toString()) } // transforms each JSONObject of the array into Foo
                                if (data != null) {
                                    for (block in data) {
                                        HexaDec.getWorkflowByName(HexaDec.workflowList[position].getWorkflowName())
                                            ?.addBlockToWorkflow(Block(block.name.toString(), block.value).getBlock())
                                    }
                                }
                            }
                            intent.putExtra("WorkflowCorrente",   HexaDec.getWorkflowByName(HexaDec.workflowList[position].getWorkflowName()))
                            intent.putExtra("position", position)
                            startActivity(intent)
                        }.start()

                    }

                    //Click Lungo
                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )
    }


}
