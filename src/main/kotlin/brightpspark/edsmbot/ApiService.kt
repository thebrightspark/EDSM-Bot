package brightpspark.edsmbot

import brightpspark.edsmbot.model.Status
import brightpspark.edsmbot.model.System
import bvanseg.kotlincommons.any.getLogger
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.getForEntity

/**
 * Service to get data from the EDSM API
 *
 * @author bright_spark
 */
@Service
class ApiService {
	companion object {
		/**
		 * The REST template used to build requests
		 * Has a custom error handler which does not handle errors so they can be handled in [get]
		 */
		private val REST = RestTemplateBuilder().errorHandler(object : ResponseErrorHandler {
			override fun hasError(response: ClientHttpResponse): Boolean = false
			override fun handleError(response: ClientHttpResponse) = Unit
		}).build()

		/**
		 * The Jackson JSON mapper, with enum case insensitivity
		 */
		private val JSON_MAPPER = jacksonObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
		private const val EMPTY_JSON = "[]"

		private const val API_BASE = "https://www.edsm.net/api-v1/"

		private const val API_STATUS = "https://www.edsm.net/api-status-v1/elite-server"

		private const val SYSTEM_PARAMS = "showId=1&showCoordinates=1&showPermit=1&showInformation=1&showPrimaryStar=1"
		private const val API_SYSTEM = API_BASE + "system?${SYSTEM_PARAMS}&systemName=%s"
		private const val API_SYSTEMS_SPHERE_NAME = API_BASE + "sphere-systems?${SYSTEM_PARAMS}&systemName=%s&minRadius=%s&radius=%s"
		private const val API_SYSTEMS_SPHERE_COORD = API_BASE + "sphere-systems?${SYSTEM_PARAMS}&x=%s&y=%s&z=%s&minRadius=%s&radius=%s"
		private const val API_SYSTEMS_CUBE_NAME = API_BASE + "cube-systems?${SYSTEM_PARAMS}&systemName=%s&size=%s"
		private const val API_SYSTEMS_CUBE_COORD = API_BASE + "cube-systems?${SYSTEM_PARAMS}&x=%s&y=%s&z=%s&size=%s"
	}

	private val log = getLogger()

	/**
	 * Posts the [request] and returns the result
	 *
	 * @param request The HTTP GET request to post
	 * @return The response body
	 * @throws HttpException if the response contains an error code or the response body is empty
	 */
	private fun get(request: String): String {
		log.info("HTTP GET: $request")
		val response = REST.getForEntity<String>(request)
		val status = response.statusCode
		if (status.isError)
			throw HttpException("Error getting request '$request'\nCode: ${status.value()}\n${response.body}")
		if (response.body.isNullOrEmpty())
			throw HttpException("Response body is null or empty for request '$request'\nCode: ${status.value()}")
		log.info("Received successful response with status code ${status.value()}: '${status.reasonPhrase}'")
		return response.body!!
	}

	/**
	 * Posts the [request] and returns the result parse from JSON to the type [T]
	 *
	 * @param request The HTTP GET request to post
	 * @param T The return type to parse the response JSON to
	 * @return The response body parsed to a [T]
	 * @throws HttpException if the response contains an error code or the response body is empty
	 * @throws NoDataException if the response contains no data
	 * @throws JsonException if the response fails to parse to the type [T]
	 */
	private inline fun <reified T> getAndParse(request: String): T {
		val json = get(request)
		if (json == EMPTY_JSON)
			throw NoDataException("No data received for request: $request")
		return try {
			JSON_MAPPER.readValue(json)
		} catch (e: JsonProcessingException) {
			throw JsonException("Failed to deserialise ${T::class.simpleName} response for request: $request", e)
		}
	}

	/**
	 * Uses the EDSM Status API to get the Elite Dangerous server status
	 *
	 * @return The ED server [Status]
	 */
	fun getStatus(): Status = getAndParse(API_STATUS)

	/**
	 * Uses the EDSM Systems API to get details about the system by its [systemName]
	 *
	 * @param systemName The name of the system to get details of
	 * @return The [System] object containing the details
	 */
	fun getSystem(systemName: String): System = getAndParse(API_SYSTEM.format(systemName))

	/**
	 * Validates that the [minRadius] and [radius] are within their correct bounds for the EDSM Sphere Systems API
	 *
	 * @param minRadius The minimum radius of the sphere
	 * @param radius The maximum radius of the sphere
	 * @throws InvalidInputException If either the [minRadius] or [radius] are out of their bounds
	 */
	private fun validateRadius(minRadius: Int, radius: Int): Unit = when {
		minRadius !in 0..radius -> throw InvalidInputException("Min radius ($minRadius) must be between 0 and the radius ($radius)!")
		radius !in minRadius..100 -> throw InvalidInputException("Radius ($radius) must be between min radius ($minRadius) and 100!")
		else -> Unit
	}

	fun getSystemsInSphere(systemName: String, minRadius: Int, radius: Int): Array<System> {
		validateRadius(minRadius, radius)
		return getAndParse(API_SYSTEMS_SPHERE_NAME.format(systemName, minRadius, radius))
	}

	fun getSystemsInSphere(x: Int, y: Int, z: Int, minRadius: Int, radius: Int): Array<System> {
		validateRadius(minRadius, radius)
		return getAndParse(API_SYSTEMS_SPHERE_COORD.format(x, y, z, minRadius, radius))
	}

	/**
	 * Validates that the [size] is within its correct bounds for the EDSM Cube Systems API
	 *
	 * @param size The size of the cube
	 * @throws InvalidInputException If the [size] is out of its bounds
	 */
	private fun validateSize(size: Int) = when (size) {
		!in 0..200 -> throw InvalidInputException("Size ($size) must be between 0 and 200!")
		else -> Unit
	}

	fun getSystemsInCube(systemName: String, size: Int): Array<System> {
		validateSize(size)
		return getAndParse(API_SYSTEMS_CUBE_NAME.format(systemName, size))
	}

	fun getSystemsInCube(x: Int, y: Int, z: Int, size: Int): Array<System> {
		validateSize(size)
		return getAndParse(API_SYSTEMS_CUBE_COORD.format(x, y, z, size))
	}

	// https://github.com/thebrightspark/TrelloEmbedBot/blob/master/src/main/kotlin/brightspark/trelloembedbot/listener/RequestHandler.kt

	// https://www.edsm.net/en_GB/api-v1
}
