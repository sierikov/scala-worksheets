
import $ivy.`co.fs2::fs2-core:3.6.1`
import $ivy.`co.fs2::fs2-io:3.6.1`
import $ivy.`org.typelevel::cats-effect:3.4.5` 

import scala.concurrent.ExecutionContext.Implicits.global

import fs2._
import cats.effect.{IO, Timer}
import scala.concurrent.duration._

def putStrLn(s: String): IO[Unit] = IO(println(s))


implicit val timer: Timer[IO] = IO.timer(scala.concurrent.ExecutionContext.global)

// Constant value stream
def constantStream(value: Int, duration: FiniteDuration) = Stream.awakeEvery[IO](duration).constant(value)

def monotonStream(start: Int, step: Int, duration: FiniteDuration) = Stream.awakeEvery[IO](duration).iterate(start)(_ + step)

def s = (constantStream(4).take(5) ++ monotonStream(5, 1).take(5)).repeat


constantStream(5).take(5).toList
monotonStream(10, 1).take(5).toList

s.take(15).toList