package com.TourAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.TourAccount.R;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 19.09.14
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class TourActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour);

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(TourActivity.this, MainActivity.class);

                startActivity(i);  // Запускаем новую Активность.

                finish();  // Завершить текущую активность.
            }
        });

    }
}
