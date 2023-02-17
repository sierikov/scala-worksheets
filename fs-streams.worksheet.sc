
import $ivy.`co.fs2::fs2-core:3.6.1`
import $ivy.`co.fs2::fs2-io:3.6.1`
import $ivy.`org.typelevel::cats-effect:3.4.5` 

import scala.concurrent.ExecutionContext.Implicits.global

import fs2._
import fs2.timeseries.TimeStamped
import cats.effect.{IO}
import java.time.Instant
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

val value = 3
val duration = 4.second
def everyStream(duration: FiniteDuration): Stream[IO, Int] = Stream.awakeEvery[IO](duration).as(value)



import cats.effect.unsafe.implicits.global
extension [A](io: IO[A]) {
    def debug: IO[A] = io.map { a => 
        println(s"Value is: $a")
        a    
    }
}


def t = everyStream(5.second).map(TimeStamped.unsafeRealTime(_))

t.take(5).compile.toList.unsafeRunSync()

// Stream that emits a value every second

//TimeStamped values only contain the duration, so another class is needed to store the timestamp
// bad - to much rewriting
// case class TimeInstanced[+A](timestamp: Instant, value: A) extends Product with Serializable {
//     def map[B](f: A => B): TimeInstanced[B] = TimeInstanced(timestamp, f(value))
//     def mapTime(f: Instant => Instant): TimeInstanced[A] = TimeInstanced(f(timestamp), value)
// }

// bad - breaks referential transparency
// bad - exists FiniteDuration boundaries -- crashes
// val timeStampedStream: Stream[Pure, TimeStamped[Int]] = monotonStream(1, 1).map(TimeStamped(System.currentTimeMillis().second, _))

//def timeStampedStream: Stream[Pure, TimeStamped[Int]] = monotonStream(1,1).map(TimeStamped.realTime(_))



// def a = TimeStamped.realTime(1)


//timeStampedStream.take(5).toList