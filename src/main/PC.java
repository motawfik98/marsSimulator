package main;

public class PC {
    private int startPosition;
    private int currentPosition;
    private int cycles = 0;

    public PC(int startPosition) {
        this.startPosition = startPosition;
        currentPosition = startPosition;
    }

    public void setNextPosition(int jumpPosition) {
        currentPosition = jumpPosition;
        cycles++;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public String get4Bits() {
        return Data.extendToNBits(currentPosition, 32).substring(0, 4);
    }

    public int getCycles() {
        return cycles;
    }

    public String getDataPath() {
        return "Program counter\n" +
                "Current position: " + currentPosition + "\n" +
                "----------------";
    }
}
