package fi.jihartik.androidtest

import org.specs.Specification

class HttpUtilsSpecs extends Specification {
  "Any" should {
    "fetch Google's main page" in {
      val test = new Object with HttpUtils
      test.httpGet("http://www.google.fi/") must startWith("<!doctype html>")
    }
  }
}