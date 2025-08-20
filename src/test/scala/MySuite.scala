// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import services.{WeatherService, WeatherServiceImpl}
import config.ApiConfig
import models.{WeatherResponse, WeatherFeature, WeatherProperties, TimeSeries}
import sttp.client3.*
import sttp.client3.testing.SttpBackendStub
import scala.util.Try

class WeatherServiceSuite extends munit.FunSuite {
  val mockConfig = ApiConfig(
    url = "https://test.metoffice.gov.uk/api",
    key = "test-key"
  )

  val validResponse = """{
    "features": [{
      "properties": {
        "timeSeries": [{
          "time": "2025-08-20T12:00Z",
          "screenTemperature": 20.5,
          "windSpeed10m": 5.2,
          "probOfPrecipitation": 10.0
        }]
      }
    }]
  }"""

  test("getHourlyForecast should parse successful response") {
    val backend = SttpBackendStub.synchronous
      .whenRequestMatches(_ => true)
      .thenRespond(validResponse)

    val service = WeatherServiceImpl(mockConfig, backend)
    val result = service.getHourlyForecast(51.7, -0.197)

    assert(result.isRight, "Expected successful response parsing")
    result.map { response =>
      assertEquals(response.features.length, 1)
      val timeSeries = response.features.head.properties.timeSeries.head
      assertEquals(timeSeries.screenTemperature, Some(20.5))
      assertEquals(timeSeries.windSpeed10m, Some(5.2))
      assertEquals(timeSeries.probOfPrecipitation, Some(10.0))
    }
  }

  test("getHourlyForecast should handle failed HTTP response") {
    val backend = SttpBackendStub.synchronous
      .whenRequestMatches(_ => true)
      .thenRespondServerError()

    val service = WeatherServiceImpl(mockConfig, backend)
    val result = service.getHourlyForecast(51.7, -0.197)

    assert(result.isLeft, "Expected error response")
  }

  test("getHourlyForecast should handle invalid JSON") {
    val backend = SttpBackendStub.synchronous
      .whenRequestMatches(_ => true)
      .thenRespond("invalid json")

    val service = WeatherServiceImpl(mockConfig, backend)
    val result = service.getHourlyForecast(51.7, -0.197)

    assert(result.isLeft, "Expected JSON parsing error")
  }
}
