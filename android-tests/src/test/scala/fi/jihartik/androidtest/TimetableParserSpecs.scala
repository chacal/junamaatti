package fi.jihartik.androidtest

import org.specs.Specification
import org.apache.commons.io.IOUtils

class TimetableParserSpecs extends Specification {

  "Any parser" should {
    "parse station" in {
      new TimetableParser().parse(getInput) mustEqual Timetable(Station("Leppävaara/VR (E1058)"))
    }
  }

  def getInput : String = getInput("timetable.html")
  def getInput(filename: String) = IOUtils.toString(getClass.getResourceAsStream("timetable.html"), "ISO-8859-1")
}
