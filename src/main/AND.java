package main;

public class AND {
    private int branch;
    private int zeroFlag;
    private int output;

    public AND(int branch, int zeroFlag) {
        this.branch = branch;
        this.zeroFlag = zeroFlag;

        calcOutput();
    }

    private void calcOutput() {
        if (branch == 1 && zeroFlag == 1)
            output = 1;
        else
            output = 0;
    }

    public int getOutput() {
        return output;
    }
}
