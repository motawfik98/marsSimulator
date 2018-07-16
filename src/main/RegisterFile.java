package main;

public class RegisterFile {
    private int readRegister1; // number of source register to read from
    private int readRegister2; // number of target register to read from
    private int writeRegister; // number of the register to write into
    private int regWrite; // indicates either we write in a register or not
    private int writeData; // value to be written

    private int readData1; // value of first register
    private int readData2; // value of second register

    public RegisterFile(int readRegister1, int readRegister2, int writeRegister, int regWrite) {
        this.readRegister1 = readRegister1;
        this.readRegister2 = readRegister2;
        this.writeRegister = writeRegister;
        this.regWrite = regWrite;

        readRegisters();
    }

    private void readRegisters() {
        readData1 = Data.getRegisterValue(readRegister1);
        readData2 = Data.getRegisterValue(readRegister2);
    }

    public void setWriteData(int writeData) {
        this.writeData = writeData;
    }

    public void changeRegister() {
        if (regWrite == 1)
            Data.setRegisterValue(writeRegister, writeData);

    }

    public int getReadData1() {
        return readData1;
    }

    public int getReadData2() {
        return readData2;
    }

    public int getWriteRegister() {
        return writeRegister;
    }

    public String getDataPath() {
        return "Register file\n" +
                "First register: " + readRegister1 + "\n" +
                "Second register: " + readRegister2 + "\n" +
                "Destination: " + writeRegister + "\n" +
                "Register write: " + regWrite + "\n" +
                "Value to write: " + writeData + "\n" +
                "---------------------------------";
    }
}
