package fi.jihartik.androidtest

import android.app.Activity
import android.os.Bundle

class MainActivity extends Activity with HttpUtils {
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }
}
