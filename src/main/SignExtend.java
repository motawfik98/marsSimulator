package main;

public class SignExtend {
    private int input;
    private int output;

    public SignExtend(int input) {
        this.input = input;
        output = input; // as we're dealing with integers, so the output must be exactly like the input
    }


    public int getOutput() {
        return output;
    }

    public String getDataPath() {
        return "Sign Extend\n" +
                "Input: " + input + "\n" +
                "Output: " + output + "\n" +
                "----------------------";
    }
}
