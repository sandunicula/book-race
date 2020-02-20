package edu.book.race.domain.books

import cats.Id
import cats.syntax.option._
import edu.book.race.assets._
import edu.book.race.domain.{BookAlreadyExistsError, BookNotFoundError}
import org.mockito.scalatest.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BookValidationInterpreterTest extends AnyFlatSpec with Matchers with MockitoSugar {

  val bookRepository: BookRepositoryAlgebra[Id] = mock[BookRepositoryAlgebra[Id]]

  val bookValidator = new BookValidationInterpreter[Id](bookRepository)

  "Validator.doesNotExists" should "check if book does not exists" in {
    when(bookRepository.findByTitleAndAuthor("Title", "Author")) thenReturn List(testBook)
    when(bookRepository.findByTitleAndAuthor(*, *)) thenReturn List()

    bookValidator.doesNotExists(Book(None, "New title", "New Author", now)).value shouldBe Right(())
  }

  it should "return BookAlreadyExistsError if validation fails" in {
    when(bookRepository.findByTitleAndAuthor("test", "author")) thenReturn List(testBook)

    bookValidator.doesNotExists(testBook).value shouldBe Left(BookAlreadyExistsError(testBook))
  }

  "Validator.exists" should "return  Unit if book exists" in {
    when(bookRepository.get(1)) thenReturn testBook.some

    bookValidator.exists(1L.some).value shouldBe Right(())
  }

  it should "return Left of BookNotFoundError if book does not exists" in {
    when(bookRepository.get(1)) thenReturn None

    bookValidator.exists(1L.some).value shouldBe Left(BookNotFoundError)
  }
}
