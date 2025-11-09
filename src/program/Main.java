package program;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.*;

/**
 * @author braydenphanna
 */

public class Main extends javax.swing.JFrame {
    public Main(){
        setTitle("Oak Donuts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack(); 
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        add(titlePanel, BorderLayout.NORTH);

        JPanel westPanel = new JPanel();
        westPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        add(westPanel, BorderLayout.WEST);

        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        add(eastPanel, BorderLayout.EAST);

        JLabel title = new JLabel("Oak Donuts");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        titlePanel.add(title);

        List test = new List(4);
        test.add("test");
        test.add("testtoo");

        //Put full menu as in argument of JScrollPane
        JScrollPane menu = new JScrollPane(test);
        menu.setPreferredSize(new Dimension(200, 400));
        westPanel.add(menu);

        JButton addButton = new JButton("Add to Order");
        westPanel.add(addButton);

        JTable orderTable = new JTable();
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Options", "Qty", "Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false,false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.setCellSelectionEnabled(true);
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 400)); // adjust size as you like
        eastPanel.add(orderScroll);

        setVisible(true);
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}
