package round1a.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Solution {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int numOfCases = Integer.parseInt(in.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            int size = Integer.parseInt(in.readLine());
            List<String> array = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                array.add(in.readLine());
            }
            String result = solve(array);
            System.out.println("Case #" + i + ": " + result);
        }
    }


    public static String solve(List<String> strings) {
        strings = strings.stream().map(str -> str.replaceAll("\\*+", "*")).collect(toList());
        String prefix = buildPrefixFromStrings(strings);
        if (prefix == null) {
            return "*";
        }
        String suffix = buildSuffixFromStrings(strings);
        if (suffix == null) {
            return "*";
        }
        String middleWords = buildMiddleWords(strings);
        return prefix + middleWords + suffix;
    }

    private static String buildMiddleWords(List<String> strings) {
        return strings.stream().flatMap(
            str -> {
                String[] parts = str.split("\\*", -1);
                return Stream.of(parts)
                    .skip(1)
                    .limit(parts.length - 2);
            }
        ).collect(Collectors.joining());
    }

    private static String buildSuffixFromStrings(List<String> strings) {
        return revert(buildPrefixFromStrings(strings.stream().map(Solution::revert).collect(toList())));
    }

    private static String revert(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            stringBuilder.append(str.charAt(i));
        }
        return stringBuilder.toString();
    }


    private static String buildPrefixFromStrings(List<String> strings) {
        StringBuilder prefix = new StringBuilder();
        List<String> prefixes = strings.stream().map(str -> str.split("\\*", -1)[0]).collect(toList());
        Integer maxLen = prefixes.stream().map(String::length).max(naturalOrder()).orElse(0);
        for (int i = 1; i <= maxLen; i++) {
            final int currLen = i;
            Set<Character> chars = prefixes.stream().filter(it -> it.length() >= currLen).map(it -> it.charAt(currLen - 1)).collect(toSet());
            if (chars.size() != 1) {
                return null;
            }
            prefix.append(chars.iterator().next());
        }
        return prefix.toString();
    }
}
