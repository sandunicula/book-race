package edu.book.race.domain

import edu.book.race.domain.books.Book

sealed trait ValidationError

case object BookNotFoundError extends ValidationError

case class BookAlreadyExistsError(book: Book) extends ValidationError
