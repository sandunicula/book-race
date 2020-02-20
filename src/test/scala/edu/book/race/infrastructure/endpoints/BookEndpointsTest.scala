package edu.book.race.infrastructure.endpoints

import cats.data.EitherT
import cats.effect.IO
import edu.book.race.assets._
import edu.book.race.domain.BookNotFoundError
import edu.book.race.domain.books.{Book, BookService}
import io.circe.generic.auto._
import org.http4s.Status.{NotFound, Ok}
import org.http4s.circe.jsonOf
import org.http4s.{EntityDecoder, Method, Request, Uri}
import org.mockito.scalatest.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// TODO: cover all scenarios
class BookEndpointsTest extends AnyFlatSpec with MockitoSugar with Matchers {
  private[this] implicit val decoder: EntityDecoder[IO, Book] = jsonOf[IO, Book]

  private[this] val bookService = mock[BookService[IO]]

  private[this] val bookEndpoints = BookEndpoints[IO](bookService)

  private[this] def getResponse(request: Request[IO]) = bookEndpoints.run(request).value.unsafeRunSync.get

  "Get book by id endpoint" should "return book if exist" in {
    when(bookService.get(1)) thenReturn EitherT.fromEither(Right(testBook))
    val response = getResponse(Request(method = Method.GET, uri = Uri(path = "/1")))

    response.status shouldBe Ok
    response.as[Book].unsafeRunSync shouldBe testBook
  }

  it should "return not found if not book exists" in {
    when(bookService.get(1)) thenReturn EitherT.fromEither(Left(BookNotFoundError))
    val response = getResponse(Request(method = Method.GET, uri = Uri(path = "/1")))

    response.status shouldBe NotFound
    response.as[String].unsafeRunSync shouldBe "Book not found"
  }


}
