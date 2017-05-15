package com.example.ruixuanzhang.clock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Temp extends AppCompatActivity {

    private Button tempreturn;
    private Button gettemp;
    private TextView TempText;
    private int response;
    private String answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        tempreturn=(Button)findViewById(R.id.tempreturn);
        tempreturn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        gettemp=(Button)findViewById(R.id.gettemp);
        gettemp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new getEnvir().execute();
            }
        });
        TempText=(TextView)findViewById(R.id.Feedback);
    }
    private class getEnvir extends AsyncTask<Void,Void,String>
    {
        protected void onPreExecute()
        {
            //reminder.setText("hi");
        }
        protected String doInBackground(Void... Params)
        {
            answer="";
            try
            {
                String TAG="TAG";
                URL url=new URL("http://160.39.204.200:2000/polls/environment");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                JSONObject temp=new JSONObject();
                String t="";
                Log.d(TAG,"json");
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
                return answer;
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
        protected void onPostExecute(String answer)
        {
            TempText.setText(answer);
        }
    }
}
