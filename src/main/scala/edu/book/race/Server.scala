package edu.book.race

import cats.effect._
import cats.implicits._
import edu.book.race.config.ApplicationConfig
import edu.book.race.domain.books.{BookService, BookValidationInterpreter}
import edu.book.race.infrastructure.endpoints.BookEndpoints
import edu.book.race.infrastructure.repository.{DbInitializer, InMemBookRepositoryInterpreter}
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import pureconfig._
import pureconfig.module.catseffect.syntax._
import pureconfig.generic.auto._

object Server extends IOApp {

  def createServer =
    for {
      config <- Resource.liftF(ConfigSource.default.loadF[IO, ApplicationConfig])
      bookRepo = InMemBookRepositoryInterpreter[IO]
      bookValidator = BookValidationInterpreter[IO](bookRepo)
      bookService = BookService[IO](bookRepo, bookValidator)
      httpApp = Router("/book" -> BookEndpoints[IO](bookService)).orNotFound
      _ <- Resource.liftF(IO(DbInitializer.initializeDb(bookRepo)))
      server <- BlazeServerBuilder[IO]
        .bindHttp(config.server.port, config.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    createServer.use(_ => IO.never).as(ExitCode.Success)

}
