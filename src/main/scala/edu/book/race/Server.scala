package edu.book.race

import cats.effect.{ExitCode, IO, IOApp}

object Server extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO(println("Hello world!"))
    } yield ExitCode.Success
}
