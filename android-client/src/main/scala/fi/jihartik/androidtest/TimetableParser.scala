package fi.jihartik.androidtest

import util.matching.Regex
import java.util.Date
import android.location.Address

class TimetableParser {
  val trainsToLpv = List("Y", "S", "U", "L", "E", "A")
  val stationRegex = new Regex("""(?s)colspan="6">(.*), lähtevät junat""")
  val departingTrainsRegex = new Regex("""(?s)lähtevät junat(.*?)</table>""")
  val rowRegex = new Regex("""(?s)<tr.*?lang=fi">(.*?)</a>.*?border">(.*?)</td>.*?border">(.*?)</td>.*?</tr>""")

  private var cachedData : Option[Timetable] = None

  def parse(input: String, addr: Address) = {
    val stationName = stationRegex.findFirstMatchIn(input).map(_.group(1).trim).getOrElse("N/A")
    val departingTrainsText = departingTrainsRegex.findFirstIn(input)
    val rows = departingTrainsText.map { departing =>
      rowRegex.findAllIn(departing).matchData.map(m => TimetableRow(m.group(1), m.group(2), m.group(3))).toList
    }.getOrElse(Nil)
    cachedData = Some(Timetable(Station(stationName), filterByLocation(rows, addr)))
    cachedData
  }

  def getCachedData = cachedData

  def resolveStopId(addr: Address) = {
    isInEspoo(addr) match {
      case true => "LPV"  // Leppävaara
      case false => "HKI"  // Helsinki
    }
  }

  private def filterByLocation(rows: List[TimetableRow], addr: Address) = {
    isInEspoo(addr) match {
      case true => rows.filter(_.destination == "Helsinki")
      case false => rows.filter(row => trainsToLpv.contains(row.train))
    }
  }

  private def isInEspoo(address: Address) = address.getLocality.contains("Espoo")
}


case class Station(name: String) {
  override def toString = name
}
case class TimetableRow(train: String, time: String, destination: String)

case class Timetable(station: Station, rows: List[TimetableRow])