package gui;

import main.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    private PC pc;
    private String[] instructions;
    private InstructionMemory instructionMemory;
    private SetRegisters setRegisters;

    private JTextArea txArea = new JTextArea(20, 60);
    private int startPosition;


    private JMenuBar bar = new JMenuBar();

    private JMenu mnuEdit = new JMenu("Edit");
    private JMenu mnuShow = new JMenu("Show");

    private JMenuItem mniEditRegisters = new JMenuItem("Edit Registers...");
    private JMenuItem mniEditMemoryLocations = new JMenuItem("Edit Data Memory...");
    private JMenuItem mniReset = new JMenuItem("Reset");

    private JMenuItem mniShowOutputValues = new JMenuItem("Show Output Values...");

    private JButton btnStart = new JButton("Start");


    public MainFrame() {
        setTitle("Main Frame");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mniEditRegisters.addActionListener(this);
        mniEditMemoryLocations.addActionListener(this);
        mniReset.addActionListener(this);
        btnStart.addActionListener(this);
        mniShowOutputValues.addActionListener(this);


        mnuEdit.add(mniEditRegisters);
        mnuEdit.add(mniEditMemoryLocations);
        mnuEdit.add(mniReset);

        mnuShow.add(mniShowOutputValues);

        bar.add(mnuEdit);
        bar.add(mnuShow);
        setJMenuBar(bar);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        txArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(30,30,30,30)));
        txArea.setFont(new Font("Arial Black", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(txArea);

        Container c = getContentPane();
        c.add(scrollPane);

        c.add(btnStart, BorderLayout.SOUTH);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mniEditRegisters) {
            setRegisters = new SetRegisters(MainFrame.this);
            MainFrame.this.dispose();
            setRegisters.setVisible(true);
        } else if (e.getSource() == mniEditMemoryLocations) {
            SetMemLocations setMemLocations = new SetMemLocations(MainFrame.this);
            MainFrame.this.dispose();
            setMemLocations.setVisible(true);
        } else if (e.getSource() == mniReset) {
            Data.resetRegisters();
            Data.resetDataMemory();
            JOptionPane.showConfirmDialog(null, "Registers and memory values were deleted",
                    "Success", JOptionPane.DEFAULT_OPTION);
        }else if (e.getSource() == mniShowOutputValues) {
            ShowRegistersMemory showRegistersMemory = new ShowRegistersMemory(MainFrame.this);
            MainFrame.this.dispose();
            showRegistersMemory.setVisible(true);
        } else if (e.getSource() == btnStart) {

            instructions = txArea.getText().split("\n"); // get all the text in the textArea
            start();
        }

    }

    private void start() {
        startPosition = setRegisters.getStartPosition();
        pc = new PC(startPosition);
        instructionMemory = new InstructionMemory(instructions, pc);

        while (true) {
            Adder pcAdder = new Adder(pc.getCurrentPosition(), 4);
            int nextInstructionLocation = pcAdder.getResult();

            instructionMemory.fetchInstruction();
            Instruction instruction = instructionMemory.getCurrentInstruction();

            if (instruction == null)
                break;

//            System.out.println(instruction.getStringInstruction());

//            System.out.println(instructionMemory.getDataPath());


            Control control = new Control(instruction.getOpCode(), instruction.getFunctionCode());
//            System.out.println(control.getDataPath());


            ALUControl ALUControl = new ALUControl(control.getALUOp(), instruction.getFunctionCode());

            Mux muxRegisterDst = new Mux(instruction.getSecondSource(), instruction.getDestination(), control.getRegisterDest());

            Mux muxRegisterDstJump = new Mux(muxRegisterDst.getOutput(), 31, control.getRegisterDestJump());

            RegisterFile registerFile = new RegisterFile(instruction.getSource(), instruction.getSecondSource(),
                    muxRegisterDstJump.getOutput(), control.getRegisterWrite());
//            System.out.println(registerFile.getDataPath());

//            System.out.println(ALUControl.getDataPath());

            int firstALUInput = registerFile.getReadData1();

            SignExtend signExtend = new SignExtend(instruction.getConstant());
            int extendedConstant = signExtend.getOutput();
//            System.out.println(signExtend.getDataPath());

            Mux muxALUSecondSource = new Mux(registerFile.getReadData2(), extendedConstant, control.getALUSource());
            int secondALUInput = muxALUSecondSource.getOutput();

            ALU alu = new ALU(firstALUInput, secondALUInput, instruction.getShiftAmount(), ALUControl.getALUSignal());
            int aluResult = alu.getOutput();
//            System.out.println(alu.getDataPath());

            DataMemory dataMemory = new DataMemory(control.getMemoryWrite(), control.getMemoryRead(),
                    control.getMemoryByte(), control.getMemoryUnsigned());
            dataMemory.initLocationValue(aluResult, registerFile.getReadData2());
            dataMemory.addToMemory();
            int memoryOutput = dataMemory.getOutput();
//            System.out.println(dataMemory.getDataPath());

            Mux muxWrittenValue = new Mux(aluResult, memoryOutput, control.getMemoryToRegister());
            Mux muxWrittenValueJump = new Mux(muxWrittenValue.getOutput(), nextInstructionLocation, control.getRegisterDestJump());
            int writtenValue = muxWrittenValueJump.getOutput();

            ShiftLeft shiftLeft = new ShiftLeft(extendedConstant);
            int extendedShiftedConstant = shiftLeft.getOutput();
//            System.out.println(shiftLeft.getDataPath());

            Adder branchAdder = new Adder(nextInstructionLocation, extendedShiftedConstant);
            int branchLocation = branchAdder.getResult();
//            System.out.println(branchAdder.getDataPath());

            AND and = new AND(control.getBranch(), alu.getZeroFlag());
            int branchControl = and.getOutput();
//            System.out.println("And: " + branchControl + "\n---------------------");

            Mux branchMux = new Mux(nextInstructionLocation, branchLocation, branchControl);
            int nextInstruction = branchMux.getOutput();

            int jumpAddress = instruction.getJumpAddress();
            String stringJumpAddress = Data.extendToNBits(jumpAddress, 26);
            stringJumpAddress += "00";
            stringJumpAddress = pc.get4Bits() + stringJumpAddress;
            jumpAddress = Integer.parseInt(stringJumpAddress, 2);
            Mux jumpMux = new Mux(nextInstruction, jumpAddress, control.getJump());
            nextInstruction = jumpMux.getOutput();

            Mux jumpRegisterMux = new Mux(nextInstruction, registerFile.getReadData1(), control.getJumpRegister());
            nextInstruction = jumpRegisterMux.getOutput();

            pc.setNextPosition(nextInstruction);

            registerFile.setWriteData(writtenValue);
            registerFile.changeRegister();

//            if (control.getRegisterWrite() == 1) {
//                int finalValue = Data.getRegisterValue(registerFile.getWriteRegister());
//                System.out.print("Value of register: " + Data.getRegisterName(registerFile.getWriteRegister())
//                        + " Was changes to: ");
//                System.out.println((finalValue));
//            }
//            System.out.println("-----------------INSTRUCTION FINISHED--------------------");
        }
        JOptionPane.showConfirmDialog(null, "Total Cycles: " + pc.getCycles(),
                "Cycles", JOptionPane.DEFAULT_OPTION);
//        System.out.println("Total cycles: " + pc.getCycles());
    }
}
