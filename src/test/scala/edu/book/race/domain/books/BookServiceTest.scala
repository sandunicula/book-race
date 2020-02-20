package edu.book.race.domain.books

import cats.Id
import cats.data.EitherT
import edu.book.race.domain.{BookAlreadyExistsError, BookNotFoundError}
import edu.book.race.assets._
import org.mockito.scalatest.MockitoSugar
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BookServiceTest extends AnyFlatSpec with Matchers with MockitoSugar {

  val bookRepository: BookRepositoryAlgebra[Id] =
    mock[BookRepositoryAlgebra[Id]]
  val bookValidator: BookValidationAlgebra[Id] =
    mock[BookValidationAlgebra[Id]]

  val bookService: BookService[Id] = BookService(bookRepository, bookValidator)

  "BookService.create" should "return created book" in {
    when(bookValidator.doesNotExists(testBook)) thenReturn EitherT.fromEither[Id](Right(()))
    when(bookRepository.create(testBook)) thenReturn testBook

    bookService.create(testBook).value shouldBe Right(testBook)
  }

  it should "return an error if book already exists" in {
    when(bookValidator.doesNotExists(testBook)) thenReturn EitherT.fromEither[Id](Left(BookAlreadyExistsError(testBook)))

    bookService.create(testBook).value shouldBe Left(BookAlreadyExistsError(testBook))
  }

  "BookService.update" should "return error if book does not exists" in {
    when(bookValidator.exists(*)) thenReturn EitherT.fromEither[Id](Left(BookNotFoundError))

    bookService.update(testBook).value shouldBe Left(BookNotFoundError)
  }

  it should "return updated book if book exists" in {
    when(bookValidator.exists(testBook.id)) thenReturn EitherT.fromEither[Id](Right(()))
    when(bookRepository.update(testBook)) thenReturn Some(testBook)

    bookService.update(testBook).value shouldBe Right(testBook)
  }

  "BookService.list" should "return all books" in {
    when(bookRepository.list()) thenReturn List(testBook)

    bookService.list() shouldBe List(testBook)
  }

  "BookService.get" should "return book by id" in {
    when(bookRepository.get(testBook.id.get)) thenReturn Some(testBook)

    bookService.get(testBook.id.get).value shouldBe Right(testBook)
  }

  it should "return BookNotFound if no book exists" in {
    when(bookRepository.get(testBook.id.get)) thenReturn None

    bookService.get(testBook.id.get).value shouldBe Left(BookNotFoundError)
  }

}