package brightpspark.edsmbot.model

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * @author bright_spark
 */
data class System(
	val name: String,
	val id: Long?,
	val id64: Long?,
	val coords: Coords,
	val coordsLocked: Boolean,
	val requirePermit: Boolean,
	val permitName: String?,
	val information: SystemInfo,
	val primaryStar: Star
) {
	@get:JsonIgnore
	val url = id?.let { "https://www.edsm.net/en_GB/system/id/$it/name/$name" }
}
