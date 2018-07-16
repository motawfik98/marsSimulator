package gui;

import main.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetRegisters extends JFrame implements ActionListener {
    private static final int COLUMNS = 5;
    private int startPosition;
    private JLabel lblStartPosition = new JLabel("Start Position", JLabel.TRAILING);
    private JTextField txtStartPosition = new JTextField("0", COLUMNS);
    private JPanel pnlGrid = new JPanel(new GridLayout(17, 2, 20, 10));
    private JButton btnOk = new JButton("Ok");


    private JLabel[] lbls;

    private JTextField[] txts;

    private String[] registers;
    private int[] values;

    MainFrame mainFrame;

    public SetRegisters(MainFrame mainFrame) {
        setTitle("Set Registers");
        this.mainFrame = mainFrame;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        registers = new String[32];
        values = new int[32];
        setLabels();
        setRegistersNames();
        setTextFields();
        txts[0].setEnabled(false);

        Container c = getContentPane();
        addRegistersTextFields();


        btnOk.addActionListener(this);
        c.add(pnlGrid);
        c.add(btnOk, BorderLayout.SOUTH);
        pack();

    }

    private void addRegistersTextFields() {
        for (int i = 0; i < lbls.length; i++) {
            pnlGrid.add(lbls[i]);
            lbls[i].setLabelFor(txts[i]);
            pnlGrid.add(txts[i]);
        }
        lblStartPosition.setLabelFor(txtStartPosition);
        pnlGrid.add(lblStartPosition);
        pnlGrid.add(txtStartPosition);

    }

    private void setRegistersNames() {
        for (int i = 0; i < lbls.length; i++)
            registers[i] = lbls[i].getText();
    }

    private void setLabels() {
        lbls = new JLabel[Data.registersNames.length];
        for (int i = 0; i < lbls.length; i++)
            lbls[i] = new JLabel(Data.registersNames[i], JLabel.TRAILING);
    }

    private void setTextFields() {
        txts = new JTextField[lbls.length];
        for (int i = 0; i < txts.length; i++) { // gets the value of the register number i from the hashMap and assign its value to the textField
            txts[i] = new JTextField(String.valueOf(Data.getRegisterValue(i)), COLUMNS);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            startPosition = Integer.parseInt(txtStartPosition.getText());
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(null,
                    "You must enter a valid location to start the program",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < txts.length; i++) {
            try {
                int value = Integer.parseInt(txts[i].getText());
                values[i] = value;

            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null,
                        "You must enter a valid integer in " + lbls[i].getText(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Data.setRegistersValues(values);
        SetRegisters.this.dispose();
        mainFrame.setVisible(true);

    }

    public int getStartPosition() {
        return startPosition;
    }
}
