package com.petscape.discord

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

abstract class Command {

    abstract val name: String

    protected abstract fun internalExecute(event: MessageReceivedEvent, args: List<String>)

    abstract val help: String

    open fun canTrigger(event: MessageReceivedEvent): Boolean = true

    fun exectue(event: MessageReceivedEvent, args: List<String>) {
        if (args.getOrNull(0) == "help") {
            //Do help thing
            return
        }

        internalExecute(event, args)
    }

    fun error(event: MessageReceivedEvent, error: String) {

    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Ignored