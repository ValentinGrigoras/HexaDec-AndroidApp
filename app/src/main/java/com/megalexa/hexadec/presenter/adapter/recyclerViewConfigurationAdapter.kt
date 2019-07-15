package com.megalexa.hexadec.presenter.adapter
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.megalexa.hexadec.R
import com.megalexa.hexadec.utils.Mail
import org.w3c.dom.Text


class recyclerViewConfigurationAdapter(private val rows: List<RowType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface RowType
    class TextRow(val nomeT: String,  val textM: String) : RowType
    class FeedRssRow(val nomeF: String, val feedType: String) : RowType
    class WeatherRow(val nomeW: String, val city: String) : RowType
    class TVProgramRow(val nomeT: String, val canale: String) : RowType
    class KindleRow(val nomeK: String, val url: String) : RowType
    class InstagramRow(val nomeI: String, val profile: String) : RowType
    class EmailRow(val nomeE: String, val mail: String) : RowType
    class FilterRow(val nomeF: String, val limit: String, val nomeB: String) : RowType
    class PinRow(val nomeP: String) : RowType

    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_FEED = 1
        private const val TYPE_WEATHER = 2
        private const val TYPE_TVP= 3
        private const val TYPE_KINDLE= 4
        private const val TYPE_INSTAGRAM= 5
        private const val TYPE_EMAIL= 6
        private const val TYPE_FILTER= 7
        private const val TYPE_PIN= 8
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewTextConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewTextConfName)
        val confTextView: TextView = itemView.findViewById(R.id.TextConfText)
    }
    class FeedRssViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewFeedConfName)
        val typeView: TextView = itemView.findViewById(R.id.textViewFeedConfType)
    }
    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewWeatherConfName)
        val cityView: TextView = itemView.findViewById(R.id.TextCityConf)

    }
    class TVProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewTVPConfName)
        val channelView: TextView = itemView.findViewById(R.id.TextConfTVP)

    }
    class KindleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewKindleConfName)
        val urlView: TextView = itemView.findViewById(R.id.TextConfKindle)

    }
    class InstagramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewInstagramConfName)
        val urlView: TextView = itemView.findViewById(R.id.InstagramConfText)

    }
    class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewEmailConfName)
        val urlView: TextView = itemView.findViewById(R.id.textViewEmailConfValue)

    }
    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewFilterConfName)
        val blockView: TextView = itemView.findViewById(R.id.TextConfFilter)
        val limitView: TextView = itemView.findViewById(R.id.TextConfFilterLimit)

    }
    class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val positionView: TextView = itemView.findViewById(R.id.textViewFeedConfPosition)
        val nameView: TextView = itemView.findViewById(R.id.textViewPinConfName)


    }
    override fun getItemCount() = rows.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_TEXT-> TextViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.text_configuration_item, parent, false))
        TYPE_FEED -> FeedRssViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.feedrss_configuration_item, parent, false))
        TYPE_WEATHER -> WeatherViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_configuration_item, parent, false))
        TYPE_TVP -> TVProgramViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.tvprogram_configuration_item, parent, false))
        TYPE_KINDLE -> KindleViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.kindle_configuration_item, parent, false))
        TYPE_INSTAGRAM -> InstagramViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.instagram_configuration_item, parent, false))
        TYPE_EMAIL -> EmailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.email_configuration_item, parent, false))
        TYPE_FILTER -> FilterViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_configuration_item, parent, false))
        TYPE_PIN -> PinViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.pin_configuration_item, parent, false))

        else -> throw IllegalArgumentException()
    }
    override fun getItemViewType(position: Int): Int =
        when (rows[position]) {
            is TextRow -> TYPE_TEXT
            is FeedRssRow -> TYPE_FEED
            is WeatherRow -> TYPE_WEATHER
            is TVProgramRow -> TYPE_TVP
            is KindleRow -> TYPE_KINDLE
            is InstagramRow -> TYPE_INSTAGRAM
            is EmailRow -> TYPE_EMAIL
            is FilterRow -> TYPE_FILTER
            is PinRow -> TYPE_PIN
            else -> throw IllegalArgumentException()
        }

    private fun onBindText(holder: RecyclerView.ViewHolder, row: TextRow) {
        val TextRow = holder as TextViewHolder
        TextRow.nameView.text = row.nomeT
        TextRow.confTextView.text = row.textM
    }

    private fun onBindFeed(holder: RecyclerView.ViewHolder, row: FeedRssRow) {
        val FeedRow = holder as FeedRssViewHolder

        FeedRow.nameView.text = row.nomeF
        FeedRow.typeView.text = row.feedType
    }

    private fun onBindWeather(holder: RecyclerView.ViewHolder, row: WeatherRow) {
        val WeatherRow = holder as WeatherViewHolder

        WeatherRow.nameView.text = row.nomeW
        WeatherRow.cityView.text = row.city
    }
    private fun onBindTVProgram(holder: RecyclerView.ViewHolder, row: TVProgramRow) {
        val TVProgramRow = holder as TVProgramViewHolder

        TVProgramRow.nameView.text = row.nomeT
        TVProgramRow.channelView.text = row.canale
    }
    private fun onBindKindle(holder: RecyclerView.ViewHolder, row: KindleRow) {
        val KindleRow = holder as KindleViewHolder

        KindleRow.nameView.text = row.nomeK
        KindleRow.urlView.text = row.url
    }
    private fun onBindInstagram(holder: RecyclerView.ViewHolder, row: InstagramRow) {
        val InstagramRow = holder as InstagramViewHolder

        InstagramRow.nameView.text = row.nomeI
        InstagramRow.urlView.text = row.profile
    }
    private fun onBindEmail(holder: RecyclerView.ViewHolder, row: EmailRow) {
        val EmailRow = holder as EmailViewHolder

        EmailRow.nameView.text = row.nomeE
        EmailRow.urlView.text = row.mail
    }
    private fun onBindFilter(holder: RecyclerView.ViewHolder, row: FilterRow) {
        val FilterRow = holder as FilterViewHolder

        FilterRow.nameView.text = row.nomeF
        FilterRow.blockView.text = row.nomeB
        FilterRow.limitView.text = row.limit
    }
    private fun onBindPin(holder: RecyclerView.ViewHolder, row: PinRow) {
        val PinRow = holder as PinViewHolder

        PinRow.nameView.text = row.nomeP

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_TEXT -> onBindText(holder, rows[position] as recyclerViewConfigurationAdapter.TextRow)
            TYPE_FEED -> onBindFeed(holder, rows[position] as recyclerViewConfigurationAdapter.FeedRssRow)
            TYPE_WEATHER -> onBindWeather(holder, rows[position] as recyclerViewConfigurationAdapter.WeatherRow)
            TYPE_TVP -> onBindTVProgram(holder, rows[position] as recyclerViewConfigurationAdapter.TVProgramRow)
            TYPE_KINDLE -> onBindKindle(holder, rows[position] as recyclerViewConfigurationAdapter.KindleRow)
            TYPE_INSTAGRAM -> onBindInstagram(holder, rows[position] as recyclerViewConfigurationAdapter.InstagramRow)
            TYPE_EMAIL -> onBindEmail(holder, rows[position] as recyclerViewConfigurationAdapter.EmailRow)
            TYPE_FILTER -> onBindFilter(holder, rows[position] as recyclerViewConfigurationAdapter.FilterRow)
            TYPE_PIN -> onBindPin(holder, rows[position] as recyclerViewConfigurationAdapter.PinRow)
            else -> throw IllegalArgumentException()
        }
}

fun getItemId(position: Int): Long {
    return position.toLong()
}

fun getItemViewType(position: Int): Int {
    return position
}