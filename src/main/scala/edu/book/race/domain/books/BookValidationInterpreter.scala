package edu.book.race.domain.books

import cats.Applicative
import cats.data.EitherT
import cats.implicits._
import edu.book.race.domain.{BookAlreadyExistsError, BookNotFoundError}

class BookValidationInterpreter[F[_] : Applicative](bookRepositoryAlgebra: BookRepositoryAlgebra[F]) extends BookValidationAlgebra[F] {
  override def doesNotExists(book: Book): EitherT[F, BookAlreadyExistsError, Unit] =
    EitherT {
      bookRepositoryAlgebra.findByTitleAndAuthor(book.title, book.author).map {
        matches => if (matches.isEmpty) Right(()) else Left(BookAlreadyExistsError(book))
      }
    }

  override def exists(bookId: Option[Long]): EitherT[F, BookNotFoundError.type, Unit] =
    EitherT {
      bookId match {
        case Some(id) =>
          bookRepositoryAlgebra.get(id).map {
            case Some(_) => Right(())
            case _ => Left(BookNotFoundError)
          }
        case _ => Either.left[BookNotFoundError.type, Unit](BookNotFoundError).pure[F]
      }
    }
}
