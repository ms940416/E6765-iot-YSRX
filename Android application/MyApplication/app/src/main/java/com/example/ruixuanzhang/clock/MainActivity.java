package com.example.ruixuanzhang.clock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    private Button hear;
    private Button clock;
    private Button record;
    private Button gotemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hear=(Button)findViewById(R.id.hear);
        hear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),Hear.class);
                startActivity(intent);
            }
        });
        clock=(Button)findViewById(R.id.clockbutton);
        clock.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),Clock.class);
                startActivity(intent);
            }
        });
        record=(Button)findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),Record.class);
                startActivity(intent);
            }
        });
        gotemp=(Button)findViewById(R.id.GoTemp);
        gotemp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(), Temp.class);
                startActivity(intent);
            }
        });

    }
}
