package brightpspark.edsmbot.command

import bvanseg.kotlincommons.armada.contexts.Context
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

/**
 * The [Context] implementation used to pass to commands
 *
 * @author bright_spark
 */
class DiscordContext(val event: MessageReceivedEvent) : Context {
	companion object {
		/**
		 * Whether debug info should be displayed in messages sent back to the channel
		 */
		var DEBUG = false
	}

	/**
	 * Util method which creates a [net.dv8tion.jda.api.entities.MessageEmbed] from the [builder] and sends it back to
	 * the channel
	 *
	 * @param builder A block which configures the [EmbedBuilder] to then be built and sent to the channel
	 */
	fun reply(builder: EmbedBuilder.() -> Unit) =
		event.channel.sendMessage(EmbedBuilder().apply(builder).build()).queue()
}
