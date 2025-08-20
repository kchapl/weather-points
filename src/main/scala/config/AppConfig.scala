package config



import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

final case class AppConfig(
    api: ApiConfig
) derives ConfigReader

final case class ApiConfig(
    url: String,
    key: String
) derives ConfigReader
