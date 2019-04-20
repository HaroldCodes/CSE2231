import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Tag Cloud Generator with Standard Java Components
 *
 * @author Yunzhou Chen/Tingyang Xie
 *
 */

public final class TagCloudGenerator {

    private TagCloudGenerator() {

    }

    /**
     * max number of appearance of a word
     */
    static int maxValue = Integer.MIN_VALUE;
    /**
     * min number of appearance of a word
     */
    static int minValue = Integer.MAX_VALUE;
    /**
     * seperators in string.
     */
    static final String Seperators = " \t\n\r,-.!?[]';:/()*";
    //
    /**
     * max size of font
     */
    static final int MAX_FONT_SIZE = 48;
    /**
     * min size of font
     */
    static final int MIN_FONT_SIZE = 15;

    /**
     *
     * Comparator compare the count.
     *
     */

    private static class CountComparator
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> a,
                Map.Entry<String, Integer> b) {
            return b.getValue().compareTo(a.getValue());
        }

    }

    /**
     *
     * Comparator compare the string in alphabetical order.
     *
     */
    private static class AlphabeticalComparator
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> a,
                Map.Entry<String, Integer> b) {

            return a.getKey().toLowerCase().compareTo(b.getKey().toLowerCase());
        }
    }

    /**
     * use a map to record and count all the words
     *
     * @param in
     * @return a map contains record string and this string counts.
     */
    private static Map<String, Integer> getMap(BufferedReader in) {
        Map<String, Integer> map = new HashMap<>();

        try {
            String str = in.readLine();
            while (str != null) {

                str = str.toLowerCase();
                StringBuilder string = new StringBuilder();

                for (int i = 0; i < str.length(); i++) {
                    if (Seperators.indexOf(str.charAt(i)) == -1) {
                        string.append(str.charAt(i));
                    }
                    if (string.length() > 0
                            && (Seperators.indexOf(str.charAt(i)) != -1
                                    || i == str.length() - 1)) {

                        if (!map.containsKey(string.toString())) {
                            map.put(string.toString(), 1);
                        } else {
                            map.put(string.toString(),
                                    map.get(string.toString()) + 1);
                        }
                        string = new StringBuilder();
                    }
                }
                str = in.readLine();
            }

        } catch (IOException e) {
            System.err.println("Input stream has error");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Method getMap has a bug");
        }
        return map;

    }

    /**
     * counts in sorted by the comparator a map to record the max and min counts
     *
     * @param count
     * @param sortingMachine
     * @return a map that contains the most frequent words and counts.
     */
    private static Map<String, Integer> getMapWithFrequentWords(int counts,
            List<Map.Entry<String, Integer>> sortingMachine) {

        Map<String, Integer> HashMap = new HashMap<>();

        for (int i = 0; i < counts; i++) {
            Map.Entry<String, Integer> pair = sortingMachine.remove(0);

            if (pair.getValue() < minValue) {
                minValue = pair.getValue();
            }
            if (pair.getValue() > maxValue) {
                maxValue = pair.getValue();
            }

            HashMap.put(pair.getKey(), pair.getValue());
        }

        return HashMap;
    }

    /**
     * Generates a HTML file
     *
     * @param alphabeticalsort
     * @param a
     *            simple writer to generate html.
     * @param fileName
     *            of input file.
     * @param how
     *            many words the user want to use
     * @param a
     *            map contains String and corresponding font size.
     *
     * @clear alphabeticalsort
     *
     */
    private static void generateHTML(
            List<Map.Entry<String, Integer>> alphabeticalsort,
            BufferedWriter writer, String fileName, int counts,
            Map<String, Integer> map) {
        try {

            writer.write("<html><head><title>" + "Top " + counts + " words in "
                    + fileName
                    + "</title><link rel=\"stylesheet\" type=\"text/css\""
                    + " href=\"data/tagcloud.css\">" + "</head><body>" + "\n");
            writer.write("<h2>" + "Top " + counts + " words in " + fileName
                    + "</h2>" + "\n");
            writer.write("<hr/>" + "\n");
            writer.write("<div class = \"cdiv\"><p class = \" cbox\">" + "\n");
            while (alphabeticalsort.size() > 0) {
                Map.Entry<String, Integer> pair = alphabeticalsort.remove(0);
                writer.write("<span style=\"cursor:default\" class=\"f"
                        + map.get(pair.getKey()) + "\" title=\"count:"
                        + pair.getValue() + "\">" + pair.getKey() + "</span> ");
            }
            writer.write("</p></div></body></html>" + "\n");
        } catch (IOException e) {
            System.err.println("generate HTML unsuccessfully.");
            System.exit(1);

        }
    }

    /**
     * a map to record font-size and its word
     *
     * @param alphabeticalsort
     * @return a map with font size
     */
    private static Map<String, Integer> getMapwithFontSize(
            Map<String, Integer> map) {

        Map<String, Integer> HashMap = new HashMap<>();

        for (Map.Entry<String, Integer> cur : map.entrySet()) {

            int font_size = MIN_FONT_SIZE;

            if (minValue != maxValue) {
                font_size = (cur.getValue()) * (MAX_FONT_SIZE - MIN_FONT_SIZE)
                        / (maxValue + minValue) + MIN_FONT_SIZE;
            }
            HashMap.put(cur.getKey(), font_size);

        }

        return HashMap;
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Initialize the program. To read the input file, record the name of
         * output fileName and how many words user want to display in tagcloud.
         */

        Scanner in = new Scanner(System.in);

        System.out.print("Please input the fileName:");

        String nameToRead = in.nextLine();
        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(nameToRead));

        } catch (IOException e) {

            System.err.println("Error Reading File, needed right name:");
            return;

        }
        /*
         * Create the output file with IOException
         */

        System.out.print("Please input the output fileName: ");
        String StorefileName = in.nextLine();
        BufferedWriter writer = null;

        while (StorefileName.equals(nameToRead)) {
            System.out.print(
                    "Same with input file name. Please use another output fileName: ");
            StorefileName = in.nextLine();
        }

        try {

            writer = new BufferedWriter(new FileWriter(StorefileName));

        } catch (IOException e) {

            System.err.println("Error Creating File");
            return;
        }
        /*
         * use number of words in the input file to generate TagCloud
         */

        System.out.println("How many words you want :");

        int counts = in.nextInt();

        while (counts <= 0) {

            System.out.println(
                    "Please input how many words you want(Greater than 0!): ");
            counts = in.nextInt();

        }

        /*
         * use getMap method to record the word in input file and its count, use
         * sorting machine to sort word by its counts.
         */

        Map<String, Integer> map = getMap(reader);

        if (counts > map.size()) {
            counts = map.size();
        }

        /*
         * Use ArrayList to sort the words by the counts
         */

        List<Map.Entry<String, Integer>> countWordsort = new ArrayList<>();
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        for (Map.Entry<String, Integer> pair : entrySet) {
            countWordsort.add(pair);
        }
        countWordsort.sort(new CountComparator());

        /*
         * get a map that contains words that needed to be displayed in tagcloud
         * html and its counts.
         */
        Map<String, Integer> sortedMapByCounts = getMapWithFrequentWords(counts,
                countWordsort);

        /*
         * get a map that contains words and its font size.
         */
        Map<String, Integer> wordwithFontSize = getMapwithFontSize(
                sortedMapByCounts);

        /*
         * sort the words in alphabetical order.
         */
        List<Map.Entry<String, Integer>> alphabeticalsort = new ArrayList<>();

        for (Map.Entry<String, Integer> pair : sortedMapByCounts.entrySet()) {
            alphabeticalsort.add(pair);
        }
        alphabeticalsort.sort(new AlphabeticalComparator());

        /**
         * make html.
         */

        generateHTML(alphabeticalsort, writer, StorefileName, counts,
                wordwithFontSize);

        /*
         * close the bufferedWriter
         */
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Fail to close the writer");
            }

        }
        /*
         * close the bufferedReader
         */

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Fail to close the reader");
            }
        }
        /*
         * close scanner
         */
        in.close();

    }

}
