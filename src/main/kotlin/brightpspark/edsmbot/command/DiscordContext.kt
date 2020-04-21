package brightpspark.edsmbot.command

import bvanseg.kotlincommons.armada.contexts.Context
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

/**
 * @author bright_spark
 */
class DiscordContext(val event: MessageReceivedEvent) : Context {
	fun reply(message: String) = event.channel.sendMessage(message).queue()

	fun reply(message: MessageEmbed) = event.channel.sendMessage(message).queue()
}
