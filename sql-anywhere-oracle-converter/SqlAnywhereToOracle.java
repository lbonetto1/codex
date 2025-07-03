import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple utility that converts basic SQL Anywhere syntax to Oracle Database
 * syntax. This is not a full SQL parser, but performs common text replacements.
 */
public class SqlAnywhereToOracle {
    public static void main(String[] args) throws IOException {
        String input = readInput(args);
        String converted = convertSql(input);
        System.out.println(converted);
    }

    private static String readInput(String[] args) throws IOException {
        if (args.length > 0) {
            return Files.readString(Paths.get(args[0]), StandardCharsets.UTF_8);
        }
        return new String(System.in.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Perform very simple SQL Anywhere to Oracle conversions.
     */
    private static String convertSql(String sql) {
        String out = sql;

        // TOP n -> FETCH FIRST n ROWS ONLY
        Pattern topPattern = Pattern.compile("(?i)\\bSELECT\\s+TOP\\s+(\\d+)\\s");
        Matcher topMatcher = topPattern.matcher(out);
        if (topMatcher.find()) {
            String rows = topMatcher.group(1);
            out = topMatcher.replaceFirst("SELECT ");
            String suffix = " FETCH FIRST " + rows + " ROWS ONLY";
            int idx = out.lastIndexOf(';');
            if (idx >= 0) {
                out = out.substring(0, idx) + suffix + out.substring(idx);
            } else {
                out = out + suffix;
            }
        }

        // GETDATE() -> SYSDATE
        out = out.replaceAll("(?i)GETDATE\\s*\\(\\s*\\)", "SYSDATE");

        // CURRENT TIMESTAMP -> SYSTIMESTAMP
        out = out.replaceAll("(?i)CURRENT\\s+TIMESTAMP", "SYSTIMESTAMP");

        // Data type conversions
        out = out.replaceAll("(?i)\\bVARCHAR\\b", "VARCHAR2");
        out = out.replaceAll("(?i)\\bNVARCHAR\\b", "NVARCHAR2");
        out = out.replaceAll("(?i)\\bINT\\b", "NUMBER");
        out = out.replaceAll("(?i)\\bINTEGER\\b", "NUMBER");
        out = out.replaceAll("(?i)\\bBIT\\b", "NUMBER(1)");
        out = out.replaceAll("(?i)\\bDATETIME\\b", "TIMESTAMP");

        return out;
    }
}
