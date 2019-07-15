package com.megalexa.hexadec.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.presenter.SettingPresenter
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar
import java.util.*


class SettingsActivity : AppCompatActivity(),MainContract.SettingView {
    internal lateinit var presenter: MainContract.SettingContract
    private lateinit var btnLang: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(R.layout.settings_activity)
        setSupportActionBar(my_toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = getString(R.string.app_name)
        setPresenter(SettingPresenter(this@SettingsActivity))
        btnLang =  findViewById(R.id.btnChangeLang)
        btnLang.setOnClickListener {
            showChangeLang()
        }
        findViewById<TextView>(R.id.textViewValueUserName).text = HexaDec.user.getName()
        findViewById<TextView>(R.id.textViewValueEmail).text = HexaDec.user.getEmail()
        findViewById<EditText>(R.id.editTextPin).setText(HexaDec.user.getPin())
        SaveButton.setOnClickListener {
            startToast("Updating PIN . . .")
            presenter.updatePin(findViewById<EditText>(R.id.editTextPin).text.toString())
        }
        settingsAnnulla.setOnClickListener{
            val intent=Intent(this@SettingsActivity, ViewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showChangeLang(){
        val listItem = arrayOf("English","Italiano")
        val mBuilder = AlertDialog.Builder(this@SettingsActivity)
        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        val checked = if(currentLocale.toString() == "en"){
            0
        } else{
            1
        }
        mBuilder.setSingleChoiceItems(listItem,checked){dialog,which->
            when(which){
                0-> {
                    setLocate("en")
                    recreate()
                }
                1->{
                    setLocate("it")
                    recreate()
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(Lang: String){
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings",Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setPresenter(presenter: MainContract.SettingContract) {
        this.presenter=presenter
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@SettingsActivity,message, Toast.LENGTH_LONG).show()
        }
    }
}