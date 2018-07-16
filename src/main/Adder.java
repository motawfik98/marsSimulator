package main;

public class Adder {
    private int firstNumber;
    private int secondNumber;

    public Adder(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    public int getResult() {
        return firstNumber + secondNumber;
    }

    public String getDataPath() {
        return "Adder\n" +
                "input1:" + firstNumber + "\n" +
                "input2: " + secondNumber + "\n" +
                "output: " + getResult() + "\n" +
                "-------------------------";
    }
}
