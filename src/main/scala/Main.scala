import config.AppConfig
import services.{WeatherService, WeatherServiceImpl}
import sttp.client3.HttpClientSyncBackend
import pureconfig.ConfigSource

@main def main: Unit = 
  val config = ConfigSource.default.load[AppConfig] match
    case Right(cfg) => cfg
    case Left(errors) =>
      println(s"Failed to load configuration: $errors")
      sys.exit(1)

  val backend = HttpClientSyncBackend()
  val weatherService = WeatherServiceImpl(config.api, backend)

  weatherService.getHourlyForecast(51.7, -0.197) match
    case Right(response) =>
      response.features.headOption match
        case Some(feature) =>
          println("Weather forecast:")
          feature.properties.timeSeries.take(5).foreach { ts =>
            println(s"Time: ${ts.time}")
            ts.screenTemperature.foreach(temp => println(s"Temperature: $temp°C"))
            ts.windSpeed10m.foreach(speed => println(s"Wind Speed: $speed m/s"))
            println("---")
          }
        case None =>
          println("No weather data available")
    case Left(error) =>
      println(s"Error: $error")
  println("Hello world!")
  println(msg)

def msg = "I was compiled by Scala 3. :)"
