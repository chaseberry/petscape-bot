package com.petscape.discord.commands

import com.petscape.discord.Command
import com.petscape.discord.Ignored
import com.petscape.discord.isStaff
import com.petscape.discord.respond
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

@Ignored
object BossKcCommand : Command() {

    override val name = "kc"

    override fun internalExecute(event: MessageReceivedEvent, args: List<String>) {
        val player = args.firstOrNull() ?: return error(event, "Must specify a player name for 'kc'")
        val kc = args.getOrNull(1)

        //TODO allow players to search their own/other players kc?

        if (kc != null) {
            kc.toIntOrNull()?.let {
                //TODO save kc
                event.respond("Set $player's boss kills to $kc.")
            } ?: event.respond("Invalid 'kc' $kc")

        } else {
            //TODO lookup kc
            val totalKc = 0
            event.respond("$player has $totalKc boss kills.")
        }
    }

    override fun canTrigger(event: MessageReceivedEvent): Boolean {
        return event.isStaff()
    }

    override val help = """
        Usage: !ps kc member [boss kills]
        
        Gets the members boss kc
        if 'boss kills' specified, will set their boss kc to that number
        
        !ps kc blakdragon
        !ps kc blakdragon 1200
    """.trimIndent()
}