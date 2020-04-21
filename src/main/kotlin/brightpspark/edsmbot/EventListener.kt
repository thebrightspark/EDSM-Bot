package brightpspark.edsmbot

import brightpspark.edsmbot.command.DiscordContext
import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.armada.CommandManager
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author bright_spark
 */
@Component
class EventListener {
	private val log = getLogger()

	@Autowired
	private lateinit var commandManager: CommandManager<Long>

	@SubscribeEvent
	fun onReady(event: ReadyEvent) {
		log.info("JDA ready with ${event.guildAvailableCount} / ${event.guildTotalCount} guilds!")
		event.jda.presence.setStatus(OnlineStatus.ONLINE)
	}

	@SubscribeEvent
	fun onMessage(event: MessageReceivedEvent) {
		val author = event.author
		if (author.isBot) return
		val raw = event.message.contentRaw
		log.debug("Received command from $author: $raw")
		try {
			commandManager.execute(raw, DiscordContext(event))
		} catch (e: Exception) {
			log.error("Error executing command '$raw'", e)
		}
	}
}
