package com.example.ruixuanzhang.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Hear extends Activity {

    private TextView clearday;
    private TextView otherday;
    private EditText cleartime;
    private EditText othertime;
    private Button submit;
    private Button returnhear;
    private Button clockbutton;
    private String datetime="";
    String clear="Clear Day";
    String other="Wet and Cloudy Day";
    String C_time="";
    String O_time="";
    private int response;
    private String answer="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear);
        clearday=(TextView)findViewById(R.id.clearday);
        clearday.setText(clear);
        otherday=(TextView)findViewById(R.id.otherday);
        otherday.setText(other);
        cleartime=(EditText)findViewById(R.id.cleardaytime);
        othertime=(EditText)findViewById(R.id.otherdaytime);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                C_time=cleartime.getText().toString();
                O_time=othertime.getText().toString();
                Log.d("cleartime",C_time);
                Log.d("othertime",O_time);
                new PostTime().execute(C_time,O_time);
                String command="";
                command="If tomorrow is a clear day, wake you up at"+String.valueOf(C_time)+" else, wake you up at"+String.valueOf(O_time);
                Toast.makeText(getBaseContext(),command,Toast.LENGTH_SHORT).show();

            }
        });
        returnhear=(Button)findViewById(R.id.returnhear);
        returnhear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        clockbutton=(Button)findViewById(R.id.clockbutton);
        clockbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),Clock.class);
                startActivity(intent);
            }
        });
    }
    private class PostTime extends AsyncTask<String,Void,Void>
    {
        protected void onPreExecute()
        {
            //reminder.setText("hi");
        }
        protected Void doInBackground(String... Params)
        {
            answer="";
            try
            {
                String TAG="TAG";
                URL url=new URL("http://160.39.204.200:2000/polls/lighton");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                JSONObject temp=new JSONObject();
                String t;
                Log.d(TAG,"json");
                Log.d("C_time",C_time);
                Log.d("O_time",O_time);
                temp.put("0",C_time);
                temp.put("1",O_time);
                t=temp.toString();
                Log.d("temp.toString",t);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Length", ""+ t.length() );
                con.setRequestMethod("POST");
                OutputStreamWriter w = new OutputStreamWriter(con.getOutputStream());
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
//            if(answer=="")
//                return "No data";
//            else {
//                return "good";
//            }
            return null;
        }
        protected void onPostExecute()
        {
            //reminder.setText("hi");
        }
    }
}
