package com.megalexa.hexadec.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.megalexa.hexadec.R
import com.megalexa.hexadec.model.Workflow
import org.json.JSONObject

class recyclerViewWorkflowAdapter (var workflowList: ArrayList<Workflow>) : RecyclerView.Adapter<recyclerViewWorkflowAdapter.ViewHolder>(),
    Filterable {
    val workflowListFull: ArrayList<Workflow> = ArrayList(workflowList)
    private var recyclerFilter : RecyclerFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workflow_item,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workflowList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workflow: Workflow = workflowList[position]
        holder.textViewName?.text = workflow.getWorkflowName()
        holder.textViewDate?.text = workflow.getCreationDate()
        holder.textViewSuggested?.text = workflow.getSuggestedTime()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewName =  itemView.findViewById<TextView>(R.id.textViewWorkflowName)
        val textViewDate =  itemView.findViewById<TextView>(R.id.textViewWorkflowCreateDate)
        val textViewSuggested =  itemView.findViewById<TextView>(R.id.textViewWorkflowSuggestedTime)
        val ImageViewPin =  itemView.findViewById<ImageView>(R.id.textViewWorkflowPin)
    }

    class anyBLock(json: String) : JSONObject(json) {
        val name = this.optString("BlockName")
        val value: String = setConfByName(name)
        fun setConfByName(name:String):String{
            when(name.toUpperCase()) {
                "SECURITY"-> return this.optString("PIN")
                "TEXT"-> return this.optString("TextValue")
                "FEEDRSS"-> return this.optString("URLValue")
                "INSTAGRAM"-> return this.optString("URLValue")
                "KINDLE"-> return this.optString("URLValue")
                "RADIO"-> return this.optString("RadioValue")
                "TELEGRAM"-> return this.optString("NumberValue")
                "YOUTUBE"-> return this.optString("URLValue")
                "YOUTUBEMUSIC"-> return this.optString("URLValue")
                "WEATHER"-> return this.optString("CityValue")
                "FILTER"-> return this.optString("FilterValue")
                "MAIL"-> return this.optString("MailValue")
                "TVPROGRAM"-> return this.optString("ProgrValue")
                else -> return "default"
            }
        }
    }


    override fun getFilter(): Filter {
        if(recyclerFilter == null){
            recyclerFilter = RecyclerFilter()
        }
        return recyclerFilter as RecyclerFilter
    }

    inner class RecyclerFilter: Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var results : FilterResults = FilterResults()
            if(constraint != null && constraint.length > 0){
                var localList: ArrayList<Workflow> = ArrayList<Workflow>()
                for (i: Int in 0..workflowListFull?.size.minus(1) as Int){
                    if(workflowListFull?.get(i)?.getWorkflowName()?.toLowerCase()?.contains(constraint.toString().toLowerCase()) as Boolean){
                        localList.add(workflowListFull.get(i) as Workflow)
                    }
                }
                results.values = localList
                results.count = localList.size as Int
            }else{
                results.values = workflowListFull
                results.count = workflowListFull?.size as Int
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            workflowList = results?.values as ArrayList<Workflow>
            notifyDataSetChanged()
        }

    }
}