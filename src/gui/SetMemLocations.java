package gui;

import main.Data;
import main.MemoryLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SetMemLocations extends JFrame implements ActionListener {
    private ArrayList<JTextField> txtLocations = new ArrayList<>();
    private ArrayList<JTextField> txtValues = new ArrayList<>();
    private JButton btnOk = new JButton("Ok");
    private JButton btnAdd = new JButton("Add");
    private JPanel pnlGrid = new JPanel(new GridLayout(0, 2));
    private JPanel pnlButtons = new JPanel();
    private MemoryLocation[] memoryLocations;
    private MainFrame mainFrame;

    public SetMemLocations(MainFrame mainFrame) {
        setTitle("Set Memory Values");
        this.mainFrame = mainFrame;
        pnlGrid.add(new JLabel("Memory Location"));
        pnlGrid.add(new JLabel("Value"));
        initTextFields();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        btnAdd.addActionListener(this);
        btnOk.addActionListener(this);
        Container c = getContentPane();
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnOk);
        c.add(pnlGrid);
        c.add(pnlButtons, BorderLayout.SOUTH);


        pack();
    }

    private void initTextFields() {
        for (int i = 0; i < txtLocations.size(); i++) {
            txtLocations.add(new JTextField());
            txtValues.add(new JTextField());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            JTextField txtLocation = new JTextField();
            JTextField txtValue = new JTextField();
            txtLocations.add(txtLocation);
            txtValues.add(txtValue);
            pnlGrid.add(txtLocation);
            pnlGrid.add(txtValue);
            pnlGrid.validate();
            pnlGrid.repaint();
            SetMemLocations.this.pack();
            repaint();
        } else if (e.getSource() == btnOk) {
            int count = 0;
            for (int i = 0; i < txtLocations.size(); i++) {
                try {
                    if (txtLocations.get(i).getText().equals("") || txtValues.get(i).getText().equals(""))
                        break;
                    Integer.parseInt(txtLocations.get(i).getText());
                    Integer.parseInt(txtValues.get(i).getText());
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null,
                            "You entered an invalid location or value",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                count++;

            }
            memoryLocations = new MemoryLocation[count];
            for (int i = 0; i < count; i++) {
                int address = Integer.parseInt(txtLocations.get(i).getText());
                int value = Integer.parseInt(txtValues.get(i).getText());
                memoryLocations[i] = new MemoryLocation(address, value);
            }
            Data.initDataMemory(memoryLocations);
            SetMemLocations.this.dispose();
            mainFrame.setVisible(true);
        }
    }
}
