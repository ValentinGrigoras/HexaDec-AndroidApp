package com.megalexa.hexadec.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.presenter.BlockPresenter
import com.megalexa.hexadec.presenter.WorkflowConnection
import com.megalexa.hexadec.presenter.WorkflowRemoveBlockConnection
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.textview.*
import kotlinx.android.synthetic.main.textview.sBlockbtnAnnulla
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvab
import kotlinx.android.synthetic.main.textview.sBlockbtnSalvaw
import kotlinx.android.synthetic.main.textview.textBlockConf
import kotlinx.android.synthetic.main.weatherview.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class WeatherBlockActivity : AppCompatActivity(),MainContract.BlockView {


    internal lateinit var presenter: MainContract.BlockContract
    private lateinit var newWorkflow :Workflow
    private lateinit var currentWorkflow :Workflow
    private  var newValue: String = ""
    private  var currentBlockPosition: Int = 0
    // inside a basic activity
    private lateinit var locationManager : LocationManager


private final val MY_PERMISSION_REQUEST_LOCATION = 1




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weatherview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
        setPresenter(BlockPresenter(this@WeatherBlockActivity))



        sBlockbtnSalvab.setOnClickListener {
            if(findViewById<EditText>(R.id.textBlockConf).text.toString() != "") {
                newWorkflow=presenter.addBlock(newWorkflow,findViewById<EditText>(R.id.textBlockConf).text.toString(),"WEATHER")
                val intent=Intent(this@WeatherBlockActivity, AddBlocksActivity::class.java)
                intent.putExtra("Workflow",newWorkflow)
                intent.putExtra("configuration", "no")
                startActivity(intent)
            }else{
                startToast(getResources().getString(R.string.errMeteo))
            }
        }

        sBlockbtnSalvaw.setOnClickListener {
            if(findViewById<EditText>(R.id.textBlockConf).text.toString() != "") {
                    newWorkflow = presenter.addBlock(
                        newWorkflow,
                        findViewById<EditText>(R.id.textBlockConf).text.toString(),
                        "WEATHER"
                    )
                    Thread {
                        val json = JSONObject()
                        json.put("IDUser", HexaDec.user.getId()).put("WorkflowName", newWorkflow.getWorkflowName())
                            .put("WelcomeText", newWorkflow.getWelcomeText())
                            .put("SuggestedTime", newWorkflow.getSuggestedTime())
                        WorkflowConnection.postOperation(json)
                        //correct
                        presenter.saveBlock(newWorkflow)
                        HexaDec.workflowList.add(newWorkflow)
                        // reload hexadec
                        val workflows = java.util.ArrayList<Workflow>()
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
                        startActivity(Intent(this@WeatherBlockActivity, ViewActivity::class.java))
                    }.start()
                }else{
                    startToast(getResources().getString(R.string.errMeteo))
            }
        }
        sBlockbtnAnnulla.setOnClickListener {
            val intent=Intent(this@WeatherBlockActivity, NewWorkflowActivity::class.java)
            intent.putExtra("Workflow",newWorkflow)
            startActivity(intent)
        }


        val btn_geol = findViewById<Button>(R.id.btn_geo)
        btn_geol.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this@WeatherBlockActivity, Manifest.permission.ACCESS_COARSE_LOCATION)){
                    ActivityCompat.requestPermissions(this@WeatherBlockActivity,
                        Array<String>(2){Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION)

                }
                else{
                    ActivityCompat.requestPermissions(this@WeatherBlockActivity,
                        Array<String>(2){Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION)
                }
            }
            else{
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var location: Location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                try{
                    textBlockConf.setText(hereLocation(location.latitude, location.longitude))
                } catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(this@WeatherBlockActivity, "Not Found", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
    fun hereLocation(lat: Double, lon: Double) : String{
        var city: String = ""
        var geocoder : Geocoder = Geocoder(this@WeatherBlockActivity, Locale.getDefault())
        var  addressList: List<Address>
        try{
            addressList = geocoder.getFromLocation(lat, lon, 1)
            if(addressList.isNotEmpty()){
                city = addressList.get(0).locality

            }
        } catch(e: Exception){
            e.printStackTrace()
        }
        return city

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this@WeatherBlockActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        var location: Location = locationManager.getLastKnownLocation(NETWORK_PROVIDER)
                        try {
                            textBlockConf.setText(hereLocation(location.latitude, location.longitude))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this@WeatherBlockActivity, "Not Found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@WeatherBlockActivity, "No permission granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@WeatherBlockActivity, WeatherBlockActivity::class.java))
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
        if(intent.hasExtra("configuration")) {
            var conf: String = intent.getStringExtra("configuration")
            getIntents(conf)
            setCustomLayout(conf)
        }

        textBlockConf.setText(newValue)

        saveChangesWeather.setOnClickListener {
            if(textBlockConf.text.toString().toUpperCase() != newValue.toUpperCase()) {
                val intent = Intent(this@WeatherBlockActivity, ConfigurationWorkflow::class.java)
                newValue = textBlockConf.text.toString()
                currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
                presenter.modifyBlock(currentWorkflow, currentBlockPosition)
                //currentWorkflow.getBlockAt(currentBlockPosition).

                intent.putExtra("WorkflowCorrente", currentWorkflow)
                startActivity(intent)
            }
            else{
                    startToast(getResources().getString(R.string.modifyError))
            }
        }
        cancelChangesWeather.setOnClickListener {
            val intent=Intent(this@WeatherBlockActivity, ConfigurationWorkflow::class.java)
            intent.putExtra("WorkflowCorrente",currentWorkflow)
            startActivity(intent)
        }
        removeChangesMeteo.setOnClickListener {
            val intent=Intent(this@WeatherBlockActivity, ViewActivity::class.java)
            //newValue = textBlockConf.text.toString()
            currentWorkflow.getBlockAt(currentBlockPosition).setConfig(newValue)
            Thread {
                WorkflowRemoveBlockConnection.removeBlock(currentWorkflow, currentBlockPosition)
                intent.putExtra("WorkflowCorrente",currentWorkflow)
                startActivity(intent)
                //currentWorkflow.getBlockAt(currentBlockPosition).
            }.start()

        }
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@WeatherBlockActivity,message, Toast.LENGTH_LONG).show()
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
            saveChangesWeather.visibility = View.VISIBLE
            saveChangesWeather.isClickable = true

            cancelChangesWeather.visibility = View.VISIBLE
            cancelChangesWeather.isClickable = true

            removeChangesMeteo.visibility = View.VISIBLE
            removeChangesMeteo.isClickable = true

            sBlockbtnSalvab.visibility = View.INVISIBLE
            sBlockbtnSalvab.isClickable = false

            sBlockbtnSalvaw.visibility = View.INVISIBLE
            sBlockbtnSalvaw.isClickable = false

            sBlockbtnAnnulla.visibility = View.INVISIBLE
            sBlockbtnAnnulla.isClickable = false
        }
        else{
            saveChangesWeather.visibility = View.INVISIBLE
            saveChangesWeather.isClickable = false

            cancelChangesWeather.visibility = View.INVISIBLE
            cancelChangesWeather.isClickable = false

            removeChangesMeteo.visibility = View.INVISIBLE
            removeChangesMeteo.isClickable = false

            sBlockbtnSalvab.visibility = View.VISIBLE
            sBlockbtnSalvab.isClickable = true

            sBlockbtnSalvaw.visibility = View.VISIBLE
            sBlockbtnSalvaw.isClickable = true

            sBlockbtnAnnulla.visibility = View.VISIBLE
            sBlockbtnAnnulla.isClickable = true

        }
    }


}