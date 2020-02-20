package edu.book.race.infrastructure.repository

import java.time.OffsetDateTime.now

import edu.book.race.domain.books.{Book, BookRepositoryAlgebra}

object DbInitializer {

  def initializeDb[F[_]](bookRepositoryAlgebra: BookRepositoryAlgebra[F]): Unit = {
    bookRepositoryAlgebra.create(Book(None, "I Am Legend", "Martin", now().minusYears(2)))
    bookRepositoryAlgebra.create(Book(None, "Lord of the Ring", "John", now().minusYears(3)))
    bookRepositoryAlgebra.create(Book(None, "Game of Thrones", "Steve", now().minusYears(2).minusDays(20)))
    bookRepositoryAlgebra.create(Book(None, "The Witcher", "Neo", now().minusYears(1)))
  }

}
