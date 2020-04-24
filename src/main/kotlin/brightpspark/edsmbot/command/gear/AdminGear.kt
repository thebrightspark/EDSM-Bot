package brightpspark.edsmbot.command.gear

import brightpspark.edsmbot.command.DiscordContext
import brightpspark.edsmbot.command.RequiresAdmin
import bvanseg.kotlincommons.armada.annotations.Command
import bvanseg.kotlincommons.armada.gears.Gear
import org.springframework.stereotype.Component

/**
 * The admin command gear
 *
 * @author bright_spark
 */
@Component
class AdminGear : Gear() {
	@Command("Toggles debug")
	@RequiresAdmin
	fun debug(ctx: DiscordContext) {
		DiscordContext.DEBUG = !DiscordContext.DEBUG
		ctx.reply {
			setDescription("Debug has been ${if (DiscordContext.DEBUG) "enabled" else "disabled"}")
		}
	}
}
