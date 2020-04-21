package brightpspark.edsmbot.model

import java.awt.Color

/**
 * @author bright_spark
 */
enum class Superpower(val colour: Color) {
	FEDERATION(Color.RED),
	EMPIRE(Color.BLUE),
	ALLIANCE(Color.GREEN);

	val displayName: String = name.toLowerCase().capitalize()
}
