package com.petscape.discord

import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.reflections.Reflections
import java.io.File
import kotlin.reflect.full.findAnnotation

fun main(args: Array<String>) {

    val config = args.firstOrNull()?.let {
        Config.parse(File(it).readText())
    } ?: throw IllegalArgumentException("Failed to load config file")

    //TODO reflect this bitch
    val commands = Reflections("com.petscape.discord.commands").getSubTypesOf(Command::class.java).filter {
        it.kotlin.findAnnotation<Ignored>() == null
    }.map {
        it.kotlin.objectInstance!!
    }.associate {
        it.name to it
    }

    println("Found ${commands.size} commands")
    println(commands.keys.joinToString("\n"))

    //permissions 268437568
    val jda = JDABuilder(config.token).build()

    jda.awaitReady()

    val rcRegex = Regex("rc|runecraft", RegexOption.IGNORE_CASE)

    jda.addEventListener(object : ListenerAdapter() {

        override fun onMessageReceived(event: MessageReceivedEvent) {
            val raw = event.message.contentRaw
            val msg = raw.split(" ")

            //1/100 chance to react :runecraft: to a message containing runecrafting
            if (config.easterEggs && raw.matches(rcRegex) && rand(100) == 0) {
                event.guild?.getEmotesByName("runecrafting", true)?.firstOrNull()?.let {
                    event.message.addReaction(it).queue()
                }
            }

            if (msg.size < 2) {
                return
            }

            //println("${event.channel.name} -- ${event.message.contentRaw} -- ${event.message.author}")
            if (msg[0] != "!ps") {
                return
            }

            commands[msg[1]]?.takeIf { it.canTrigger(event) }?.exectue(event, msg.drop(2))


        }

    })

}