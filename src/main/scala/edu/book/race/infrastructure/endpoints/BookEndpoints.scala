package edu.book.race.infrastructure.endpoints

import cats.Monad
import cats.effect._
import cats.implicits._
import edu.book.race.domain.BookNotFoundError
import edu.book.race.domain.books.BookService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class BookEndpoints[F[_] : Monad : Sync] extends Http4sDsl[F] {

  def getBookById(bookService: BookService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / id =>
        bookService.get(id.toLong).value flatMap {
          case Right(book) => Ok(book.asJson)
          case Left(BookNotFoundError) => NotFound("Book not found")
        }
    }
}

object BookEndpoints {
  def apply[F[_] : Monad : Sync](bookService: BookService[F]): HttpRoutes[F] =
    new BookEndpoints[F].getBookById(bookService)
}