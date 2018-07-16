package main;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Data {

    private static HashMap<String, Integer> ISA = new HashMap<String, Integer>() {{
        put("add", 0);
        put("sll", 0);
        put("nor", 0);
        put("jr", 0);
        put("slt", 0);
        put("addi", 8);
        put("lw", 35);
        put("sw", 43);
        put("lb", 32);
        put("lbu", 36);
        put("sb", 40);
        put("beq", 4);
        put("j", 2);
        put("jal", 3);
        put("slti", 11);
    }}; // contains the opcode for each instruction

    private static HashMap<String, Integer> extendedOpCode = new HashMap<String, Integer>() {{
        put("add", 32);
        put("sll", 0);
        put("nor", 39);
        put("jr", 8);
        put("slt", 42);
    }}; // contains the extended opcode foe the R-type instructions

    private static HashMap<String, Integer> registerNumber = new HashMap<String, Integer>() {{
        put("$zero", 0);
        put("$0", 0);

        put("$at", 1);
        put("$v0", 2);
        put("$v1", 3);

        put("$a0", 4);
        put("$a1", 5);
        put("$a2", 6);
        put("$a3", 7);

        put("$t0", 8);
        put("$t1", 9);
        put("$t2", 10);
        put("$t3", 11);
        put("$t4", 12);
        put("$t5", 13);
        put("$t6", 14);
        put("$t7", 15);

        put("$s0", 16);
        put("$s1", 17);
        put("$s2", 18);
        put("$s3", 19);
        put("$s4", 20);
        put("$s5", 21);
        put("$s6", 22);
        put("$s7", 23);

        put("$t8", 24);
        put("$t9", 25);

        put("$k0", 26);
        put("$k1", 27);

        put("$gp", 28);

        put("$sp", 29);

        put("$fp", 30);

        put("$ra", 31);
    }}; // each register with its number

    private static HashMap<String, Integer> registerValue = new HashMap<String, Integer>() {{
        put("$zero", 0);
        put("$0", 0);
    }}; // each register with its value

    private static HashMap<Integer, String> instructionMemory = new HashMap<>(); // contains each instruction with its location in memory

    private static ArrayList<MemoryLocation> dataMemory = new ArrayList<>(); // contains the data loaded/stored in memory with its location

    public static String[] registersNames = {"$0", "$at", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3",
            "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0",
            "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8",
            "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra"};


    public static int getOpCode(String instruction) {  // returns the opcode of given instruction
        return ISA.get(instruction);
    }

    public static int getExtendedOpCode(String instruction) { // returns the extended opcode for R-type instructions
        return extendedOpCode.get(instruction);
    }

    public static int getRegisterNumber(String register) { // gets the register number of a given register name
        return registerNumber.get(register);
    }

    public static void setRegisterValue(int register, int value) { // sets the value of a register, unless it's register zero
        String registerName = getRegisterName(register);
        if (registerName.equals("$0") || registerName.equals("$zero")) {
            return;
        }
        registerValue.put(registerName, value);
    }

    public static void setRegistersValues(int[] values) { // set the registers in the registersNames array with the values in the second array
        for (int i = 0; i < registersNames.length; i++)
            registerValue.put(registersNames[i], values[i]);
        registerValue.put("$zero", 0);
    }

    public static void resetRegisters() { // reset the values for all the registers to 0
        for (String register : registerValue.keySet()) {
            registerValue.put(register, 0);
        }
    }

    public static int getRegisterValue(int register) {
        String registerName = getRegisterName(register); // gets the name of a given register
        Integer value = registerValue.get(registerName); // gets the value of the register by its name
        if (value == null)
            return 0;
        return value;
    }

    public static int[] getRegistersValues() { // gets the values of all the registers stored in the register file
        int[] values = new int[32];
        for (int i = 0; i < registersNames.length; i++) {
            values[i] = registerValue.get(registersNames[i]);
        }
        return values;
    }

    public static String getRegisterName(int register) { // gets the name of a register by its number
        String registerName = null;
        for (String name : registerNumber.keySet()) // loop through the keys if the registerNumber hashMap
            if (getRegisterNumber(name) == register) // if the number of one of the registers equals the required number
                registerName = name; // let the name equals the name of the found register
        return registerName;
    }

    public static String extendToNBits(int value, int bits) { // returns the value in binary format in the given number of bits
        String binary = Integer.toBinaryString(value); // gets the binary representation of the value and stores it in a string
        int length = binary.length();
        if (length == bits) // if the length of the string is equal to the number of bits, then we return the string
            return binary;
        if (length > bits) // if the length is larger, then it's a negative number and we only want the bits from 16 to 32
            return binary.substring(16, 32);

        // if the string length is less than the number of bits
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bits - length; i++) // we loop to add the remaining bits(zeros) at the left of the number
            builder.append("0");

        builder.append(binary);  // we add the value of the integer
        return builder.toString();
    }

    public static void addToMemory(MemoryLocation locationValue) {
        for (MemoryLocation memoryLocation : dataMemory) { // loop through the memoryLocations stored in the dataMemory
            if (locationValue.getAddress() == memoryLocation.getAddress()) { // if the address of the new memoryLocation is founds in the dataMemory
                memoryLocation.setValue(locationValue.getValue()); // we update the value stored in that location
                return;
            }
        }
        dataMemory.add(locationValue); // the address was not found so we add the address with the value
    }

    public static void initDataMemory(MemoryLocation[] locationsValues) { // initialize the dataMemory arrayList with the given values
        for (MemoryLocation memoryLocation : locationsValues)
            dataMemory.add(memoryLocation);
    }

    public static void resetDataMemory() { // deletes the information in the arrayLst by making the reference points to a new empty arrayList
        dataMemory = new ArrayList<>();
    }

    public static int getFromMemory(int address) { // returns the value from the given address
        for (MemoryLocation memoryLocation : dataMemory)  // loop through the dataMemory
            if (address == memoryLocation.getAddress())  // if the given address equals an address of a stored memoryLocation
                return memoryLocation.getValue(); // return the value with that address
        return 0; // the address was not found so we return zero
    }

    public static MemoryLocation[] getAllMemoryValues() { // gets all the data stored in the dataMemory and return it
        MemoryLocation[] memoryLocations = new MemoryLocation[dataMemory.size()];
        dataMemory.toArray(memoryLocations);
        return memoryLocations;
    }

    public static boolean isInstruction(String instruction) { // checks if the given string is a command (supported instruction)
        return ISA.get(instruction) != null;
    }

    public static boolean isRegister(String register) {  // checks if the given string is a register
        return registerNumber.get(register) != null;
    }

    public static String getInstruction(int position) { // returns the instruction in the given position in the instructionMemory
        return instructionMemory.get(position);
    }

    public static void addInstruction(int position, String instruction) { // adds the instruction to the instructionMemory
        String instructionNoLabel = removeLabel(instruction); // removes the label if found
        if (instructionMemory.get(position) == null) // checks that the instruction isn't already stored
            instructionMemory.put(position, instructionNoLabel); // adds the position (location) and the instruction
    }

    private static String removeLabel(String instruction) { // returns the instruction without the label
        String[] split = instruction.split(" ");
        if (isInstruction(split[0])) // if the first word in the instruction is supported instruction, then there is no label
            return instruction; // return the actual instruction

        // the first word is a label so we remove it
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < split.length; i++) // we loop from the second word till the end
            sb.append(split[i]).append(" "); // we add them to a stringBuilder

        return sb.toString(); // we return the string
    }

    public static String[] getRegistersNames() {
        return registersNames;
    }

    public static String[] removeNullsFromStringArray(String[] initialSplit) { // takes a string array and return the same array but without any empty strings
        ArrayList<String> removedEmptyStrings = new ArrayList<>();
        for (String split : initialSplit) // loop through the given string
            if (!split.equals("")) // if the string doesn't equals "" (empty)
                removedEmptyStrings.add(split); // we add the string to the ArrayList

        initialSplit = new String[removedEmptyStrings.size()]; // we initialize the array with the size of the ArrayList
        for (int i = 0; i < initialSplit.length; i++)
            initialSplit[i] = removedEmptyStrings.get(i); // make every element equals to the corresponding element in the ArrayList
        return initialSplit;
    }
}
