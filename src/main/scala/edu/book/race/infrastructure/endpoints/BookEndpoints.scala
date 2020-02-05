package edu.book.race.infrastructure.endpoints

import cats.Monad
import cats.effect._
import cats.implicits._
import edu.book.race.domain.books.{Book, BookService}
import edu.book.race.domain.{BookAlreadyExistsError, BookNotFoundError}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

class BookEndpoints[F[_] : Monad : Sync] extends Http4sDsl[F] {

  implicit val decoder: EntityDecoder[F, Book] = jsonOf[F, Book]

  private[this] def createEndpoint(bookService: BookService[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case req@POST -> Root =>
      val result = for {
        book <- req.as[Book]
        response <- bookService.create(book).value

      } yield response

      result.flatMap {
        case Right(book) => Ok(book.asJson)
        case Left(BookAlreadyExistsError(book)) =>
          Conflict(s"Book with following title: ${book.title} and author: ${book.author} already exists")
      }
  }

  private[this] def getByIdEndpoint(bookService: BookService[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / id =>
      bookService.get(id.toLong).value flatMap {
        case Right(book) => Ok(book.asJson)
        case Left(BookNotFoundError) => NotFound("Book not found")
      }
  }

  private[this] def getAllEndpoint(bookService: BookService[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      for {
        books <- bookService.list()
        resp <- Ok(books.asJson)
      } yield resp
  }

  private[this] def updateEndpoint(bookService: BookService[F]): HttpRoutes[F] = HttpRoutes.of[F] {
    case req@PUT -> Root =>
      val result =
        for {
          book <- req.as[Book]
          updated <- bookService.update(book).value
        } yield updated

      result.flatMap {
        case Right(book) => Ok(book.asJson)
        case Left(BookNotFoundError) => NotFound()
      }
  }

  private def endpoints(bookService: BookService[F]) =
    createEndpoint(bookService) <+> getByIdEndpoint(bookService) <+> getAllEndpoint(bookService) <+> updateEndpoint(bookService)

}

object BookEndpoints {
  def apply[F[_] : Monad : Sync](bookService: BookService[F]): HttpRoutes[F] =
    new BookEndpoints[F].endpoints(bookService)
}