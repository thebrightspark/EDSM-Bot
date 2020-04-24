package brightpspark.edsmbot

import brightpspark.edsmbot.listener.EvenirEventListener
import brightpspark.edsmbot.listener.JDAEventListener
import bvanseg.kotlincommons.armada.CommandManager
import bvanseg.kotlincommons.armada.gears.Gear
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.*
import javax.security.auth.login.LoginException
import kotlin.system.exitProcess

/**
 * Main class for the application
 *
 * @author bright_spark
 */
@SpringBootApplication
class EdsmBotApplication {
	/**
	 * The token defined in the application.properties
	 */
	@Value("\${token}")
	private lateinit var token: String

	@Autowired
	private lateinit var jdaEventListener: JDAEventListener

	@Autowired
	private lateinit var evenirEventListener: EvenirEventListener

	/**
	 * Gets the [CommandManager] bean defined in [Beans]
	 */
	@Autowired
	private lateinit var commandManager: CommandManager<Long>

	/**
	 * Gets all components which extend [Gear]
	 */
	@Autowired
	private lateinit var gears: Array<Gear>

	/**
	 * This is called on startup to initialise the application
	 */
	@Bean
	fun init() {
		// Ensure we have a token
		require(token.isNotBlank()) { "Token not set" }

		val builder = JDABuilder(token)
			// Disable the caches - we don't need them for this bot
			.setDisabledCacheFlags(EnumSet.allOf(CacheFlag::class.java))
			// Disable typing and presence update events - again, not needed
			.setGuildSubscriptionsEnabled(false)
			// Sets status to DND during startup
			.setStatus(OnlineStatus.DO_NOT_DISTURB)
			// Sets the bot user's activity
			.setActivity(Activity.playing("www.edsm.net | ${commandManager.prefix} help"))
			// Allows us to use @SubscribeEvent for event handlers instead of extend Listener
			.setEventManager(AnnotatedEventManager())
			// Add our listener
			.addEventListeners(jdaEventListener)
		try {
			builder.build()
		} catch (e: LoginException) {
			e.printStackTrace()
			exitProcess(0)
		}

		// Add our Evenir event listener to Armada's event bus
		commandManager.eventBus.addListener(evenirEventListener)
		// Register all our command gears to Armada
		gears.forEach { commandManager.addGear(it) }
	}
}

fun main(args: Array<String>) {
	runApplication<EdsmBotApplication>(*args)
}
