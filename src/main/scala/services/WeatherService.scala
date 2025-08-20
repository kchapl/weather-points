package services

import models.*
import config.ApiConfig
import sttp.client3.*
import sttp.client3.circe.*
import io.circe.generic.auto.*
import io.circe.Decoder
import io.circe.generic.semiauto._

trait WeatherService:
  def getHourlyForecast(latitude: Double, longitude: Double): Either[Throwable, WeatherResponse]

class WeatherServiceImpl(config: ApiConfig, backend: SttpBackend[Identity, Any]) extends WeatherService:
  def getHourlyForecast(latitude: Double, longitude: Double): Either[Throwable, WeatherResponse] =
    val request = basicRequest
      .get(uri"${config.url}?latitude=$latitude&longitude=$longitude")
      .header("accept", "application/json")
      .header("apikey", config.key)
      .response(asJson[WeatherResponse])

    val response = request.send(backend)
    
    response.body match
      case Right(weatherResponse) => Right(weatherResponse)
      case Left(error) => Left(error)
