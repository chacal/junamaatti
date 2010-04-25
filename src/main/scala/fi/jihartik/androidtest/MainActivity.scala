package fi.jihartik.androidtest

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity extends Activity {
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }
}
