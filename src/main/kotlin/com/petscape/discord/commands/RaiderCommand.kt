package com.petscape.discord.commands

import com.petscape.discord.Command
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

object RaiderCommand : Command() {

    override val name = "raider"

    override val help = "Sets or removes your raider role."

    override fun internalExecute(event: MessageReceivedEvent, args: List<String>) {
        val role = event.guild.roles.find { it.name == "Raider" } ?: return

        if (role in event.member.roles) {
            println("Nuking")
            event.guild.controller.removeRolesFromMember(event.member, role).queue()
        } else {
            println("Adding")
            event.guild.controller.addRolesToMember(event.member, role).queue()
        }
    }

}