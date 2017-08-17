package clwater.androiddashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
<<<<<<< HEAD

=======
>>>>>>> 620a1994bf312b65820e200a089117b899e2baf9
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        final DashBoard d = (DashBoard) findViewById(R.id.dash);
=======
        final DashBoard d = findViewById(R.id.dash);
>>>>>>> 620a1994bf312b65820e200a089117b899e2baf9


        findViewById(R.id.rand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = 150;
                int min = 1;
                Random random = new Random();
                int p = random.nextInt(max) % (max - min + 1) + min;
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