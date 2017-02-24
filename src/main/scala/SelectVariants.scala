import java.io.{BufferedInputStream, FileInputStream, PrintWriter}
import java.util.zip.GZIPInputStream

object SelectVariants extends App {
  val folder = args(0)
  //val folder = "src/main/resources/"
  val listSource = io.Source.fromFile(folder + "variant.list.txt")
  var prev = 0
  val chromosomes = listSource.getLines().drop(1).map(_.split("\t").map(_.trim)).
    map(line => (line(0).toInt, line(1).toInt)).toSet

  val writer = new PrintWriter(folder + "1000G.variants.selected.vcf") {
    val headers = io.Source.fromInputStream(
      gis(folder + s"ALL.chr1.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz")).getLines().
      takeWhile(_.startsWith("#"))
    headers.foreach(line => write(line + "\n"))

    (1 to 22 map ("chr" + _)).par.foreach(chr => {
      val genSource = io.Source.fromInputStream(
        gis(folder + s"ALL.${chr}.phase3_shapeit2_mvncall_integrated_v5a.20130502.genotypes.vcf.gz"))

      genSource.getLines.dropWhile(_.startsWith("#")).foreach(line => {
        val cols = line.split("\t").map(_.trim)
        if (chromosomes.contains((cols(0).toInt, cols(1).toInt)))
          write(line + "\n")
      })
    })
    close
  }

  def gis(s: String) = new GZIPInputStream(new BufferedInputStream(new FileInputStream(s)))

  def parse(file:String): List[Map[String,Any]] = {
    val bufferedSource = io.Source.fromFile(file)
    try {
      bufferedSource.getLines().map(line => {
        val cols = line.split(",").map(_.trim)
        Map(
          "name" -> cols(0),
          "age" -> cols(1),
          "job" -> cols(2)
        )
      }).toList
    } finally
      bufferedSource.close
  }
}
