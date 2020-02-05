package edu.book.race

import cats.effect._
import cats.implicits._
import edu.book.race.config.ServerConfig
import edu.book.race.domain.books.{BookService, BookValidationInterpreter, InMemBookRepositoryInterpreter}
import edu.book.race.infrastructure.DbConfig
import edu.book.race.infrastructure.endpoints.BookEndpoints
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

object Server extends IOApp {

  def createServer =
    for {
      config <- Resource.pure[IO, ServerConfig.type](ServerConfig)
      bookRepo = InMemBookRepositoryInterpreter[IO]
      bookValidator = BookValidationInterpreter[IO](bookRepo)
      bookService = BookService[IO](bookRepo, bookValidator)
      httpApp = Router("/book" -> BookEndpoints[IO](bookService)).orNotFound
      _ <- Resource.liftF(IO(DbConfig.initializeDb(bookRepo)))
      server <- BlazeServerBuilder[IO]
        .bindHttp(config.port, config.host)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    createServer.use(_ => IO.never).as(ExitCode.Success)

}
