package fi.jihartik.androidtest

import org.specs.Specification
import org.apache.commons.io.IOUtils
import android.location.Address
import org.specs.mock.EasyMock

object TimetableParserSpecs extends Specification with ExceptionHandling with EasyMock {

  "Any parser" should {
    "parse station" in {
      new TimetableParser().parse(getInput, addressIn("Leppävaara")).get.station.name mustEqual "Leppävaara"
    }
    "parse correct amount of rows" in {
      lpvRows.size mustEqual 7
    }
    "parse correct trains" in {
      lpvRows.map(_.train) must containInOrder(List("A", "S", "E", "A", "U", "A", "S"))
    }
    "parse correct times" in {
      lpvRows.map(_.time) must containInOrder(List("21:50", "21:55", "22:10", "22:12", "22:25", "22:42", "22:55"))
    }
    "parse correct destination" in {
      lpvRows.forall(_.destination == "Helsinki")
    }
    "return empty list if source has no trains" in {
      new TimetableParser().parse(getInput("no_trains_timetable.html"), addressIn("Espoo")).get.rows mustEqual Nil
    }
    "return only trains to Leppävaara at Helsinki" in {
      val rows = new TimetableParser().parse(getInput("hkl_timetable.html"), addressIn("Helsinki")).get.rows
      rows.size mustEqual 7
      rows.map(_.train) must containInOrder(List("A", "Y", "S", "L", "E", "A", "U"))
    }
  }

  def lpvRows = new TimetableParser().parse(getInput, addressIn("Espoo")).get.rows
  def getInput : String = getInput("lpv_timetable.html")
  def getInput(filename: String) = IOUtils.toString(getClass.getResourceAsStream(filename), "UTF-8")
  def addressIn(locality: String) = {
    val addr = mock[Address]
    expect { addr.getLocality stubReturns locality }
    addr
  }
}
