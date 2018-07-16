package main;

public class ALUControl {
    private int ALUOp; // integer to determine which operation have to be done
    private int functionCode; // extended opcode of the function
    private int ALUSignal; // output of the ALUControl

    public ALUControl(int ALUOp, int functionCode) {
        this.ALUOp = ALUOp;
        this.functionCode = functionCode;
        generateOutput();
    }

    private void generateOutput() {
        if (ALUOp == 0)  // Add
            ALUSignal = 2;
        else if (ALUOp == 1) // Sub(branch)
            ALUSignal = 1;
        else // it's R type instruction so we look at the extended op code
            if (functionCode == Data.getExtendedOpCode("sll"))
                ALUSignal = 3;
            else if (functionCode == Data.getExtendedOpCode("nor"))
                ALUSignal = 0;
            else if (ALUOp == 3 || functionCode == Data.getExtendedOpCode("slt"))
                ALUSignal = 7;
            else if (functionCode == Data.getExtendedOpCode("add"))
                ALUSignal = 2;
    }

    public int getALUSignal() {
        return ALUSignal;
    }

    public String getDataPath() {
        return "ALU Control\n"
                + "ALU Op: " + ALUOp + "\n"
                + "Function code: " + functionCode + "\n"
                + "Output: " + ALUSignal + "\n"
                + "----------------------------";
    }
}
