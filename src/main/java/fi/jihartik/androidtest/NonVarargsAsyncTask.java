package fi.jihartik.androidtest;

public abstract class NonVarargsAsyncTask<Params, Progress, Result> extends android.os.AsyncTask<Params, Progress, Result>{
  @Override
  protected Result doInBackground(Params... params) {
    return doInBackground(params[0]);
  }

	abstract protected Result doInBackground(Params f);
}