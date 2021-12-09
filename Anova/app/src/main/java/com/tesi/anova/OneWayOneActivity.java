package com.tesi.anova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class OneWayOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_way_one);

        Spinner spinner = findViewById(R.id.significance);
        spinner.setSelection(3);    // 0.05 by default
    }

    public void computeOneWay(View view)
    {
        double[][] data;
        double[] groupMeans;

        // Read data
        EditText editText = findViewById(R.id.data);
        String text = editText.getText().toString().trim();
        String[] numbers = text.split("\\s+");
        int numElements = numbers.length;
        int numRows = Etc.countLines1(text);
        int numCols = numElements / numRows;
        data = new double[numRows][numCols];
        groupMeans = new double[numRows];

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

        // Groups and sample means
        double sampleSum = 0;
        for (int row = 0; row < numRows; row++) {

            double sum = 0;
            for (int col = 0; col < numCols; col++) {
                sum += data[row][col];
                sampleSum += data[row][col];
            }

            groupMeans[row] = sum / numCols;
        }
        double sampleMean = sampleSum / numElements;

        // Square sum of mean groups
        double q1 = 0;
        for (double groupMean : groupMeans) {
            q1 += numCols * (groupMean - sampleMean) * (groupMean - sampleMean);
        }

        // Square sum inside groups
        double q2 = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                q2 += (data[row][col] - groupMeans[row]) * (data[row][col] - groupMeans[row]);
            }
        }

        // Quotient
        double MST = q1 / (numRows - 1);
        double MSE = q2 / (numElements - numRows);
        double F = MST / MSE;

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

        double f_table = FDistribution.ftable(alpha, numRows - 1, numElements - numRows);

        // Print results
        StringBuilder builder = new StringBuilder();
        int digits = 4;
        builder.append("Group means:");
        for (double mean : groupMeans) {
            builder.append(" ").append(Etc.format(mean, digits));
        }
        builder.append("\nSample mean: ").append(Etc.format(sampleMean, digits));
        builder.append("\n\nDegrees of Freedom");
        builder.append("\nTreatments: ").append(numRows - 1);
        builder.append("\nError: ").append(numElements - numRows);
        builder.append("\nTotal : ").append(numElements - 1);
        builder.append("\n\nSquare Sums");
        builder.append("\nTreatments (SST): ").append(Etc.format(q1, digits));
        builder.append("\nError (SSE): ").append(Etc.format(q2, digits));
        builder.append("\n\nMean Squares");
        builder.append("\nTreatments (MST): ").append(Etc.format(MST, digits));
        builder.append("\nError (MSE): ").append(Etc.format(MSE, digits));
        builder.append("\n\nF: ").append(Etc.format(F, digits));
        builder.append("\nLevel of significance: ").append(alpha);
        builder.append("\nF(").append(alpha).append(", ").append(numRows - 1).append(", ")
                .append(numElements - numRows).append("): ")
                .append(Etc.format(f_table, digits));
        builder.append("\n\nH0 hypothesis (all group means are equal) is ");
        if (F < f_table) {
            builder.append("NOT rejected");
        }
        else {
            builder.append("rejected");
        }
        builder.append("\n");

        editText = findViewById(R.id.results);
        editText.setText(builder.toString());
    }
}