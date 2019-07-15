package com.megalexa.hexadec.presenter.contract

import android.content.Context

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.megalexa.hexadec.R
import com.megalexa.hexadec.presenter.contract.MainContract
import com.megalexa.hexadec.model.HexaDec
import com.megalexa.hexadec.model.Workflow
import java.util.ArrayList


class ConfigurationPresenter(view: MainContract.ConfigurationView): MainContract.ConfigurationContract {
    override fun getType(conf: String): String {
        when(conf){
            "https://www.eyefootball.com/football_news.xml" -> return "Football"
            "https://www.gazzetta.it/rss/serie-b.xml" ->  return "Seria B"
            "https://www.gazzetta.it/rss/motociclismo.xml" -> return "Motociclismo"
            "https://www.gazzetta.it/rss/formula-1.xml" -> return "Formula 1"
            "https://www.gazzetta.it/rss/basket.xml" -> return "Basket"
            "https://www.gazzetta.it/rss/serie-a.xml" -> return "Seria A"
        }
        return "default"
    }

    private var view:MainContract.ConfigurationView? = view
    private  var wList = HexaDec.workflowList

    override fun onDestroy() {
        this.view = null
    }

}