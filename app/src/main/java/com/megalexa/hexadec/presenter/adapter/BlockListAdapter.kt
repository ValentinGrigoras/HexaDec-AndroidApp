package com.megalexa.hexadec.presenter
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.megalexa.hexadec.R

class BlockListAdapter(var context: Context, private var arrayListImage: ArrayList<Int>, var name: Array<String>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var myView = convertView
        val holder: ViewHolder
        if (myView == null) {
            //If our View is Null than we Inflater view using Layout Inflater
            val mInflater = (context as Activity).layoutInflater
            //Inflating our grid_item.
            myView = mInflater.inflate(R.layout.block_entry, parent, false)
            //Create Object of ViewHolder Class and set our View to it
            holder = ViewHolder()
            //Find view By Id For all our Widget taken in grid_item.
            /*Here !! are use for non-null asserted two prevent From null.
             you can also use Only Safe (?.)
            */
            holder.mImageView = myView!!.findViewById(R.id.icon) as ImageView
            holder.mTextView = myView.findViewById(R.id.textView) as TextView
            //Set A Tag to Identify our view.
            myView.tag = holder
        } else {
            //If Our View in not Null than Just get View using Tag and pass to holder Object.
            holder = myView.getTag() as ViewHolder
        }
        //Setting Image to ImageView by position
        holder.mImageView!!.setImageResource(arrayListImage[position])
        //Setting name to textview by position
        holder.mTextView!!.text = name[position]
        return myView
    }

    //Auto Generated Method
    override fun getItem(p0: Int): Any {
        return arrayListImage[p0]
    }

    //Auto Generated Method
    override fun getItemId(p0: Int): Long {
        return 0
    }

    //Auto Generated Method
    override fun getCount(): Int {
        return arrayListImage.size
    }

    class ViewHolder {
        var mImageView: ImageView? = null
        var mTextView: TextView? = null
    }
}

