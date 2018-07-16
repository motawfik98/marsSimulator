package main;

public class Mux {
    private int firstInput;
    private int secondInput;
    private int select;

    public Mux(int firstInput, int secondInput, int select) {
        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.select = select;
    }

    public int getOutput() {
        if (select == 0)
            return firstInput;
        return secondInput;
    }
}
