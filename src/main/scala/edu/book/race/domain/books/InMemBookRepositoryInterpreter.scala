package edu.book.race.domain.books

import cats.Applicative
import cats.implicits._

import scala.util.Random

class InMemBookRepositoryInterpreter[F[_] : Applicative] extends BookRepositoryAlgebra[F] {

  private[this] val books: scala.collection.mutable.Map[Long, Book] = scala.collection.mutable.Map()

  private[this] val idGenerator: Random.type = Random

  override def create(book: Book): F[Book] = {
    val id = idGenerator.nextLong
    val createdBook = book.copy(id = Some(id))
    books += (id -> createdBook)
    createdBook.pure[F]
  }

  override def get(id: Long): F[Option[Book]] =
    books.get(id) match {
      case Some(book) => book.some.pure[F]
      case None => none[Book].pure[F]
    }

  override def update(book: Book): F[Option[Book]] = {
    book.id.traverse { id =>
      books.update(id, book)
      book.pure[F]
    }
  }

  override def findByTitleAndAuthor(title: String, author: String): F[List[Book]] =
    books.values.filter(b => b.title == title && b.author == author).toList.pure[F]

  override def list(): F[List[Book]] =
    books.values.toList.pure[F]
}

object InMemBookRepositoryInterpreter {
  def apply[F[_] : Applicative]: InMemBookRepositoryInterpreter[F] = new InMemBookRepositoryInterpreter[F]()
}