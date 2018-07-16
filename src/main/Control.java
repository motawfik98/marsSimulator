package main;

public class Control {
    private int opCode;
    private int registerDest = 0;
    private int registerDestJump = 0;
    private int jump = 0;
    private int jumpRegister = 0;
    private int branch = 0;
    private int memoryRead = 0;
    private int memoryToRegister = 0;
    private int memoryByte = 0;
    private int memoryUnsigned = 0;
    private int memoryToRegisterJump = 0;
    private int ALUOp = 0;// output to theALU
    private int memoryWrite = 0;
    private int ALUSource = 0;
    private int registerWrite = 0;

    public Control(int opCode, int extendedOpCode) {
        this.opCode = opCode;
        if (opCode == 0) {// R Type
            if (extendedOpCode == Data.getExtendedOpCode("jr"))
                jumpRegister = 1;
            initRControls();

        } else if (opCode == Data.getOpCode("j"))
            initJumpControls();
        else if (opCode == Data.getOpCode("jal"))
            initJumpLinkControls();
        else if (opCode == Data.getOpCode("beq"))
            initBranchControls();
        else if (opCode == Data.getOpCode("lw") || opCode == Data.getOpCode("lb") ||
                opCode == Data.getOpCode("lbu"))
            initLoadControls();
        else if (opCode == Data.getOpCode("sw") || opCode == Data.getOpCode("sb"))
            initStoreControls();
        else if (opCode == Data.getOpCode("addi"))
            initAddIControls();
        else if (opCode == Data.getOpCode("slti"))
            initSLTIControls();
    }

    private void initSLTIControls() {
        registerWrite = 1;
        ALUSource = 1;
        ALUOp = 3;
    }

    private void initAddIControls() {
        registerWrite = 1;
        ALUSource = 1;
        ALUOp = 0;
    }

    private void initJumpLinkControls() {
        initJumpControls();
        registerDestJump = 1;
        memoryToRegisterJump = 1;
        registerWrite = 1;
    }

    private void initRControls() {
        registerDest = 1;
        registerWrite = 1;
        ALUSource = 0;
        ALUOp = 2;
    }

    private void initJumpControls() {
        jump = 1;
    }

    private void initStoreControls() {
        if (opCode == Data.getOpCode("sb"))
            memoryByte = 1;
        ALUSource = 1;
        memoryWrite = 1;
        ALUOp = 0;
    }

    private void initBranchControls() {
        branch = 1;
        ALUSource = 0;
        ALUOp = 1;
    }

    private void initLoadControls() {
        if (opCode == Data.getOpCode("lb"))
            memoryByte = 1;
        else if (opCode == Data.getOpCode("lbu")) {
            memoryByte = 1;
            memoryUnsigned = 1;
        }
        memoryRead = 1;
        memoryToRegister = 1;
        ALUSource = 1;
        registerWrite = 1;
        ALUOp = 0;
    }

    public int getRegisterDest() {
        return registerDest;
    }

    public int getJump() {
        return jump;
    }

    public int getBranch() {
        return branch;
    }

    public int getMemoryRead() {
        return memoryRead;
    }

    public int getMemoryToRegister() {
        return memoryToRegister;
    }

    public int getALUOp() {
        return ALUOp;
    }

    public int getMemoryWrite() {
        return memoryWrite;
    }

    public int getALUSource() {
        return ALUSource;
    }

    public int getRegisterWrite() {
        return registerWrite;
    }

    public int getJumpRegister() {
        return jumpRegister;
    }

    public int getRegisterDestJump() {
        return registerDestJump;
    }

    public int getMemoryToRegisterJump() {
        return memoryToRegisterJump;
    }

    public int getMemoryByte() {
        return memoryByte;
    }

    public int getMemoryUnsigned() {
        return memoryUnsigned;
    }

    public String getDataPath() {
        return "Control\n" +
                "Input: " + opCode + "\n" +
                "Register Destination: " + registerDest + "\n" +
                "Register Jump Destination: " + registerDestJump + "\n" +
                "Jump: " + jump + "\n" +
                "Jump Register: " + jumpRegister + "\n" +
                "Branch: " + branch + "\n" +
                "Memory read: " + memoryRead + "\n" +
                "Memory To Register: " + memoryToRegister + "\n" +
                "Memory Byte: " + memoryByte + "\n" +
                "Memory Unsigned: " + memoryUnsigned + "\n" +
                "Memory to Register Jump: " + memoryToRegisterJump + "\n" +
                "ALU opCode: " + ALUOp + "\n" +
                "Memory Write: " + memoryWrite + "\n" +
                "ALU Source: " + ALUSource + "\n" +
                "Register write: " + registerWrite + "\n" +
                "-------------------------";
    }
}
