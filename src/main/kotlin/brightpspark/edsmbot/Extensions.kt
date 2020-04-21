package brightpspark.edsmbot

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color

fun MessageChannel.sendError(message: String) =
	this.sendMessage(EmbedBuilder().setDescription(message).setColor(Color.RED).build())

fun EmbedBuilder.addField(name: String?, value: String?) = this.addField(name, value, true)
