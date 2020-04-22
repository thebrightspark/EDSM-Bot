package brightpspark.edsmbot

/**
 * @author bright_spark
 */
sealed class BotException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)
class InvalidInputException(message: String) : BotException(message)
class HttpException(message: String) : BotException(message)
class NoDataException(message: String) : BotException(message)
class JsonException(message: String, cause: Throwable) : BotException(message, cause)
