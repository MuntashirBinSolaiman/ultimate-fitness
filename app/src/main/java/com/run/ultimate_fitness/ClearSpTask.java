package com.run.ultimate_fitness;

import android.os.AsyncTask;

public class ClearSpTask extends AsyncTask<Void, Void, Void> {
    public interface AsynResponse {
        void processFinish(Boolean output);
    }

    AsynResponse asynResponse = null;

    //This class performs asynchronous tasks in this case getting camera image
    public ClearSpTask(AsynResponse asynResponse) {
        this.asynResponse = asynResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //showProgressDialog();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //cleardata();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //hideProgressDialog();
        asynResponse.processFinish(true);
    }
}

