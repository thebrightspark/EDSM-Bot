package brightpspark.edsmbot

import bvanseg.kotlincommons.armada.CommandManager
import bvanseg.kotlincommons.armada.gears.Gear
import net.dv8tion.jda.api.JDA
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

@SpringBootApplication
class EdsmBotApplication {
	companion object {
		lateinit var jda: JDA
	}

	@Value("\${token}")
	private lateinit var token: String

	@Autowired
	private lateinit var eventListener: EventListener
	@Autowired
	private lateinit var commandManager: CommandManager<Long>
	@Autowired
	private lateinit var gears: Array<Gear>

	@Bean
	fun init() {
		require(token.isNotBlank()) { "Token not set" }

		val builder = JDABuilder(token)
			.setDisabledCacheFlags(EnumSet.allOf(CacheFlag::class.java))
			.setGuildSubscriptionsEnabled(false)
			.setStatus(OnlineStatus.DO_NOT_DISTURB)
			.setActivity(Activity.playing("www.edsm.net | ${commandManager.prefix} help"))
			.setEventManager(AnnotatedEventManager())
			.addEventListeners(eventListener)
		try {
			jda = builder.build()
		} catch (e: LoginException) {
			e.printStackTrace()
			exitProcess(0)
		}

		gears.forEach { commandManager.addGear(it) }
	}
}

fun main(args: Array<String>) {
	runApplication<EdsmBotApplication>(*args)
}
