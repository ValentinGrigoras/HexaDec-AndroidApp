package com.megalexa.hexadec.model.block

class Block constructor(private val blockName:String,private val configuration:String) {

    fun getBlock(): BlockInterface {
        when(blockName.toUpperCase()) {
            "SECURITY"-> return BlockSecurity(configuration)
            "TEXT"-> return BlockText(configuration)
            "FEEDRSS"-> return BlockFeedRss(configuration)
            "INSTAGRAM"-> return BlockInstagram(configuration)
            "KINDLE"-> return BlockKindle(configuration)
            "RADIO"-> return BlockRadio(configuration)
            "TELEGRAM"-> return BLockTelegram(configuration)
            "YOUTUBE"-> return BlockYouTube(configuration)
            "YOUTUBEMUSIC"-> return BlockYouTubeMusic(configuration)
            "WEATHER"-> return BlockWeather(configuration)
            "FILTER"-> return BlockFilter(configuration)
            "TVPROGRAM"-> return BlockTVProgram(configuration)
            "MAIL"-> return BlockMail(configuration)
            else-> {
                return BlockText(configuration)
            }
        }
    }

    fun getConfig(): String {
        when(blockName.toUpperCase()) {
           "SECURITY"-> return BlockSecurity(configuration).getConfig()
            "TEXT"-> return BlockText(configuration).getConfig()
            "FEEDRSS"-> return BlockFeedRss(configuration).getConfig()
            "INSTAGRAM"-> return BlockInstagram(configuration).getConfig()
            "KINDLE"-> return BlockKindle(configuration).getConfig()
            "RADIO"-> return BlockRadio(configuration).getConfig()
            "TELEGRAM"-> return BLockTelegram(configuration).getConfig()
            "YOUTUBE"-> return BlockYouTube(configuration).getConfig()
            "YOUTUBEMUSIC"-> return BlockYouTubeMusic(configuration).getConfig()
            "WEATHER"-> return BlockWeather(configuration).getConfig()
            "FILTER"-> return BlockFilter(configuration).getConfig()
            "MAIL"-> return BlockMail(configuration).getConfig()
            "TVPROGRAM"-> return BlockTVProgram(configuration).getConfig()
            else-> {
                return "Errore"
            }
        }
    }
}
