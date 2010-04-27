package fi.jihartik.androidtest

import java.io.InputStream
import java.net.URL
import org.apache.commons.io.IOUtils

trait HttpUtils {
  def httpGet(url: String) = withStream(new URL(url).openStream()) { IOUtils.toString(_, "UTF-8") }

  private def withStream[T](input: InputStream)(func: (InputStream) => T) = {
    try {
      func(input)
    } catch {
      case e => throw e
    } finally {
      input.close
    }
  }
}