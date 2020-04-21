package brightpspark.edsmbot.command.gear

import brightpspark.edsmbot.ApiService
import brightpspark.edsmbot.addField
import brightpspark.edsmbot.command.DiscordContext
import brightpspark.edsmbot.model.StatusType
import bvanseg.kotlincommons.armada.annotations.Command
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.numbers.format
import net.dv8tion.jda.api.EmbedBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author bright_spark
 */
@Component
class MainGear : Gear() {
	@Autowired
	private lateinit var apiService: ApiService

	@Command("Get the Elite: Dangerous server status")
	fun status(ctx: DiscordContext) = apiService.getStatus()?.run {
		ctx.reply(EmbedBuilder()
			.setTitle("Status")
			.setColor(type.colour)
			.setDescription(message)
			.setFooter("Last Update: $lastUpdate")
			.build()
		)
	} ?: ctx.reply(EmbedBuilder()
		.setTitle("Status")
		.setColor(StatusType.DANGER.colour)
		.setDescription("Failed to get status")
		.build()
	)

	@Command("Get info about a system")
	fun system(ctx: DiscordContext, systemName: String) = apiService.getSystem(systemName)?.run {
		ctx.reply(EmbedBuilder().apply {
			setTitle(name)
			information.allegiance?.let { setColor(it.colour) }
			addField("Coords", """
				**X:** ${coords.x.format()}
				**Y:** ${coords.y.format()}
				**Z:** ${coords.z.format()}
			""".trimIndent())
			if (requirePermit)
				addField("Permit Required", permitName)
			addField("Primary Star", """
				**Name:** ${primaryStar.name}
				**Type:** ${primaryStar.type}
				**Scoopable:** ${if (primaryStar.isScoopable) "Yes" else "No"}
			""".trimIndent())
			information.allegiance?.let { addField("Allegiance", it.displayName) }
			information.government?.let { addField("Government", it) }
			information.faction?.let {
				addField("Faction", """
					**Name:** $it
					**State:** ${information.factionState ?: "None"}
				""".trimIndent())
			}
			if (information.population > 0) addField("Population", information.population.format())
			information.security?.let { addField("Security", it) }
			information.economy?.let {
				addField("Economy", """
					**Main:** $it
					**Secondary:** ${information.secondEconomy ?: "None"}
				""".trimIndent())
			}
			information.reserve?.let { addField("Reserve", it) }
		}.build()
		)
	} ?: ctx.reply(EmbedBuilder().setDescription("Failed to find system with name '$systemName'").build())
}
