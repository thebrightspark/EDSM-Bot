package brightpspark.edsmbot

import brightpspark.edsmbot.command.DiscordContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

/**
 * Returns a truncated string containing the first [n] characters from this string, or the entire string if this string
 * is shorter.
 */
fun String.truncate(n: Int, truncated: String = "..."): String {
	if (this.length <= n)
		return this
	return take(n - truncated.length) + truncated
}

fun MessageChannel.sendError(message: String) =
	this.sendMessage(EmbedBuilder().setDescription(message).setColor(Color.RED).build())

fun EmbedBuilder.addField(name: String?, value: String?) = this.addField(name, value, true)

fun EmbedBuilder.setDescription(description: String, e: RuntimeException) = this.setDescription(
	if (DiscordContext.DEBUG && e.message != null)
		"$description\n\n```${e.message!!.truncate(MessageEmbed.TEXT_MAX_LENGTH - description.length - 8)}```"
	else
		description
)
