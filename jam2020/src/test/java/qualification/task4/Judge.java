package qualification.task4;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Judge implements Solution.IO {

    private final Random random = new Random();
    private Queue<String> responseQueue = new ArrayDeque<>();
    private final Boolean[] values;
    private boolean inverted;
    private boolean reverted;
    private int requestCount = 0;

    Judge(Boolean[] values) {
        this.values = values;
    }

    @Override
    public String receive() throws IOException {
        return Objects.requireNonNull(responseQueue.remove());
    }

    @Override
    public void transmit(String request) throws IOException {
        if (requestCount > 150) {
            reply("N");
        }  else if (values.length == request.length()) {
            boolean validate = validate(request);
            if (!validate) {
                System.err.println("Solution yielded " + request + " although real state was " + stateToString());
            }
            reply(validate ? "Y" : "N");
        } else {
            if (requestCount % 10 == 0) {
                shuffle();
            }
            boolean result = getAtIndex(Integer.parseInt(request) - 1);
            reply(result ? "1" : "0");
            requestCount++;
        }
    }

    private void shuffle() {
        reverted ^= flipACoin();
        inverted ^= flipACoin();
    }

    private boolean flipACoin() {
        return random.nextInt() % 2 == 0;
    }

    private void reply(String string) {
        responseQueue.add(string);
    }
    
    private boolean getAtIndex(int index) {
        int realIndex = reverted ? values.length - index - 1 : index;
        return values[realIndex] ^ inverted;
    }

    private boolean validate(String request) {
        return stateToString().equals(request);
    }

    public String stateToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < values.length; j++) {
            stringBuilder.append(getAtIndex(j) ? "1" : "0");
        }
        return stringBuilder.toString();
    }

    public static Boolean[] stringToBoolArray(String request) {
        return Stream.of(request.split("")).map(Integer::parseInt).map(it -> it == 1).collect(Collectors.toList()).toArray(Boolean[]::new);
    }

    public int size() {
        return values.length;
    }
}