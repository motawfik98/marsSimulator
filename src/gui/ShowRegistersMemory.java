package gui;

import main.Data;
import main.MemoryLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowRegistersMemory extends JFrame implements ActionListener {
    private JPanel pnlRegisters = new JPanel(new GridLayout(17, 2, 50, 20));
    private String[] registersNames = Data.getRegistersNames();
    private int[] registersValues = Data.getRegistersValues();
    private MemoryLocation[] memoryLocations = Data.getAllMemoryValues();
    private JLabel[] lblMemoryValues = new JLabel[memoryLocations.length];
    private JPanel pnlMemory = new JPanel(new GridLayout(memoryLocations.length / 2, 2, 50, 10));
    private JPanel pnlValues = new JPanel(new FlowLayout());
    private JLabel[] lblRegistersValues = new JLabel[32];
    private MainFrame mainFrame;
    private JButton btnOk = new JButton("Ok");

    public ShowRegistersMemory(MainFrame mainFrame) {
        setTitle("Output");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.mainFrame = mainFrame;
        Container c = getContentPane();


        btnOk.addActionListener(this);
        addRegistersValues();
        addMemoryValues();
        c.setLayout(new BorderLayout());
        pnlRegisters.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Registers Values"));
        pnlValues.add(pnlRegisters);
        pnlMemory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Memory Value"));
        pnlValues.add(pnlMemory);
        c.add(btnOk, BorderLayout.SOUTH);
        c.add(pnlValues);
        pack();

    }

    private void addMemoryValues() {
        for (int i = 0; i < memoryLocations.length; i++) {
            lblMemoryValues[i] = new JLabel(memoryLocations[i].getAddress() + " = " + memoryLocations[i].getValue());
            pnlMemory.add(lblMemoryValues[i]);
        }
    }

    private void addRegistersValues() {
        for (int i = 0; i < registersNames.length; i++) {
            lblRegistersValues[i] = new JLabel(registersNames[i] + " = " + registersValues[i]);
            pnlRegisters.add(lblRegistersValues[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ShowRegistersMemory.this.dispose();
        mainFrame.setVisible(true);
    }
}
