package brightpspark.edsmbot.command.gear

import brightpspark.edsmbot.*
import brightpspark.edsmbot.command.DiscordContext
import brightpspark.edsmbot.model.StatusType
import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.armada.annotations.Command
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.numbers.format
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * The main command gear
 *
 * @author bright_spark
 */
@Component
class MainGear : Gear() {
	private val log = getLogger()

	@Autowired
	private lateinit var apiService: ApiService

	@Command("Get the Elite: Dangerous server status")
	fun status(ctx: DiscordContext) = ctx.reply {
		setTitle("Status")
		try {
			apiService.getStatus().run {
				setColor(type.colour)
				setDescription(message)
				setFooter("Last Update: $lastUpdate")
			}
		} catch (e: BotException) {
			log.error(e.message, e.cause)
			setColor(StatusType.DANGER.colour)
			setDescription("Failed to get status", e)
		}
	}

	@Command("Get info about a system")
	fun system(ctx: DiscordContext, systemName: String) = ctx.reply {
		try {
			apiService.getSystem(systemName).run {
				setTitle(name, url)
				information.allegiance?.let {
					setColor(it.colour)
					setThumbnail(it.logoUrl)
				}
				addField("Coords", """
					**X:** ${coords.x.format()}
					**Y:** ${coords.y.format()}
					**Z:** ${coords.z.format()}
					**Dist From Sol:** ${coords.distance.format()}LY
				""".trimIndent())
				if (requirePermit)
					addField("Permit Required", permitName)
				if (primaryStar.name != null)
					addField("Primary Star", """
						**Name:** ${primaryStar.name}
						**Type:** ${primaryStar.type}
						**Scoopable:** ${if (primaryStar.isScoopable!!) "Yes" else "No"}
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
				setFooter("System ID: $id")
			}
		} catch (e: BotException) {
			log.error(e.message, e.cause)
			when (e) {
				is InvalidInputException -> setDescription("Invalid input:\n${e.message}")
				is HttpException -> setDescription("Error getting response from EDSM", e)
				is NoDataException -> setDescription("No system found with name '$systemName'")
				is JsonException -> setDescription("Error deserialising response from EDSM", e)
			}
		}
	}
}
