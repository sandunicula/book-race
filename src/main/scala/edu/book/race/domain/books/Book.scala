package edu.book.race.domain.books

case class Book(
                 id: Option[Long] = None,
                 title: String,
                 author: String
               )
