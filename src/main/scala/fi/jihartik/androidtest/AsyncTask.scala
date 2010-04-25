package fi.jihartik.androidtest

class AsyncTask[Result](work: () => Result, callback: (Result) => Unit) {
  def this(work: () => Result) = this(work, (Unit) => {})

  def doInBackground {
    new NonVarargsAsyncTask[Function0[Result], Unit, Result] {
      def doInBackground(workInTask: () => Result) = workInTask()
      override def onPostExecute(result: Result) = callback(result)
    }.execute(work)
  }
}

object AsyncTask {
  implicit def function2AsyncTask[Result](f: () => Result) : AsyncTask[Result] = new AsyncTask(f)
  def apply[Result](work: => Result) = {
    val func = () => { work }
    func.doInBackground
  }
  def apply[Result](work: => Result, callback: (Result) => Unit) = {
    val workFunc = () => { work }
    new AsyncTask(workFunc, callback).doInBackground
  }
  def apply(work: => Unit, callback: => Unit) = {
    val workFunc = () => { work }
    val callbackFunc = (res : Unit) => { callback }
    new AsyncTask(workFunc, callbackFunc).doInBackground
  }
}
