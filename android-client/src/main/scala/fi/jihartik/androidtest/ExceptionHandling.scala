package fi.jihartik.androidtest

import android.util.Log


trait ExceptionHandling {
  implicit def intToRepeater(i: Int) = new Repeater(i)

  def withExceptionHandling[T](logMessage: String)(func: => Option[T]) = {
    try {
      func
    } catch {
      case e => {
        Log.d("JunaMaatti", logMessage, e)
        None
      }
    }
  }

  class Repeater(repeatCount: Int) {
    def triesWithExceptionHandling[T](logMessage: String)(func: => Option[T]) = {
      tryNTimes(logMessage, func, repeatCount, repeatCount)
    }

    private def tryNTimes[T](logMessage: String, func: => Option[T], repeatCount: Int, timesLeft: Int) : Option[T] = {
      timesLeft match {
        case 0 => {
          Log.d("JunaMaatti", "Tried " + repeatCount + " times but still failed. Returning None.")
          None
        }
        case _ => {
          runOnce(logMessage, func) match {
            case Some(x) => x
            case None => tryNTimes(logMessage, func, repeatCount, timesLeft - 1)
          }
        }
      }
    }

    private def runOnce[T](logMessage: String, func: => Option[T]) = {
      try {
        Some(func)
      } catch {
        case e => {
          Log.d("JunaMaatti", logMessage, e)
          None
        }
      }
    }
  }
}
