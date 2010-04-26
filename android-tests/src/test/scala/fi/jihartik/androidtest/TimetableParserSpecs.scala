package fi.jihartik.androidtest

import org.specs.Specification
import org.apache.commons.io.IOUtils

class TimetableParserSpecs extends Specification with ExceptionHandling {

  "Any parser" should {
    "parse station" in {
      new TimetableParser().parse(getInput).get.station.name mustEqual "Leppävaara/VR (E1058)"
    }
    "parse correct amount of rows" in {
      new TimetableParser().parse(getInput).get.rows.size mustEqual 10
    }
    "parse correct trains" in {
      val rows = new TimetableParser().parse(getInput).get.rows
      List("A", "U", "A", "S", "A", "U", "A", "S", "A", "Y").zip(rows).foreach(pair => pair._1 mustEqual pair._2.train)
    }
    "parse correct times" in {
      val rows = new TimetableParser().parse(getInput).get.rows
      List("15:12", "15:25", "15:42", "15:55", "16:12", "16:25", "16:42", "16:55", "17:12", "17:15").zip(rows).foreach(pair => pair._1 mustEqual pair._2.time)
    }
    "parse correct destination" in {
      val rows = new TimetableParser().parse(getInput).get.rows
      1.until(10).map(i => "Helsinki").toList.zip(rows).foreach(pair => pair._1 mustEqual pair._2.destination)
    }
  }

  def getInput : String = getInput("timetable.html")
  def getInput(filename: String) = IOUtils.toString(getClass.getResourceAsStream("timetable.html"), "ISO-8859-1")
}
