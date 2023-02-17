
import $ivy.`co.fs2::fs2-core:3.6.1`
import $ivy.`co.fs2::fs2-io:3.6.1`
import $ivy.`org.typelevel::cats-effect:3.4.5` 

import scala.concurrent.ExecutionContext.Implicits.global

import fs2._
import cats.effect.{IO}
import scala.concurrent.duration._

def putStrLn(s: String): IO[Unit] = IO(println(s))


// implicit val timer: Timer[IO] = IO.timer(scala.concurrent.ExecutionContext.global)

// Constant value stream
def constantStream(value: Int) = Stream.constant(value)

// Monotonically increasing stream
def monotonStream(start: Int, step: Int) = Stream.iterate(start)(_ + step)

// Combined stream
def combinedStream = (constantStream(4).take(5) ++ monotonStream(5, 1).take(5)).repeat


constantStream(5).take(5).toList
monotonStream(10, 1).take(5).toList

combinedStream.take(15).toList