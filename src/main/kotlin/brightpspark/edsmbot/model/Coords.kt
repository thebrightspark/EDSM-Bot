package brightpspark.edsmbot.model

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author bright_spark
 */
data class Coords(
	val x: Float,
	val y: Float,
	val z: Float
) {
	val distance = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
}
