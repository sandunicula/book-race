package edu.book.race.infrastructure.repository

import edu.book.race.domain.books.{Book, BookRepositoryAlgebra}

object DbInitializer {

  def initializeDb[F[_]](bookRepositoryAlgebra: BookRepositoryAlgebra[F]): Unit = {
    bookRepositoryAlgebra.create(Book(None, "I Am Legend", "Martin"))
    bookRepositoryAlgebra.create(Book(None, "Lord of the Ring", "John"))
    bookRepositoryAlgebra.create(Book(None, "Game of Thrones", "Steve"))
    bookRepositoryAlgebra.create(Book(None, "The Witcher", "Neo"))
  }

}
