package org.lombok.donotation;

import lombok.bind;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lombok.donotation.MOptional.of;
import static org.lombok.donotation.MStream.from;
import static org.lombok.donotation.Parsers.parseDouble;
import static org.lombok.donotation.Parsers.parseRegex;

public class main {

    private static MOptional<Double> solveQuadratic(double a, double b, double c) {
        @bind Double r = of(b * b - 4. * a * c).filter(w -> w >= 0.).get();
        return of((-b + Math.sqrt(r)) / (2. * a));
    }

    private static Either<String, Double> solveQuadratic(final String equation) {
        @bind String[] mx = parseRegex("^([\\+\\-\\.0-9]+)x\\^2([\\+\\-\\.0-9]+)x([\\+\\-\\.0-9]+)$", equation.replace(" ", "")).get();
        @bind Double a = parseDouble(mx[1]).get();
        @bind Double b = parseDouble(mx[2]).get();
        @bind Double c = parseDouble(mx[3]).get();
        return solveQuadratic(a, b, c).toEither("I don't know the imaginary numbers!");
    }

    private static MStream<String> solveQuadratics(final MStream<String> lines) {

        final AtomicInteger row = new AtomicInteger(0);
        @bind String equations = lines.get();
        row.incrementAndGet();

        final AtomicInteger col = new AtomicInteger(0);
        @bind String equation = MStream.of(equations.split(";")).get();
        col.incrementAndGet();

        final String result = solveQuadratic(equation)
                .either(
                        e -> String.format("at %d,%d: %s", row.get(), col.get(), e),
                        x -> x.toString());

        return MStream.of(result);
    }

    private static void batch(final String inputFile, final String outputFile) throws IOException {
        final File i = new File(inputFile);
        final File o = new File(outputFile);

        try (final InputStream is = new FileInputStream(i);
             final BufferedReader br = new BufferedReader(new InputStreamReader(is));
             final OutputStream os = new FileOutputStream(o);
             final PrintWriter bw = new PrintWriter(new OutputStreamWriter(os))) {

            solveQuadratics(from(br.lines())).forEach(bw::println);

        }
    }

    public static void main(String... args) throws IOException {
        if (args.length != 2)
            System.err.printf("usage: do-notation-example input.equations output.equations%n");
        else
            batch(args[0], args[1]);
    }
}
