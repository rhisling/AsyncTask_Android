package com.example.aravi.thsensoraynctask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private EditText temperatureField;
    private EditText humidityField;
    private EditText activityField;
    private EditText sensorReadingsField;
    private TextView outputField;
    private Random randomGenerator;
    private Button generateButton;
    private Button cancelButton;
    int noofsensorreadings;
    int temperaturethreshold;
    int humiditythreshold;
    int actviitythreshold;
    ProgressBar progressBar;
    Taskexecute tasky;
    private static final String TAG = "MainActivity";
    AlertDialog dialog;
    private static final int DIALOG_SHOW_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomGenerator = new Random();
        dialog = new SpotsDialog(this);
        temperatureField = (EditText) findViewById(R.id.temperatureFieldID);
        humidityField = (EditText) findViewById(R.id.humidityFieldID);
        activityField = (EditText) findViewById(R.id.activityFieldId);
        sensorReadingsField = (EditText) findViewById(R.id.sensorreadingsId);
        outputField = (TextView) findViewById(R.id.outputFieldId);
        generateButton = (Button) findViewById(R.id.generateButtonId);
        progressBar = (ProgressBar) findViewById(R.id.progressBarId);
        cancelButton = (Button) findViewById(R.id.cancelButtonId);
        progressBar.setVisibility(View.GONE);
        outputField.setMovementMethod(new ScrollingMovementMethod());


        //  Toast.makeText(this,"SR"+sensorReadingsField.getText().toString(),Toast.LENGTH_SHORT);

        View.OnClickListener generateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noofsensorreadings = Integer.parseInt(sensorReadingsField.getText().toString());
                temperaturethreshold = Integer.parseInt(temperatureField.getText().toString());
                humiditythreshold = Integer.parseInt(humidityField.getText().toString());
                actviitythreshold = Integer.parseInt(activityField.getText().toString());
                outputField.setText("");
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                progressBar.setMax(noofsensorreadings);
                tasky = new Taskexecute(MainActivity.this);
                tasky.setProgressBar(progressBar);
                tasky.execute(noofsensorreadings, temperaturethreshold, humiditythreshold, actviitythreshold);
            }
        };

        View.OnClickListener cancelButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tasky.cancel(true);

            }
        };

        generateButton.setOnClickListener(generateButtonListener);
        cancelButton.setOnClickListener(cancelButtonListener);
    }


    class Taskexecute extends AsyncTask<Integer, Integer, String> {

        private ProgressBar progressBar;
        private Random randomGenerator;
        private TextView outputField;
        private Activity mActivity;
        private int noofsensorreadings;
        private int temperaturethreshold;
        private int humiditythreshold;
        private int actviitythreshold;


        private static final String TAG = "taskexecute";

        public Taskexecute(Activity activity) {
            mActivity = activity;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //txt.setText("Task Starting...");
            //Toast.makeText(getApplicationContext(),"Started", Toast.LENGTH_SHORT).show();
        }


        protected void setProgressBar(ProgressBar bar) {
            progressBar = bar;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(),"Completed", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            Log.d(TAG, "onProgressUpdate:" + progress[0]);
            outputField = mActivity.findViewById(R.id.outputFieldId);
            progressBar.setProgress(progress[0]);
            //outputField.append("Output:" + count + "\n");
            outputField.append("Temperature:" + progress[1] + "\n");
            outputField.append("Humidity:" + progress[2] + "\n");
            outputField.append("Activity:" + progress[3] + "\n");
            dialog.show();

        }

        @Override
        protected String doInBackground(final Integer... params) {
            Log.d(TAG, "doInBackground: In");
            noofsensorreadings = params[0];
            temperaturethreshold = params[1];
            humiditythreshold = params[2];
            actviitythreshold = params[3];
            randomGenerator = new Random();

            for (int i = 0; i <= noofsensorreadings; i++) {
                try {
                    int temperature = randomGenerator.nextInt(15) + temperaturethreshold;
                    int humidity = randomGenerator.nextInt(40) + humiditythreshold;
                    int activity = randomGenerator.nextInt(20) + actviitythreshold;
                    //updateOutputField(i, temperature, humidity, activity);
                    publishProgress(i,temperature,humidity,activity);
                    if(isCancelled()){
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    void updateOutputField(final int count, final int temperature_t, final int humidity_h, final int activity_a) {
        Log.d(TAG, "updateOutputField: IN");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputField.append("Output:" + count + "\n");
                outputField.append("Temperature:" + temperature_t + "\n");
                outputField.append("Humidity:" + humidity_h + "\n");
                outputField.append("Activity:" + activity_a + "\n");
            }
        });
        Log.d(TAG, "updateOutputField: OUT");

    }


}

