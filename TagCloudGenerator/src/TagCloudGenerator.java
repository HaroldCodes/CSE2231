import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * This program asks users to enter an positive number n and return a tag cloud
 * that only get the first nth used words alphabetically.
 *
 * @author Tingyang Xie and Yunzhou Chen
 *
 */
public final class TagCloudGenerator {

    /**
     * Compare {@code Map.Pair<String, Integer>}s in alphabetical order by keys
     */
    private static class order1
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> one,
                Map.Pair<String, Integer> two) {

            return one.key().toLowerCase().compareTo(two.key().toLowerCase());
        }
    }

    /**
     * Compare {@code Map.Pair<String, Integer>}s by decreasing order by values.
     */
    private static class order2
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> one,
                Map.Pair<String, Integer> two) {
            return two.value().compareTo(one.value());
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudGenerator() {
    }

    /**
     * capital numbers of bottom number of the ASCII table.
     */
    public static final int BOTTOM = 64;
    /**
     * capital numbers of top number of the ASCII table.
     */
    public static final int L_MIDDLE = 91;
    /**
     * lowercase numbers of bottom number of the ASCII table.
     */
    public static final int T_MIDDLE = 96;
    /**
     * lowercase numbers of top number of the ASCII table.
     */
    public static final int TOP = 123;

    /**
     * This method creates a map the words of the user's given file and,
     * correspondingly, the number of times each word is used. The file will not
     * be changed.
     *
     * @param in
     *            an SimpleReader file
     * @requires in is not null
     * @ensures all words from in will appear once
     * @return Map<String, Integer> of words of the file and their counts
     */
    private static Map<String, Integer> reader(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        Map<String, Integer> words = new Map1L<String, Integer>();
        String string;
        while (!in.atEOS()) {
            string = in.nextLine();
            int i = 0;
            String word = "";
            while (i < string.length()) {
                char character = string.charAt(i);
                int num = character;
                if ((num > BOTTOM && num < L_MIDDLE)
                        || (num > T_MIDDLE && num <= TOP)) {
                    word += character;
                    word = word.toLowerCase();
                } else {
                    if (!words.hasKey(word)) {
                        words.add(word, 1);
                    } else {
                        int count = words.value(word);
                        words.replaceValue(word, count + 1);
                    }
                    word = "";
                }
                int l = string.length();
                if (((num > T_MIDDLE && num <= TOP)
                        || (num > BOTTOM && num < L_MIDDLE)) && i == l - 1) {
                    if (!words.hasKey(word)) {
                        words.add(word, 1);
                    } else {
                        int count = words.value(word);
                        words.replaceValue(word, count + 1);
                    }
                    word = "";
                }
                i++;
            }
        }

        if (words.hasKey("")) {
            words.remove("");
        }

        return words;
    }

    /**
     * Method for sorting by value.
     *
     * @param words
     *            map of all of the words and their counts
     * @requires words is not null
     * @ensures all Map.Pairs is sorted in decreasing order by their values
     * @return SortingMachine<Map.Pair<String, Integer>> of words of the file
     *         and their counts
     */
    public static SortingMachine<Map.Pair<String, Integer>> countSort(
            Map<String, Integer> words) {
        assert words != null : "Violation of: words is not null";
        Comparator com = new order2();
        SortingMachine<Map.Pair<String, Integer>> sortMachine = new SortingMachine1L(
                com);
        for (Map.Pair<String, Integer> each : words) {
            sortMachine.add(each);
        }
        words.clear();
        sortMachine.changeToExtractionMode();
        return sortMachine;
    }

    /**
     * Method for sorting by value.
     *
     * @param counts
     *            SortingMachine<Map.Pair<String, Integer>> of all of the words
     *            and their counts
     * @requires counts is not null && number is not null
     * @ensures all Map.Pairs in terms will be sorted in decreasing order based
     *          on their value
     * @return SortingMachine<Map.Pair<String, Integer>> of words of the file
     *         and their counts
     */
    public static SortingMachine<Map.Pair<String, Integer>> SortByAlph(
            SortingMachine<Map.Pair<String, Integer>> counts) {
        assert counts != null : "Violation of: counts is not null";
        Comparator com = new order1();
        SortingMachine<Map.Pair<String, Integer>> map = new SortingMachine1L<>(
                com);
        for (Map.Pair<String, Integer> count : counts) {
            map.add(count);
        }
        map.changeToExtractionMode();
        return map;
    }

    /**
     * Reduces the size of the Sorting Machine down to the number entered by the
     * user in order to increase efficiency.
     *
     * @param sortMachine
     *            SortingMachine<Map.Pair<String, Integer>> of all of the words
     *            and their counts
     * @param number
     *            the number of the words that you want to output
     * @requires sortMachine is not null && number > 0 && number <
     *           sortMachine.size()
     * @ensures all of the top n value in sortMachine will be extracted
     * @return SortingMachine<Map.Pair<String, Integer>> of resized top value
     *         Map.Pairs
     */
    public static SortingMachine<Map.Pair<String, Integer>> Sample(
            SortingMachine<Map.Pair<String, Integer>> sortMachine, int number) {
        assert sortMachine != null : "Violation of: sortMachine is not null";
        assert number <= sortMachine
                .size() : "Violation of: sortMachines <= size of sortMachine";
        SortingMachine<Map.Pair<String, Integer>> counts = sortMachine
                .newInstance();
        int i = 0;
        for (Map.Pair<String, Integer> count : sortMachine) {
            if (i < number) {
                counts.add(count);
            }
            i++;
        }
        return counts;
    }

    /**
     * It creates a map the words of the user's given file and, correspondingly,
     * the number of times each word is used.
     *
     * @param out
     *            is a SimpleWriter to write out to the file
     * @param counts
     *            a SortingMachine of all the words and their counts
     * @param str
     *            title of the page
     * @param number
     *            number input by the user
     * @requires counts is not null && number > 0 && out is not null
     * @ensures an HTML page of the tag cloud will be generated
     */
    public static void output(SortingMachine<Map.Pair<String, Integer>> counts,
            SimpleWriter out, String string, int number) {
        assert counts != null : "Violation of: counts is not null";
        assert out != null : "Violation of: out is not null";
        assert number > 0 : "Violation of: number > 0";

        out.println("<html>");
        out.println("<head>");
        out.print("<title>");
        out.print("Top " + number + " Words in " + string);
        out.println("</title>");
        out.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
        out.println("<body>");
        out.print("<h2>");
        out.print("Top " + number + " Words in " + string + "");
        out.println("</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");
        int ave = 0;
        for (Map.Pair<String, Integer> count : counts) {
            ave += count.value();
        }
        ave = ave / number;
        for (Map.Pair<String, Integer> count : counts) {
            out.print("<span style=\"cursor:default\" class=\"f");
            double perc = (double) (count.value() * 5) / ave;
            perc += 11;
            if (perc < 11) {
                perc = 11;
            } else if (perc > 48) {
                perc = 48;
            }
            out.print((int) perc);
            out.print("\" title=\"count: ");
            out.println(count.value() + "\">" + count.key() + "</span>");
        }

        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.println("Cloud Tag Generator");
        out.print("Please input file name: ");
        String fileName = in.nextLine();
        String name = fileName;
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '/') {
                for (int j = i; j < name.length(); j++) {
                    if (name.charAt(j) == '.') {
                        name = name.substring(i + 1, j);
                    }
                }
            }
        }
        SimpleReader file = new SimpleReader1L(fileName);
        out.println();
        out.print("Please input the number of words you want to search ");
        int num = in.nextInteger();
        while (num < 0) {
            out.println();
            out.print("You should enter an positive integer. Try again ");
            num = in.nextInteger();
        }
        Map<String, Integer> words = reader(file);
        SortingMachine res = countSort(words);
        SimpleWriter fileout = new SimpleWriter1L(
                "data/" + num + "_words_in_" + name + ".html");
        if (res.size() == 0) {
            output(res, fileout, fileName, num);
        } else {

            res = Sample(res, num);
            res = SortByAlph(res);

            output(res, fileout, fileName, num);
        }
        in.close();
        out.close();
    }
}
