package brightpspark.edsmbot.model

import java.awt.Color

/**
 * @author bright_spark
 */
enum class StatusType(val colour: Color) {
	SUCCESS(Color.GREEN),
	WARNING(Color.YELLOW),
	DANGER(Color.RED)
}
