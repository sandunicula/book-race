package edu.book.race.domain.books

trait BookRepositoryAlgebra[F[_]] {

  def create(book: Book): F[Book]

  def get(id: Long): F[Option[Book]]

  def update(book: Book): F[Option[Book]]

  def findByTitleAndAuthor(title: String, author: String): F[List[Book]]

  def list(): F[List[Book]]
}
