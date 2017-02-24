import java.io.PrintWriter

  object ChangeGenotypes extends App {
  val input = args(0)
  val output = args(1)

  val writer = new PrintWriter(output) {
    val genSource = io.Source.fromFile(input)
    val lines = genSource.getLines()

    write(lines.next() + "\n")

    lines.foreach(line => {
      val columns = line.split(" ")

      write(columns.map(c => if(c == "2") "0" else if (c == "0") "2" else c).mkString(" ")+ "\n")
    })
    close
  }
}
