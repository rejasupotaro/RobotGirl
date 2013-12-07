package rejasupotaro.robotgirl.faker;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Faker {

    private static final Pattern PATTERN_WORD_SURROUNDED_DOUBLE_QUOTE = Pattern.compile("\".+\"");

    private static final Random RANDOM = new Random();

    private static Locale sLocale;

    public static void init(Context context, Locale locale) throws IOException {
        sLocale = locale;

        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = assetManager.open(locale.getFileName());
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            if (line.trim().startsWith("first_name")) {
                Name.sFirstNameList = parseArray(line);
            } else if (line.trim().startsWith("last_name")) {
                Name.sLastNameList = parseArray(line);
            }

            line = bufferedReader.readLine();
        }
    }

    public static List<String> parseArray(String source) {
        List<String> wordList = new ArrayList<String>();
        int wordArrayStartIndex = source.indexOf('[');
        int wordArrayEndIndex = source.indexOf(']');
        String[] words = source.substring(wordArrayStartIndex, wordArrayEndIndex).split(" ");
        for (String word : words) {
            Matcher matcher = PATTERN_WORD_SURROUNDED_DOUBLE_QUOTE.matcher(word);
            if (matcher.find()) {
                String result = matcher.group(0);
                wordList.add(result.substring(1, result.length() - 1));
            }
        }
        return wordList;
    }

    private static String randomChoice(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    public static class Name {

        private static List<String> sFirstNameList = new ArrayList<String>();

        private static List<String> sLastNameList = new ArrayList<String>();

        public static List<String> getFirstNameList() {
            return sFirstNameList;
        }

        public static List<String> getLastNameList() {
            return sLastNameList;
        }

        public static String name() {
            if (sLocale == Locale.JA) {
                return randomChoice(sLastNameList) + randomChoice(sFirstNameList);
            } else {
                return randomChoice(sFirstNameList) + randomChoice(sLastNameList);
            }
        }
    }
}
