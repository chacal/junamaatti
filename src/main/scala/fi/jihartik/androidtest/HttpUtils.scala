package fi.jihartik.androidtest

import java.io.InputStream
import java.net.URL
import sun.misc.IOUtils


trait HttpUtils {
  def httpGet(url: String) = withStream(new URL(url).openStream()) { IOUtils.toString }

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