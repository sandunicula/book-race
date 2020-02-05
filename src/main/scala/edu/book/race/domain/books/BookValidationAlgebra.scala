package edu.book.race.domain.books

import cats.data.EitherT
import edu.book.race.domain.{BookAlreadyExistsError, BookNotFoundError}

trait BookValidationAlgebra[F[_]] {

  def doesNotExists(book: Book): EitherT[F, BookAlreadyExistsError, Unit]

  def exists(bookId: Option[Long]): EitherT[F, BookNotFoundError.type, Unit]

}
