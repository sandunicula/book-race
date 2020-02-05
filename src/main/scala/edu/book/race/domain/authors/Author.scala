package edu.book.race.domain.authors

import java.util.Optional

case class Author(
                 id: Optional[Long],
                 firstName: String,
                 lastName: String
                 )
