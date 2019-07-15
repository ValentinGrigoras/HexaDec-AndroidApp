package com.megalexa.hexadec.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.textview.sBlockbtnAnnulla
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvab
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvaw
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import android.R.id
import android.util.Log
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager.signOut
import com.megalexa.hexadec.presenter.APIGoogleConnection
import com.megalexa.hexadec.presenter.APIInstagramConnection
import com.megalexa.hexadec.presenter.WorkflowConnection
import kotlinx.android.synthetic.main.mailmview.*
import org.json.JSONObject
import java.util.ArrayList


class GMailBlockActivity : AppCompatActivity(),MainContract.BlockView {

    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow :Workflow
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mailmview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@GMailBlockActivity))
        setListener()
    }

    override fun onStart() {
        super.onStart()
        var conf : String = intent.getStringExtra("configuration")

        getIntents(conf)
        setCustomLayout(conf)
    }

    //NON TOCCARE
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@GMailBlockActivity, GMailBlockActivity::class.java))
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

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@GMailBlockActivity,message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListener(){

        sBlockbtnSalvab.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,"hexadec.swe@gmail.com","MAIL")
            val intent=Intent(this@GMailBlockActivity, AddBlocksActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }

        sBlockbtnSalvaw.setOnClickListener {
            newWorkflow=presenter.addBlock(newWorkflow,"hexadec.swe@gmail.com","MAIL")
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
                startActivity(Intent(this@GMailBlockActivity, ViewActivity::class.java))
            }.start()
        }

        sBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@GMailBlockActivity, NewWorkflowActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
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

    override fun setCustomLayout(conf:String){
        if(conf == "yes"){
            saveChangesMail.visibility = View.VISIBLE
            saveChangesMail.isClickable = true

            cancelChangesMail.visibility = View.VISIBLE
            cancelChangesMail.isClickable = true

            removeChangesMail.visibility = View.VISIBLE
            removeChangesMail.isClickable = true

            sBlockbtnSalvab.visibility = View.INVISIBLE
            sBlockbtnSalvab.isClickable = false

            sBlockbtnSalvaw.visibility = View.INVISIBLE
            sBlockbtnSalvaw.isClickable = false

            sBlockbtnAnnulla.visibility = View.INVISIBLE
            sBlockbtnAnnulla.isClickable = false
        }
        else{
            saveChangesMail.visibility = View.INVISIBLE
            saveChangesMail.isClickable = false

            cancelChangesMail.visibility = View.INVISIBLE
            cancelChangesMail.isClickable = false

            removeChangesMail.visibility = View.INVISIBLE
            removeChangesMail.isClickable = false

            sBlockbtnSalvab.visibility = View.VISIBLE
            sBlockbtnSalvab.isClickable = true

            sBlockbtnSalvaw.visibility = View.VISIBLE
            sBlockbtnSalvaw.isClickable = true

            sBlockbtnAnnulla.visibility = View.VISIBLE
            sBlockbtnAnnulla.isClickable = true



        }
    }

}

