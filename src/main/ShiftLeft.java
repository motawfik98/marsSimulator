package main;

public class ShiftLeft {
    private int input;
    private int output;

    public ShiftLeft(int input) {
        this.input = input;
        output = input * 4;
    }


    public int getOutput() {
        return output;
    }

    public String getDataPath() {
        return "Shift Left\n" +
                "Input: " + input + "\n" +
                "Output: " + output + "\n" +
                "----------------------";
    }
}
