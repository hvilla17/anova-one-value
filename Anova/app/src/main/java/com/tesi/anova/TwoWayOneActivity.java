package com.tesi.anova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TwoWayOneActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_way_one);

        Spinner spinner = findViewById(R.id.significance);
        spinner.setSelection(3);    // 0.05 by default
    }

    public void computeTwoWayOne(View view)
    {
        double[][] data;
        double[] sumRows, sumCols;

        // Read data
        EditText editText = findViewById(R.id.data);
        String text = editText.getText().toString().trim();
        String[] numbers = text.split("\\s+");
        int numElements = numbers.length;
        int numRows = Etc.countLines1(text);
        int numCols = numElements / numRows;
        data = new double[numRows][numCols];
        sumRows = new double[numRows];
        sumCols = new double[numCols];

        if ((numElements % numRows) != 0) {
            Toast.makeText(this, "Size of rows do not match", Toast.LENGTH_LONG).show();
        }

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                try {
                    data[row][col] = Double.parseDouble(numbers[row * numCols + col]);
                }
                catch (Exception e) {
                    data[row][col] = 0;
                }
            }
        }

        // Sum by rows and cols
        // Sum of all squares
        double u = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                sumRows[row] += data[row][col];
                sumCols[col] += data[row][col];
                u += data[row][col] * data[row][col];
            }
        }

        // Total sum of rows
        double g_mean = 0;
        for (double d : sumRows) {
            g_mean += d;
        }

        double g = (g_mean * g_mean) / numElements;
        double q = u - g;

        // Sum of squares
        double q1 = 0;
        for (double d : sumRows) {
            q1 += d * d;
        }
        q1 = q1 / numCols - g;

        double q2 = 0;
        for (double d : sumCols) {
            q2 += d * d;
        }
        q2 = q2 / numRows - g;

        double q3 = q - q1 - q2;

        // Mean squares
        double s1 = q1 / (numRows - 1);
        double s2 = q2 / (numCols - 1);
        double s3 = q3 / ((numRows - 1) * (numCols - 1));

        // Quotients
        double v1 = s1 / s3;
        double v2 = s2 / s3;

        // Level of significance
        double alpha = 0;
        Spinner spinner = findViewById(R.id.significance);
        switch (spinner.getSelectedItemPosition()) {

            case 0:
                alpha = 0.005;
                break;

            case 1:
                alpha = 0.01;
                break;

            case 2:
                alpha = 0.02;
                break;

            case 3:
                alpha = 0.05;
                break;

            case 4:
                alpha = 0.1;
                break;
        }

        double f_table1 = FDistribution.ftable(alpha, numRows - 1, (numRows - 1) * (numCols - 1));
        double f_table2 = FDistribution.ftable(alpha, numCols - 1, (numRows - 1) * (numCols - 1));

        // Print results
        int digits = 4;
        StringBuilder builder = new StringBuilder();
        builder.append("Sum of squares\n");
        builder.append("q1: ").append(Etc.format(q1, digits)).append("\n");
        builder.append("q2: ").append(Etc.format(q2, digits)).append("\n");
        builder.append("q3: ").append(Etc.format(q3, digits)).append("\n\n");
        builder.append("Mean squares\n");
        builder.append("s1: ").append(Etc.format(s1, digits)).append("\n");
        builder.append("s2: ").append(Etc.format(s2, digits)).append("\n");
        builder.append("s3: ").append(Etc.format(s3, digits)).append("\n\n");
        builder.append("Quotients\n");
        builder.append("v1: ").append(Etc.format(v1, digits)).append("\n");
        builder.append("v2: ").append(Etc.format(v2, digits)).append("\n\n");
        builder.append("F from table\n");
        builder.append("f1: ").append(Etc.format(f_table1, digits)).append("\n");
        builder.append("f2: ").append(Etc.format(f_table2, digits)).append("\n\n");
        builder.append("Factor 1 DOES ");
        if (v1 < f_table1) {
            builder.append("NOT ");
        }
        builder.append("have an effect\n");
        builder.append("Factor 2 DOES ");
        if (v2 < f_table2) {
            builder.append("NOT ");
        }
        builder.append("have an effect\n\n");

        editText = findViewById(R.id.results);
        editText.setText(builder.toString());
    }
}