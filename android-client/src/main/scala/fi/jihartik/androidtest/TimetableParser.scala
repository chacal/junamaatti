package fi.jihartik.androidtest

import util.matching.Regex

class TimetableParser {
  def parse(input: String) = {
    val stationName = stationRegex.findFirstMatchIn(input).map(_.group(1)).getOrElse("N/A")
    Timetable(Station(stationName))
  }

  val stationRegex = new Regex("""span class="departures_title">(.*)</span>""")
}


case class Station(name: String) {
  override def toString = name
}
case class Timetable(station: Station)