import $ivy.`co.fs2::fs2-core:3.6.1`
import $ivy.`co.fs2::fs2-io:3.6.1`
import $ivy.`org.typelevel::cats-effect:3.4.5`

import scala.concurrent.ExecutionContext.Implicits.global

import fs2._
import fs2.timeseries.TimeStamped
import cats.effect.{IO}
import java.time.Instant
import scala.concurrent.duration._
import scala.util.Random

val duration = 4.second

def constantStream(value: Int, duration: FiniteDuration): Stream[IO, Int] =
  Stream.awakeEvery[IO](duration).as(value)

def randomStream(mean: Double, deviation: Double, duration: FiniteDuration): Stream[IO, Double] =
  Stream.awakeEvery[IO](duration).map(_ => mean + (deviation * Random.nextGaussian()))

def monotonStream(start: Int, step: Int, duration: FiniteDuration): Stream[IO, Int] = Stream.awakeEvery[IO](duration).scan(start){
    case (x, _) => x + step
}

def monotonCycleStream(
    start: Int,
    stop: Int,
    step: Int,
    duration: FiniteDuration
): Stream[IO, Int] = Stream
  .awakeEvery[IO](duration)
  .scan(start) {
    case (currentValue, _) => {
      val t = currentValue + step
      if (t > stop) start else t
    }
  }
  .repeat

// Sinus stream
def sinusStream(
    amplitude: Double = 1,
    period: Double = 10,
    phaseShift: Double = 0,
    verticalShift: Double = 0,
    duration: FiniteDuration = 1.second
) = monotonCycleStream(start = 0, period.toInt, 1, duration)
  .map { t =>
    amplitude * Math.sin(
      (2 * Math.PI * t) / period + phaseShift
    ) + verticalShift
  }

import cats.effect.unsafe.implicits.global
extension[A](io: IO[A]) {
  def debug: IO[A] = io.map { a =>
    println(s"Value is: $a")
    a
  }
}

constantStream(1, 1.second).take(5).compile.toList.unsafeRunSync()
randomStream(1, 3, 1.second).take(5).compile.toList.unsafeRunSync()
monotonStream(0, 3, 1.second).take(5).compile.toList.unsafeRunSync()
monotonCycleStream(0, 3, 1, 1.second).take(5).compile.toList.unsafeRunSync()
sinusStream(1, 3, 0, 0, 1.millisecond).take(5).compile.toList.unsafeRunSync()


// def timestampedPipe: Pipe[Pure, Int,TimeStamped[Int]] = _.map(TimeStamped.realTime(_))

//monotonStream(0, 3, 1.second).through(timestampedPipe).take(5).compile.toList.unsafeRunSync()