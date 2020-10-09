package qualification.task4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Stream;

public class Solution {
    private final IO io;

    private final InvertableArray invertableArray;
    private int requested = 0;

    public Solution(IO io, int size) throws IOException {
        this.io = io;
        this.invertableArray = new InvertableArray(size);
    }

    public static void main(String[] args) throws IOException {
        String[] line = IO.STDIO.receive().split(" ");
        final int numOfCases = Integer.parseInt(line[0]);
        final int arraySize = Integer.parseInt(line[1]);
        for (int i = 0; i < numOfCases; i++) {
            if (!new Solution(IO.STDIO, arraySize).run()) {
                break;
            }
        }
    }

    public boolean run() throws IOException {
        String result = runSolution();

        io.transmit(result);
        final String testResult = io.receive();
        return "Y".equals(testResult);

    }

    public String runSolution() throws IOException {
        int length = invertableArray.values.length;
        int i = 0;
        while (i < length && !invertableArray.isFull()) {
            boolean firstBit = request(i);
            invertableArray.set(i, firstBit);
            int mirrorIndex = length - i - 1;
            boolean mirrorBit = request(mirrorIndex);
            invertableArray.set(mirrorIndex, mirrorBit);
            i++;
            if (requested % 10 == 0) {
                // check if was inverted or/and mirrored
                {
                    Integer sameBitsIndex = invertableArray.findSameBitsIndex();
                    if (sameBitsIndex != null) {
                        boolean result = request(sameBitsIndex);
                        if (result != invertableArray.get(sameBitsIndex)) {
                            invertableArray.invert();
                        }
                    } else {
                        request(0); // just to avoid ruining sequence
                    }
                }
                {
                    Integer mirrorBitsIndex = invertableArray.findMirrorBitsIndex();
                    if (mirrorBitsIndex != null) {
                        boolean result = request(mirrorBitsIndex);
                        if (result != invertableArray.get(mirrorBitsIndex)) {
                            invertableArray.reverse();
                        }
                    } else {
                        request(0);
                    }
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < invertableArray.values.length; j++) {
            stringBuilder.append(invertableArray.get(j) ? "1" : "0");
        }
        return stringBuilder.toString();
    }

    private boolean request(int i) throws IOException {
        io.transmit(Integer.toString(i + 1));
        requested++;
        String reply = io.receive();
        return Integer.parseInt(reply) == 1;
    }


    private static class InvertableArray {

        private final Boolean[] values;

        private boolean isReversed = false;
        private boolean isInverted = false;


        private InvertableArray(int size) {
            this.values = new Boolean[size];
        }

        private void set(int index, boolean value) {
            int realIndex = isReversed ? values.length - index - 1 : index;
            values[realIndex] = isInverted ^ value;
        }

        private boolean get(int index) {
            int realIndex = isReversed ? values.length - index - 1 : index;
            return isInverted ^ values[realIndex];
        }

        private boolean isFull() {
            return Stream.of(values).allMatch(Objects::nonNull);
        }

        public Integer findSameBitsIndex() {
            for (int i = 0; i < values.length; i++) {
                int mirrorIndex = values.length - i - 1;
                if (values[i] != null && values[mirrorIndex] != null && values[i] == values[mirrorIndex]) {
                    return i;
                }
            }
            return null;
        }

        public Integer findMirrorBitsIndex() {
            for (int i = 0; i < values.length; i++) {
                int mirrorIndex = values.length - i - 1;
                if (values[i] != null && values[mirrorIndex] != null && values[i] != values[mirrorIndex]) {
                    return i;
                }
            }
            return null;
        }

        public void invert() {
            isInverted = !isInverted;
        }

        public void reverse() {
            isReversed = !isReversed;
        }
    }

    interface IO {
        BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

        IO STDIO = new IO() {
            @Override
            public String receive() throws IOException {
                return READER.readLine();
            }

            @Override
            public void transmit(String request) throws IOException {
                System.out.println(request);
                System.out.flush();

            }
        };

        String receive() throws IOException;

        void transmit(String request) throws IOException;
    }

}
