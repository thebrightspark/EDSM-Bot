package brightpspark.edsmbot.model

/**
 * @author bright_spark
 */
data class System(
	val name: String,
	val coords: Coords,
	val coordsLocked: Boolean,
	val requirePermit: Boolean,
	val permitName: String?,
	val information: SystemInfo,
	val primaryStar: Star
)
