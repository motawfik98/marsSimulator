package main;

import java.util.HashMap;

public class Instruction {

    private String machineInstruction;
    private String[] splitted;
    private String stringInstruction;
    private int opCode;
    private int destination;
    private int source;
    private int secondSource;
    private int shiftAmount;
    private int constant;
    private int functionCode;
    private int jumpAddress;

    private PC programCounter;
    private char type;

    public Instruction(String stringInstruction, PC programCounter) {
        this.programCounter = programCounter;
        this.stringInstruction = stringInstruction;
        String[] initialSplit = stringInstruction.split(": | |\\(|\\)|,|:"); // splits the string instructions on any non-word
        initialSplit = Data.removeNullsFromStringArray(initialSplit);

        if (!Data.isInstruction(initialSplit[0])) { // checks if the first word is not an supported instruction (label)
            splitted = new String[initialSplit.length - 1];
            System.arraycopy(initialSplit, 1, splitted, 0, splitted.length); // copy the initialSplit array to the final one without the labrl
        } else
            splitted = initialSplit; // if there's not a label then we make the final array point to the initial one
        setType();

        setAllBits(); // sets all the values of the variables to use in the data path

    }


    private void setAllBits() {
        opCode = Integer.parseInt(machineInstruction.substring(0, 6), 2);
        source = Integer.parseInt(machineInstruction.substring(6, 11), 2);
        secondSource = Integer.parseInt(machineInstruction.substring(11, 16), 2);
        destination = Integer.parseInt(machineInstruction.substring(16, 21), 2);
        shiftAmount = Integer.parseInt(machineInstruction.substring(21, 26), 2);
        functionCode = Integer.parseInt(machineInstruction.substring(26, 32), 2);

        constant = (short) Integer.parseInt(machineInstruction.substring(16, 32), 2);
        jumpAddress = Integer.parseInt(machineInstruction.substring(6, 32), 2);
    }

    private void setType() {
        opCode = Data.getOpCode(splitted[0]);
        switch (opCode) {
            case 0:
                type = 'R';
                setRValues();
                break;
            case 2:
            case 3:
                type = 'J';
                setJValues();
                break;
            default:
                type = 'I';
                setIValues();
                break;
        }

    }

    private void setIValues() {
        source = Data.getRegisterNumber(splitted[1]);
        if (Data.isRegister(splitted[2])) { // if it is not a load or store word ex.(addi, beq)
            destination = Data.getRegisterNumber(splitted[2]);
            HashMap<String, Integer> labelsPositions = InstructionMemory.getLabelPosition();
            if (isLabel(splitted[3])) { // if it is a branch instruction, we calculate the label position (binary representation)
                int labelPosition = labelsPositions.get(splitted[3] + ":");

                int currentPosition = programCounter.getCurrentPosition();
                int initialPosition = programCounter.getStartPosition();
                constant = ((initialPosition + labelPosition * 4) - (currentPosition + 4)) / 4;
            } else // then the last part must be the constant
                constant = Integer.parseInt(splitted[3]);
        } else { // it must be a load or store
            destination = Data.getRegisterNumber(splitted[3]);
            constant = Integer.parseInt(splitted[2]);
        }
        machineInstruction = Data.extendToNBits(opCode, 6) +
                Data.extendToNBits(destination, 5) +
                Data.extendToNBits(source, 5) +
                Data.extendToNBits(constant, 16);
    }

    private boolean isLabel(String s) {

        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private void setJValues() { // the instruction contains only 2 words, (instruction and label)
        HashMap<String, Integer> labelPosition1 = InstructionMemory.getLabelPosition();
        int labelPosition = labelPosition1.get(splitted[1] + ":") * 4;

        Adder adder = new Adder(labelPosition, programCounter.getStartPosition());
        constant = adder.getResult();
        jumpAddress = constant / 4;
        machineInstruction = Data.extendToNBits(opCode, 6) +
                Data.extendToNBits(jumpAddress, 26);
    }

    private void setRValues() {
        functionCode = Data.getExtendedOpCode(splitted[0]); // gets the extended opcode for the given instruction
        switch (splitted[0]) { // make a switch based on the first word (instruction)
            case "sll":
                source = 0;
                secondSource = Data.getRegisterNumber(splitted[2]);
                destination = Data.getRegisterNumber(splitted[1]);
                shiftAmount = Integer.parseInt(splitted[3]);
                break;
            case "jr":
                source = Data.getRegisterNumber(splitted[1]);
                secondSource = 0;
                destination = 0;
                shiftAmount = 0;
                break;
            default:
                source = Data.getRegisterNumber(splitted[2]);
                secondSource = Data.getRegisterNumber(splitted[3]);
                destination = Data.getRegisterNumber(splitted[1]);
                shiftAmount = 0;
                break;
        }
        machineInstruction = Data.extendToNBits(opCode, 6) +
                Data.extendToNBits(source, 5) +
                Data.extendToNBits(secondSource, 5) +
                Data.extendToNBits(destination, 5) +
                Data.extendToNBits(shiftAmount, 5) +
                Data.extendToNBits(functionCode, 6);
    }

    public String getMachineInstruction() {
        return machineInstruction;
    }

    public int getOpCode() {
        return opCode;
    }

    public int getDestination() {
        return destination;
    }

    public int getSource() {
        return source;
    }

    public int getSecondSource() {
        return secondSource;
    }

    public int getShiftAmount() {
        return shiftAmount;
    }

    public int getConstant() {
        return constant;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public int getJumpAddress() {
        return jumpAddress;
    }

    public String getStringInstruction() {
        return stringInstruction;
    }

    public char getType() {
        return type;
    }
}
