package org.lombok.donotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.lombok.donotation.Either.left;
import static org.lombok.donotation.Either.right;

public class Parsers {
    public static Either<String, Double> parseDouble(final String s) {
        try {
            return right(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return left(e.getLocalizedMessage());
        }
    }

    public static Either<String, Integer> parseInt(final String s) {
        try {
            return right(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return left(e.getLocalizedMessage());
        }
    }

    public static Either<String, String[]> parseRegex(final String regex, final String input) {
        final Matcher mx = Pattern.compile(regex).matcher(input);
        if (mx.find())
            return right(IntStream.range(0, mx.groupCount() + 1).mapToObj(mx::group).toArray(String[]::new));
        return left(String.format("string '%s' do not match pattern '%s'", input, regex));
    }
}
