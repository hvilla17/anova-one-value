package com.tesi.anova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
    ANOVA - Analysis of variance

    Authors:
        Dr. Hector Antonio Villa-Martinez
        Dr. Francisco Javier Tapia-Moreno

        Departamento de Matemáticas
        Universidad de Sonora
        (c) 2021
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void oneWayANOVA(View view)
    {
        Intent intent = new Intent(this, OneWayOneActivity.class);
        startActivity(intent);
    }

    public void twoWayANOVAOne(View view)
    {
        Intent intent = new Intent(this, TwoWayOneActivity.class);
        startActivity(intent);
    }
}