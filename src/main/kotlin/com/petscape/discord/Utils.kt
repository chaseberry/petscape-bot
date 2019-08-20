package com.petscape.discord

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

fun MessageReceivedEvent.respond(msg: String) = channel.sendMessage(msg).queue()

fun rand(max: Int) = (Math.random() * max).toInt()

fun MessageReceivedEvent.isStaff(): Boolean = member.roles.any { it.name == "Clan Staff" }