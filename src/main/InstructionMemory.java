package main;

import java.util.HashMap;

public class InstructionMemory {
    private String[] stringInstructions;
    private Instruction currentInstruction;
    private PC programCounter;
    private int input;
    private static HashMap<String, Integer> labelPosition;


    public InstructionMemory(String[] stringInstructions, PC programCounter) {
        this.programCounter = programCounter;
        labelPosition = new HashMap<>();
//        this.stringInstructions = stringInstructions;
        this.stringInstructions = Data.removeNullsFromStringArray(stringInstructions);
        initLabelPosition();
        initInstructionMemory();
    }

    private void initInstructionMemory() { // adds the instruction and its position in the instructionMemory
        for (int i = 0; i < stringInstructions.length; i++) {
            Data.addInstruction(programCounter.getStartPosition() + (4 * i),
                    stringInstructions[i]);
        }
    }

    public void initLabelPosition() {
        for (int i = 0; i < stringInstructions.length; i++) {
            String label = stringInstructions[i].substring(0, stringInstructions[i].indexOf(" ")); // gets the first word in the instruction
            if (!Data.isInstruction(label)) // if it's not an instruction, then it must be a label
                labelPosition.put(label, i); // we add the label and its position in the labelPosition hashMap
        }
    }

    public static HashMap<String, Integer> getLabelPosition() {
        return labelPosition;
    }

    public void fetchInstruction() {
        int current = programCounter.getCurrentPosition(); // gets the current position that the PC is pointing to
        String stringInstruction = Data.getInstruction(current); // gets the instruction form that position
        if (stringInstruction == null) { // if there is no instruction then the program has finished
            currentInstruction = null; // set the current instruction to null
            return; // we return
        }

        currentInstruction = new Instruction(stringInstruction, programCounter); // initialize the current instruction by passing the current string instruction and the program counter
        input = programCounter.getCurrentPosition(); // input of the instruction memory is the address that the PC in pointing to
    }

    public String getDataPath() {
        return "Instruction Memory\n" +
                "Input: " + input + "\n" +
                "Output: " + currentInstruction.getMachineInstruction() + "\n" +
                "-------------------";

    }

    public Instruction getCurrentInstruction() {
        return currentInstruction;
    }

}
