package com.tesi.anova;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Etc {
    // https://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string/50631407#50631407

    private static Pattern pattern = Pattern.compile("\r\n|\r|\n");

    public static int countLines1(String str)
    {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    public static int countLines2(String str)
    {
        Matcher m = pattern.matcher(str);

        int lines = 1;
        while (m.find()) {
            lines++;
        }

        return lines;
    }

    public static int countLines3(String str)
    {
        String separator = System.getProperty("line.separator");
        return separator == null ? -1 : str.split(separator).length;
    }

    public static int countLines4(String input) throws IOException
    {
        LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(input));
        lineNumberReader.skip(Long.MAX_VALUE);

        return lineNumberReader.getLineNumber() + 1;
    }

    public static String format(double x, int digits)
    {
        int y = (int) Math.round(x * Math.pow(10, digits));
        double z = y / Math.pow(10, digits);

        return z + "";
    }
}
