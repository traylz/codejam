package round1b.task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {


    public Solution(IO stdio) {

    }

    public static void main(String[] args) throws IOException {
        String[] line = IO.STDIO.receive().split(" ");
        final int numOfCases = Integer.parseInt(line[0]);
        final int arraySize = Integer.parseInt(line[1]);
        for (int i = 0; i < numOfCases; i++) {
            if (!new Solution(IO.STDIO).run()) {
                break;
            }
        }
    }

    private boolean run() {
        return true;

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
