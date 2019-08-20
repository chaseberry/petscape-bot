package com.petscape.discord

import edu.csh.chase.kjson.Json

class Config(val token: String, val easterEggs: Boolean = true) {

    companion object {
        fun parse(txt: String): Config {
            val obj = Json.parseToObject(txt) ?: throw IllegalArgumentException("Config is not a Json object")
            return Config(
                token = obj.getString("token") ?: throw IllegalArgumentException("token is null or not a string"),
                easterEggs = obj.getBoolean("easterEggs", true)
            )
        }
    }

}