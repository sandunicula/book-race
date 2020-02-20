package edu.book.race.domain.books

import java.time.OffsetDateTime

case class Book(
                 id: Option[Long] = None,
                 title: String,
                 author: String,
                 publicationDate: OffsetDateTime
               )
