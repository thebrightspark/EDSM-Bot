package brightpspark.edsmbot.model

import java.awt.Color

/**
 * @author bright_spark
 */
enum class Superpower(val colour: Color, val logoUrl: String) {
	FEDERATION(Color.RED, "https://vignette.wikia.nocookie.net/elite-dangerous/images/2/2c/FederationInsignia.png/revision/latest?cb=20150623012616"),
	EMPIRE(Color.BLUE, "https://vignette.wikia.nocookie.net/elite-dangerous/images/d/da/EmpireInsignia.png/revision/latest?cb=20150623012128"),
	ALLIANCE(Color.GREEN, "https://vignette.wikia.nocookie.net/elite-dangerous/images/5/56/AllianceInsignia.png/revision/latest?cb=20150623012414"),
	INDEPENDENT(Color.ORANGE, "https://vignette.wikia.nocookie.net/elite-dangerous/images/0/00/Independent_insignia.png/revision/latest?cb=20181219204802");

	val displayName: String = name.toLowerCase().capitalize()
}
