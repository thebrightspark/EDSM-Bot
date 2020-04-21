package brightpspark.edsmbot.model

/**
 * @author bright_spark
 */
data class SystemInfo(
	val allegiance: Superpower?,
	val government: String?,
	val faction: String?,
	val factionState: String?,
	val population: Long,
	val security: String?,
	val economy: String?,
	val secondEconomy: String?,
	val reserve: String?
)
