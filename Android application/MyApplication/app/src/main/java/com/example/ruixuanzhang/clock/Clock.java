package com.example.ruixuanzhang.clock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Clock extends AppCompatActivity {

    RelativeLayout rl;
    AnalogClock clk;
    private TextView remaining_time;
    private Button back;
    private String remaining="";
    private int response;
    private String answer;
    private Button sleepbutton;
    private Calendar calendar;
    private int cHour;
    private int cMinute;
    private int getuptime=7;
    static boolean Getup=false;
    private String remain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        calendar=Calendar.getInstance();
        cHour=calendar.get(Calendar.HOUR);
        cMinute=calendar.get(Calendar.MINUTE);
        int am_pm=calendar.get(Calendar.AM_PM);
        if(am_pm==1)
            cHour+=12;
        Log.d("cHour",String.valueOf(cHour));
        Log.d("ampm",String.valueOf(am_pm));
        Log.d("cMinute",String.valueOf(cMinute));
        remaining_time=(TextView)findViewById(R.id.RemainingTime);
        final Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted() && ! Getup) {
                   Log.d("Flag",String.valueOf(Getup));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new sleep().execute();
                            }
                        });
                        remain=remaining_time.getText().toString();
                        Log.d("Remaining_time in run",remain);
                        if(remain.equals("0")) {
                            Log.d("remain==0","remain==0");
                            Getup = true;
                        }
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        rl=(RelativeLayout)findViewById(R.id.activity_clock);
        clk=new AnalogClock(Clock.this);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int) LayoutParams.WRAP_CONTENT,(int)LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        clk.setLayoutParams(params);
        rl.addView(clk);
        clk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Toast.makeText(getBaseContext(),"Clock",Toast.LENGTH_SHORT).show();
            }
        });
        back=(Button)findViewById(R.id.clockbackbutton);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        remaining=getre(cHour,cMinute,getuptime);
        remaining_time.setText("00:00");
        sleepbutton=(Button)findViewById(R.id.sleep);
        sleepbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

// /                Getup=false;
//                remaining_time.setText("00:00");
//                if (t.getState() == Thread.State.NEW) {
//                    t.start();
//                }
                remaining_time.setText("00:00");
                Getup=false;
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        try {
                            while (!isInterrupted() && ! Getup) {
                                Log.d("Flag",String.valueOf(Getup));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new sleep().execute();
                                    }
                                });
                                remain=remaining_time.getText().toString();
                                Log.d("Remaining_time in run",remain);
                                if(remain.equals("0")) {
                                    Log.d("remain==0","remain==0");
                                    Getup = true;
                                }
                                Thread.sleep(60000);
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                };
                t.start();
            }
        });

    }
    private String getre(int now,int min,int wakeup)
    {
        int remain=0;
        int remainmin=0;
        if(now<wakeup)
            remain=wakeup-now-1;
        else
            remain=wakeup+24-now+1;
        remainmin=60-min;
        if(remainmin==60)
        {
            remainmin=00;
            remain+=1;
        }
        String result=String.valueOf(remain)+"h"+String.valueOf(remainmin)+"min";
        return result;
    }
    private synchronized void stopThread(Thread theThread)
    {
        if (theThread != null)
        {
            theThread = null;
        }
    }
    private class sleep extends AsyncTask<Integer,Void,String>
    {
        private String answer="";
        protected void onPreExecute()
        {
            //reminder.setText("hi");
        }
        protected String doInBackground(Integer... Params)
        {

//            C_time=Params[0];
//            O_time=Params[1];
//            datetime=Params[2];
            try
            {
                String TAG="TAG";
                URL url=new URL("http://160.39.204.200:2000/polls/sleep");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                JSONObject temp=new JSONObject();
                String t;
                temp.put("command","sleep");
                t=temp.toString();
                Log.d("temp.toString",t);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Length", ""+ t.length() );
                con.setRequestMethod("POST");
                OutputStreamWriter w = new OutputStreamWriter(con.getOutputStream());
                Log.d("Before write","Before write");
                w.write(temp.toString());
                w.flush();
                w.close();
                Log.d(TAG,"close");
                response = con.getResponseCode();
                System.out.println("response:"+response);
                if(response==200)
                {
                    String line;
                    BufferedReader reader = new BufferedReader((new InputStreamReader(con.getInputStream())));
                    while ((line=reader.readLine())!=null)
                    {
                        answer+=line;
                    }
                    Log.d("reponse","200");
                }
                else{
                    Log.d("reponse","others");
                    answer="";
                }
                Log.d("Return answer",answer);
//                if(answer=="")
//                    return "No data";
//                else {
//                    return "good";
//                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            if (answer.equals("0"))
                Getup=true;
            return answer;
        }
        protected void onPostExecute(String answer)
        {
            Log.d("onPostExecute",answer);
            remaining_time.setText(answer);
        }
    }
}
