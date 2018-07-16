package main;

public class ALU {
    private int data1; // first number
    private int data2; // second number
    private int shamt; // shift amount
    private int ALUControl; // bits to decide what operation to perform
    private int output; // result of the ALU operation
    private int zeroFlag;

    public ALU(int data1, int data2, int shamt, int ALUControl) {
        this.data1 = data1;
        this.data2 = data2;
        this.shamt = shamt;
        this.ALUControl = ALUControl;
        calculateOutput();
        outputZeroFlag();
    }

    private void outputZeroFlag() {
        zeroFlag = (data1 - data2 == 0) ? 1 : 0;
    }

    private void calculateOutput() {
        switch (ALUControl) {
            case 0: // nor operation
                output = ~(data1 | data2);
                break;
            case 1: //sub operation
                output = data1 - data2;
                break;
            case 2: // add operation
                output = data1 + data2;
                break;
            case 3: // sll operation
                output = data2 << shamt;
                break;
            case 7: // slt operation
                if (data1 < data2)
                    output = 1;
                else
                    output = 0;
                break;
        }
    }

    public String getDataPath() {
        return "ALU\n" +
                "First input: " + data1 + "\n" +
                "Second input: " + data2 + "\n" +
                "ALU Control: " + ALUControl + "\n" +
                "Result: " + output + "\n" +
                "Zero flag: " + zeroFlag + "\n" +
                "--------------------------";
    }

    public int getOutput() {
        return output;
    }

    public int getZeroFlag() {
        return zeroFlag;
    }
}
