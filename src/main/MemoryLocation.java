package main;

public class MemoryLocation {
    private int address;
    private int value;

    public MemoryLocation(int address, int value) {
        this.address = address;
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getAddress() {
        return address;
    }
}
