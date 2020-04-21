package brightpspark.edsmbot.model

/**
 * @author bright_spark
 */
data class Status(
	val lastUpdate: String,
	val type: StatusType,
	val message: String,
	val status: Int
)
