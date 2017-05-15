package com.example.ruixuanzhang.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Record extends Activity {

    Button RecordReturn;
    EditText address;
    Button Addsubmit;
    String HomeAdd;
    private String answer;
    private int response;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        RecordReturn=(Button)findViewById(R.id.recordreturn);
        RecordReturn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        address=(EditText)findViewById(R.id.HomeAddress);
        Addsubmit=(Button)findViewById(R.id.addsubmit);
        Addsubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                HomeAdd=address.getText().toString();
                new PostAdd().execute(HomeAdd);
                Toast.makeText(getBaseContext(),"Address Submitted!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class PostAdd extends AsyncTask<String,Void,Void>
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
                URL url=new URL("http://160.39.204.200:2000/polls/address");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                JSONObject temp=new JSONObject();
                String t;
                Log.d(TAG,"json");
                temp.put("HomeAdd",HomeAdd);
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