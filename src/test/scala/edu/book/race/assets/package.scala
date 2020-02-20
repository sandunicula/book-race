package edu.book.race

import java.time.OffsetDateTime

import edu.book.race.domain.books.Book

package object assets {
  val now: OffsetDateTime = OffsetDateTime.now()
  val testBook = Book(Some(1), "test", "author", now)
}