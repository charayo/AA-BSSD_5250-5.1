package com.example.aa_bssd_5250_51
import org.json.JSONObject
import java.util.*

class Note(var name:String, var desc:String, var date:String?, var important:Int) {
    init {
        if (date == null){
            date = Date().toString()
        }
    }
    fun toJSON(): JSONObject {
        //make a new json object
        val jsonObject = JSONObject().apply {
            //put each place of data into the object
            put("name", name)
            put("date", date)
            put("desc", desc)
            put("important", important)
        }
        return jsonObject
    }
    override fun toString(): String {
        return "$name, $date, $desc, $important"
    }
}