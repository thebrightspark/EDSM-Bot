package brightpspark.edsmbot

import bvanseg.kotlincommons.armada.CommandManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author bright_spark
 */
@Configuration
class Beans {
	/**
	 * [CommandManager] bean
	 */
	@Bean
	fun commandManager(@Value("\${prefix:ed?}") prefix: String): CommandManager<Long> = CommandManager(prefix)
}
