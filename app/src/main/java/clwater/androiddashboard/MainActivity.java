package clwater.androiddashboard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DashBoard d = findViewById(R.id.dash);
        d.setBackGroundColor(Color.WHITE);


        int width = this.getWindowManager().getDefaultDisplay().getWidth();

        d.setR(width / 2);
        d.setPointLength1(0.8f);


        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) d.getLayoutParams();
        ll.height = width / 2 / 4 * 5   ;
        d.setLayoutParams(ll);


//        d.cgangePer(0);

        findViewById(R.id.rand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = 150;
                int min = 1;
                Random random = new Random();
                int p = random.nextInt(max) % (max - min + 1) + min;
//                Log.d("gzb" , "p / 100f: " + p / 100f);
                d.cgangePer(p / 120f);
            }
        });

        findViewById(R.id.retu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cgangePer(0);
            }
        });
    }

}
