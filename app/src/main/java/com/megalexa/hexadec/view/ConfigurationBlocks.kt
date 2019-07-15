package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.megalexa.hexadec.R
import kotlinx.android.synthetic.main.workflow_view.my_toolbar


class ConfigurationBlocks: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Calcio", "Notizie"))

       var name = intent.getStringExtra("name")
    }

    override fun onStart() {
        super.onStart()
        var TextV = findViewById<TextView>(R.id.textViewWorkflowName)
        val prova = intent.extras.getString("workflowCorrente")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@ConfigurationBlocks, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@ConfigurationBlocks,message, Toast.LENGTH_LONG).show()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}