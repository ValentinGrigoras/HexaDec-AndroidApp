package com.megalexa.hexadec.utils


interface RowType
class TextRow(val nome: String, val positionW: String, val textM: String) : RowType
class FeedRssRow(val nome: String, val positionW: String, val feedType: String, val feedUrl: String) : RowType
class YouTube(val nome: String, val positionW: String, val urlM: String) : RowType
class YouTubeMusic(val nome: String, val positionW: String, val urlM: String) : RowType
class Weather(val nome: String, val positionW: String, val cityM: String) : RowType
class Filter(val nome: String, val positionW: String, val BlockM: String, val limits: String) : RowType
class Radio(val nome: String, val positionW: String, val urlM: String) : RowType
class Mail(val nome: String, val positionW: String, val urlM: String) : RowType
