val example = "´`:->>....xxxx..****..****..****.XX."
val example2 = "´`:===>...xxxx..****..****..****.@@."

val examples = List(example, example2)

type Path = String
case class Drill(power: Int, speed: Int) 
object Drill {
    val drillr = """´`:(-+|=+)(>+)""".r

    def apply(s: String) = s match {
        case drillr(arm, drill) => {
            val power = arm.head match {
                case '-' => 1
                case '=' => 2
            }
            val speed = drill.length
            new Drill(power, speed)
        }
    }
}

def parse(s: String): (Drill, Path) = {
    val (drill, path) = s.splitAt(s.lastIndexOf('>') + 1)
    (Drill(drill), path)
}



examples.map(parse)