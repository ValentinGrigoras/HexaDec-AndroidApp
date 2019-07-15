
package com.megalexa.hexadec.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.Workflow
import com.megalexa.hexadec.model.block.Filtrable
import com.megalexa.hexadec.model.block.Security
import com.megalexa.hexadec.presenter.AddBlockPresenter
import com.megalexa.hexadec.presenter.BlockListAdapter
import com.megalexa.hexadec.presenter.contract.MainContract
import kotlinx.android.synthetic.main.addblock_gridview.*
import kotlinx.android.synthetic.main.workflow_view.my_toolbar


class AddBlocksActivity: AppCompatActivity() {
    private lateinit var newWorkflow:Workflow
    private var conf: String = "ciao"
    private var checkIfYouCanFiltrate = false
    private var checkIfYouCanPin = true
    private val arrayListImage = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addblock_gridview)
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val gridView = findViewById<GridView>(R.id.gridview)


        arrayListImage.add(R.drawable.text_icon2)
        arrayListImage.add(R.drawable.feedrss_icon)
        if(checkIfYouCanFiltrate) {
            arrayListImage.add(R.drawable.filter_icon)
        }else{
            arrayListImage.add(R.drawable.filter_iconno)
        }
        arrayListImage.add(R.drawable.instagram_icon)
        arrayListImage.add(R.drawable.meteo)//meteo
        if(checkIfYouCanPin){
            arrayListImage.add(R.drawable.securitykey)//security
        }else{
            arrayListImage.add(R.drawable.securitykeyno)//security
        }
        arrayListImage.add(R.drawable.pdf_icon)//kindle
        arrayListImage.add(R.drawable.tv)//tv program
        arrayListImage.add(R.drawable.mail)//mail
        if(intent.hasExtra("configuration")) {
            conf = intent.getStringExtra("configuration")
        }


        newWorkflow = if(conf == "yes") intent.getSerializableExtra("WorkflowCorrente") as Workflow
        else intent.getSerializableExtra("Workflow") as Workflow

        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        val name = if(currentLocale.toString() == "en") {
                arrayOf("Text", "Feed RSS", "Filter", "Instagram", "Weather", "Security", "Kindle", "Tv Program.", "Mail")
        }else{
                arrayOf("Testo", "Feed RSS", "Fitro", "Instagram", "Meteo", "Sicurezza", "Kindle", "Prog. Tv", "Mail")
        }

        val customAdapter = BlockListAdapter(this@AddBlocksActivity, arrayListImage, name)
        gridView.adapter = customAdapter

        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when(position){
                0-> {
                    val intentText= Intent(this@AddBlocksActivity, BlockActivity::class.java)
                    intentText.putExtra("WorkFlow",newWorkflow)
                    intentText.putExtra("configuration","no")
                    startActivity(intentText)
                }
                1->{
                    val intentFeed= Intent(this@AddBlocksActivity, FeedRssBlockActivity::class.java)
                    intentFeed.putExtra("WorkFlow",newWorkflow)
                    intentFeed.putExtra("configuration","no")
                    startActivity(intentFeed)
                }
                2->{
                    val intentFilter= Intent(this@AddBlocksActivity, FilterBlockActivity::class.java)
                    if(checkIfYouCanFiltrate) {
                        intentFilter.putExtra("WorkFlow", newWorkflow)
                        intentFilter.putExtra("configuration", "no")
                        startActivity(intentFilter)
                    }else{
                        startToast(resources.getString(R.string.erroreFilter))
                    }
                }
                3-> { val intentInstagram=Intent(this@AddBlocksActivity, InstagramBlockActivity::class.java)
                    intentInstagram.putExtra("WorkFlow",newWorkflow)
                    intentInstagram.putExtra("configuration","no")
                    startActivity(intentInstagram)
                }
                /*4-> { val intentRadio=Intent(this@AddBlocksActivity, RadioBlockActivity::class.java)
                    intentRadio.putExtra("WorkFlow",newWorkflow)
                    intentRadio.putExtra("configuration","no")
                    startActivity(intentRadio)
                }*/
                4-> { val intentWeather=Intent(this@AddBlocksActivity, WeatherBlockActivity::class.java)
                    intentWeather.putExtra("WorkFlow",newWorkflow)
                    intentWeather.putExtra("configuration","no")
                    startActivity(intentWeather)
                }
                5-> { val intentFilter=Intent(this@AddBlocksActivity, SecurityBlockActivity::class.java)
                    if(checkIfYouCanPin){
                        intentFilter.putExtra("WorkFlow",newWorkflow)
                        intentFilter.putExtra("configuration","no")
                        startActivity(intentFilter)
                    }else{
                        startToast(resources.getString(R.string.errorePin))
                    }
                }
                6-> { val intentFilter=Intent(this@AddBlocksActivity, KindleBlockActivity::class.java)
                    intentFilter.putExtra("WorkFlow",newWorkflow)
                    intentFilter.putExtra("configuration","no")
                    startActivity(intentFilter)
                }
                /*11-> { val intentFilter=Intent(this@AddBlocksActivity, SlackBlockActivity::class.java)
                    intentFilter.putExtra("WorkFlow",newWorkflow)
                    intentFilter.putExtra("configuration","no")
                    startActivity(intentFilter)
                }*/
                7-> { val intentFilter=Intent(this@AddBlocksActivity, TVProgramBlockActivity::class.java)
                    intentFilter.putExtra("WorkFlow",newWorkflow)
                    intentFilter.putExtra("configuration","no")
                    startActivity(intentFilter)
                }
                8-> { val intentFilter=Intent(this@AddBlocksActivity, GMailBlockActivity::class.java)
                    intentFilter.putExtra("WorkFlow",newWorkflow)
                    intentFilter.putExtra("configuration","no")
                    startActivity(intentFilter)
                }
                /*9-> { val intentYoutube=Intent(this@AddBlocksActivity, YouTubeBlockActivity::class.java)
                    intentYoutube.putExtra("WorkFlow",newWorkflow)
                    intentYoutube.putExtra("configuration","no")
                    startActivity(intentYoutube)
                }*/
                /*10-> { val intentFilter=Intent(this@AddBlocksActivity, TelegramBlockActivity::class.java)
                    intentFilter.putExtra("WorkFlow",newWorkflow)
                    intentFilter.putExtra("configuration","no")
                    startActivity(intentFilter)
                }*/
                /*11-> { val intentYoutube=Intent(this@AddBlocksActivity, YouTubeMusicBlockActivity::class.java)
                   intentYoutube.putExtra("WorkFlow",newWorkflow)
                   intentYoutube.putExtra("configuration","no")
                   startActivity(intentYoutube)
               }*/
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_menunocerca, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.settings) {
            startActivity(Intent(this@AddBlocksActivity, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        if( intent.hasExtra("Workflow")) {
            newWorkflow = intent.getSerializableExtra("Workflow") as Workflow
        }
        else{

        }
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        checkIfYouCan()
        updateImg()
    }

    override fun onResume() {
        super.onResume()
        checkIfYouCan()
        updateImg()
    }

    fun checkIfYouCan(){
        if(newWorkflow.getWorkflowSize() >0){
            val blocks = newWorkflow.getBlocks()
            if(blocks[blocks.size-1] is Filtrable){
                checkIfYouCanFiltrate = true
            }
        }
        if(newWorkflow.getWorkflowSize() >0){
            val blocks = newWorkflow.getBlocks()
            for(block in blocks){
                if(block is Security){
                    checkIfYouCanPin = false
                }
            }
        }
    }

    fun updateImg(){
        if(checkIfYouCanFiltrate) {
            arrayListImage[2]=R.drawable.filter_icon
        }else{
            arrayListImage[2]=R.drawable.filter_iconno
        }
        if(checkIfYouCanPin) {
            arrayListImage[5]=R.drawable.securitykey
        }else{
            arrayListImage[5]=R.drawable.securitykeyno
        }
    }

    private fun startToast(message:String) {
        runOnUiThread {
            Toast.makeText(this@AddBlocksActivity,message,Toast.LENGTH_LONG).show()
        }
    }
}