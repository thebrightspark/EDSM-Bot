package brightpspark.edsmbot.listener

import brightpspark.edsmbot.command.DiscordContext
import brightpspark.edsmbot.command.RequiresAdmin
import bvanseg.kotlincommons.armada.events.CommandExecuteEvent
import bvanseg.kotlincommons.evenir.annotations.SubscribeEvent
import net.dv8tion.jda.api.Permission
import org.springframework.stereotype.Component

/**
 * @author bright_spark
 */
@Component
class EvenirEventListener {
	@SubscribeEvent
	fun onCommand(event: CommandExecuteEvent.Pre) {
		if (event.command.data.containsKey(RequiresAdmin::class) && (event.context as DiscordContext).event.member?.hasPermission(Permission.ADMINISTRATOR) != true)
			event.isCancelled = true
	}
}
