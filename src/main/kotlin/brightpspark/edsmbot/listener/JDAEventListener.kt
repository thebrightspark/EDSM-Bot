package brightpspark.edsmbot.listener

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
 * Class for JDA event listeners.
 * This is where we set the bot status to [OnlineStatus.ONLINE] once JDA is ready, and listen for messages to send them
 * to our Armada [CommandManager] for processing.
 *
 * @author bright_spark
 */
@Component
class JDAEventListener {
	private val log = getLogger()

	@Autowired
	private lateinit var commandManager: CommandManager<Long>

	@SubscribeEvent
	fun onReady(event: ReadyEvent) {
		log.info("JDA ready with ${event.guildAvailableCount} / ${event.guildTotalCount} guilds!")
		// The bot is now ready, so set the status to ONLINE
		event.jda.presence.setStatus(OnlineStatus.ONLINE)
	}

	@SubscribeEvent
	fun onMessage(event: MessageReceivedEvent) {
		val author = event.author
		// Ensure we're not handling commands from bots
		if (author.isBot) return
		val raw = event.message.contentRaw
		log.debug("Received command from $author: $raw")
		try {
			// Send the message to the Armada command manager
			commandManager.execute(raw, DiscordContext(event))
		} catch (e: Exception) {
			log.error("Error executing command '$raw'", e)
		}
	}
}
