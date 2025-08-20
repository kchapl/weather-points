package models

import io.circe.*
import io.circe.generic.auto.*

case class WeatherResponse(features: List[WeatherFeature])
case class WeatherFeature(properties: WeatherProperties)
case class WeatherProperties(timeSeries: List[TimeSeries])

case class TimeSeries(
    time: String,
    screenTemperature: Option[Double],
    windSpeed10m: Option[Double],
    probOfPrecipitation: Option[Double]
)
