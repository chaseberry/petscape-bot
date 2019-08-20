package com.petscape.discord.commands

import com.petscape.discord.Command
import com.petscape.discord.Ignored
import com.petscape.discord.isStaff
import com.petscape.discord.respond
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

@Ignored
object SplitsCommand : Command() {

    override val name = "splits"

    override fun internalExecute(event: MessageReceivedEvent, args: List<String>) {
        val player = args.firstOrNull() ?: return BossKcCommand.error(event, "Must specify a player name for 'kc'")
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
        Usage: !ps splits member [amount]
        
        Gets the members split amounts
        if 'amount' is specified, will add that amount to their existing splits
        
        !ps kc blakdragon
        !ps kc blakdragon 20
        """.trimIndent()

}