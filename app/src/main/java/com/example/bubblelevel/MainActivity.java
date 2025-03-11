package com.example.bubblelevel;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor accelerometer;

    SensorEventListener accelerometerEventListener;
    TextView levelConfirm;
    ProgressBar tiltProgressBarR;
    ProgressBar tiltProgressBarL;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accelerometerEventListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelerometerEventListener);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        levelConfirm=findViewById(R.id.levelConfirm);
        tiltProgressBarR=findViewById(R.id.levelProgressR);
        tiltProgressBarL=findViewById(R.id.levelProgressL);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelerometerEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float tiltX = event.values[0]; // Left-Right Tilt
                int progressR = (int) (0 - (tiltX * 10));
                progressR = Math.max(0, Math.min(progressR, 100));

                int progressL = (int) (0 + (tiltX * 10));
                progressL = Math.max(0, Math.min(progressL, 100));

                tiltProgressBarR.setProgress(progressR);
                tiltProgressBarL.setProgress(progressL);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
}