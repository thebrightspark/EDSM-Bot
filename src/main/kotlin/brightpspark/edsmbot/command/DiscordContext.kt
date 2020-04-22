package brightpspark.edsmbot.command

import bvanseg.kotlincommons.armada.contexts.Context
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

/**
 * @author bright_spark
 */
class DiscordContext(val event: MessageReceivedEvent) : Context {
	companion object {
		var DEBUG = false
	}

	fun reply(builder: EmbedBuilder.() -> Unit) =
		event.channel.sendMessage(EmbedBuilder().apply(builder).build()).queue()
}
