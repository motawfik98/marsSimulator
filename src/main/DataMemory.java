package main;

public class DataMemory {
    private int address; // address to write to
    private int writeData; // value to be written to the dataMemory (if memoryWrite == 1)
    private int output; // output of the dataMemory (if memoryRead == 1)
    private int lastByte; // the last byte in the word (signed) (if lb == 1)
    private int lastByteUnsigned; // the last byte in the word (unsigned) (if lbu == 1)
    // CONTROL SIGNALS
    private int memoryWrite; // bit to determine if we're going to write in the dataMemory
    private int memoryRead; // bit to determine if we're going to read from the dataMemory
    private int memoryByte; // bit to determine if we're dealing with bytes (ex. lb, lbu, sb)
    private int unsigned; // bit to determine if we're getting the value unsigned

    public DataMemory(int memoryWrite, int memoryRead, int memoryByte, int unsigned) {
        this.memoryWrite = memoryWrite;
        this.memoryRead = memoryRead;
        this.memoryByte = memoryByte;
        this.unsigned = unsigned;
        initOutput();
    }


    private void initOutput() { // initialize the output by getting the value in the given address from the dataMemory
        output = Data.getFromMemory(address);
        String binaryValue = Integer.toBinaryString(output);
        lastByte = (short) Integer.parseInt(binaryValue, 2);
        lastByteUnsigned = Integer.parseUnsignedInt(binaryValue, 2);
        if (memoryByte == 1)
            if (unsigned == 1)
                output = lastByteUnsigned;
            else
                output = lastByte;

    }

    public int getOutput() { // returns the output if memoryRead == 1
        if (memoryRead == 1)
            return output;
        return 0;
    }

    public void addToMemory() { // adds to the memory if memoryWrite == 1
        if (memoryWrite == 1)
            Data.addToMemory(new MemoryLocation(address, writeData));
    }

    public void initLocationValue(int address, int writeData) { // sets the location and value, then initialize the output
        this.address = address;
        this.writeData = writeData;
        initOutput();
    }

    public String getDataPath() {
        return "Memory Read: " + memoryRead + "\n" +
                "Memory Write: " + memoryWrite + "\n" +
                "Value to Write: " + writeData + "\n" +
                "Output: " + output + "\n" +
                "----------------------------";
    }
}
