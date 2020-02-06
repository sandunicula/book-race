package edu.book.race.config


case class ServerConfig(port: Int, host: String)
case class ApplicationConfig(server: ServerConfig)