package brightpspark.edsmbot.listener

import brightpspark.edsmbot.command.DiscordContext
import brightpspark.edsmbot.command.RequiresAdmin
import bvanseg.kotlincommons.armada.events.CommandExecuteEvent
import bvanseg.kotlincommons.evenir.annotations.SubscribeEvent
import net.dv8tion.jda.api.Permission
import org.springframework.stereotype.Component

/**
 * Class for Evenir event listeners.
 * This is where
 *
 * @author bright_spark
 */
@Component
class EvenirEventListener {
	@SubscribeEvent
	fun onCommand(event: CommandExecuteEvent.Pre) {
		// Check before the command is executed that the user has permission to use it
		if (event.command.data.containsKey(RequiresAdmin::class) && (event.context as DiscordContext).event.member?.hasPermission(Permission.ADMINISTRATOR) != true)
			event.isCancelled = true
	}
}
