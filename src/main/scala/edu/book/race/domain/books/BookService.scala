package edu.book.race.domain.books

import cats.Monad
import cats.data.EitherT
import edu.book.race.domain.{BookAlreadyExistsError, BookNotFoundError}

class BookService[F[_] : Monad](bookRepositoryAlgebra: BookRepositoryAlgebra[F], bookValidationAlgebra: BookValidationAlgebra[F]) {

  def create(book: Book): EitherT[F, BookAlreadyExistsError, Book] =
    for {
      _ <- bookValidationAlgebra.doesNotExists(book)
      book <- EitherT.liftF(bookRepositoryAlgebra.create(book))
    } yield book

  def update(book: Book): EitherT[F, BookNotFoundError.type, Book] =
    for {
      _ <- bookValidationAlgebra.exists(book.id)
      updated <- EitherT.fromOptionF(bookRepositoryAlgebra.update(book), BookNotFoundError)
    } yield updated

  def get(id: Long): EitherT[F, BookNotFoundError.type, Book] =
    EitherT.fromOptionF(bookRepositoryAlgebra.get(id), BookNotFoundError)

  def list(): F[List[Book]] = bookRepositoryAlgebra.list()

}

object BookService {
  def apply[F[_] : Monad](bookRepositoryAlgebra: BookRepositoryAlgebra[F], bookValidationAlgebra: BookValidationAlgebra[F]): BookService[F] =
    new BookService(bookRepositoryAlgebra, bookValidationAlgebra)
}