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
 * @author bright_spark
 */
@Service
class ApiService {
	companion object {
		private val REST = RestTemplateBuilder().errorHandler(object : ResponseErrorHandler {
			override fun hasError(response: ClientHttpResponse): Boolean = false
			override fun handleError(response: ClientHttpResponse) = Unit
		}).build()

		private val JSON_MAPPER = jacksonObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)

		private const val API_BASE = "https://www.edsm.net/api-v1/"

		private const val API_STATUS = "https://www.edsm.net/api-status-v1/elite-server"
		private const val API_SYSTEM = API_BASE + "system?showCoordinates=1&showPermit=1&showInformation=1&showPrimaryStar=1&systemName=%s"
		private const val API_SYSTEMS_SPHERE_NAME = API_BASE + "sphere-systems?showCoordinates=1&showPermit=1&showInformation=1&showPrimaryStar=1&systemName=%s&minRadius=%s&radius=%s"
		private const val API_SYSTEMS_SPHERE_COORD = API_BASE + "sphere-systems?showCoordinates=1&showPermit=1&showInformation=1&showPrimaryStar=1&x=%s&y=%s&z=%s&minRadius=%s&radius=%s"
		private const val API_SYSTEMS_CUBE_NAME = API_BASE + "cube-systems?showCoordinates=1&showPermit=1&showInformation=1&showPrimaryStar=1&systemName=%s&size=%s"
		private const val API_SYSTEMS_CUBE_COORD = API_BASE + "cube-systems?showCoordinates=1&showPermit=1&showInformation=1&showPrimaryStar=1&x=%s&y=%s&z=%s&size=%s"
	}

	private val log = getLogger()

	private fun get(request: String): String {
		log.info("HTTP GET: $request")
		val response = REST.getForEntity<String>(request)
		val status = response.statusCode
		check(status.is2xxSuccessful) { "Error getting request '$request'\nCode: ${status.value()}\n${response.body}" }
		checkNotNull(response.body) { "Response body is null for request '$request'\nCode: ${status.value()}" }
		log.info("Received successful response with status code ${status.value()}: '${status.reasonPhrase}'")
		return response.body!!
	}

	private inline fun <reified T> getAndParse(request: String): T? {
		val json = get(request)
		return try {
			JSON_MAPPER.readValue<T>(json)
		} catch (e: JsonProcessingException) {
			log.error("Failed to deserialise ${T::class.simpleName} response:\n$json\nError: ${e.message}")
			null
		}
	}

	fun getStatus(): Status? = getAndParse(API_STATUS)

	fun getSystem(systemName: String): System? = getAndParse(API_SYSTEM.format(systemName))

	private fun validateRadius(minRadius: Int, radius: Int) {
		require(minRadius in 0..radius) { "Min radius ($minRadius) must be between 0 and the radius ($radius)!" }
		require(radius in minRadius..100) { "Radius ($radius) must be between min radius ($minRadius) and 100!" }
	}

	fun getSystemsInSphere(systemName: String, minRadius: Int, radius: Int): Array<System>? {
		validateRadius(minRadius, radius)
		return getAndParse(API_SYSTEMS_SPHERE_NAME.format(systemName, minRadius, radius))
	}

	fun getSystemsInSphere(x: Int, y: Int, z: Int, minRadius: Int, radius: Int): Array<System>? {
		validateRadius(minRadius, radius)
		return getAndParse(API_SYSTEMS_SPHERE_COORD.format(x, y, z, minRadius, radius))
	}

	private fun validateSize(size: Int) = require(size in 0..200) { "Size ($size) must be between 0 and 200!" }

	fun getSystemsInCube(systemName: String, size: Int): Array<System>? {
		validateSize(size)
		return getAndParse(API_SYSTEMS_CUBE_NAME.format(systemName, size))
	}

	fun getSystemsInCube(x: Int, y: Int, z: Int, size: Int): Array<System>? {
		validateSize(size)
		return getAndParse(API_SYSTEMS_CUBE_COORD.format(x, y, z, size))
	}

	// https://github.com/thebrightspark/TrelloEmbedBot/blob/master/src/main/kotlin/brightspark/trelloembedbot/listener/RequestHandler.kt

	// https://www.edsm.net/en_GB/api-v1
}
