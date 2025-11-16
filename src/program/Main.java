package program;

import java.awt.*;
import java.lang.classfile.ClassFile.Option;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import java.util.*;
import java.util.Vector;
import java.awt.event.*;

import entity.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.StyleSheet.BoxPainter;
import javax.swing.text.html.parser.Entity;
import javax.swing.text.Document;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author braydenphanna
 */

public class Main extends javax.swing.JFrame {
    private ArrayList<entity.Item> menu = new ArrayList<entity.Item>();
    private static ItemDAO itemDAO = new ItemDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private int orderIndex = 0;
    private int itemIndex = 0;

    private JList<String> menuItemNames;
    private JScrollPane menuScrollPane;
    private JTextField searchField;
    private DefaultListModel<String> listModel;

    private JComboBox<String> categoryComboBox;

    JLabel subtotalAmount;
    JLabel taxAmount;
    JLabel totalAmount;

    public Main(){
        // Populates the menu
        menu.add(new Item(0,"Glazed Donut", 1.49, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(1,"Donut w/ Sprinkles", 1.79, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(2,"Breakfast Sandwich", 4.50, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(3,"Latte", 3.00, "Icing,Chocolate,Vanilla\nFilling,Jelly,Cream"));
        menu.add(new Item(4,"House Coffee", 2.00,"Sugar,A little,A lot\nCream,A little,A lot"));

        initComponents();
    }
    // Does the bulk of setting up the gui elements
    private void initComponents(){
        setTitle("Oak Donuts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack(); 
        setLocationRelativeTo(null);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setLayout(new BorderLayout());

        // TITLE PANEL
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        titlePanel.setBackground(Color.LIGHT_GRAY);
        add(titlePanel, BorderLayout.NORTH);

        JLabel title = new JLabel("Oak Donuts");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        titlePanel.add(title);

        // WEST PANEL
        
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        westPanel.setPreferredSize(new Dimension(150,600));
        add(westPanel, BorderLayout.WEST);

        JLabel fitlersLabel= new JLabel("Filters");
        fitlersLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        fitlersLabel.setForeground(Color.BLACK);
        fitlersLabel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
        fitlersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(fitlersLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        categoryLabel.setForeground(Color.BLACK);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(categoryLabel);

        categoryComboBox = new JComboBox<>(new String[]{"All","Food","Drinks"});
        categoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMenuItems();
            }
        });

        categoryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(categoryComboBox);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        searchLabel.setForeground(Color.BLACK);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        Document doc = searchField.getDocument();

        listModel = new DefaultListModel<>();
        menuItemNames = new JList<String>(listModel);

        doc.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateMenuItems();
            }
            public void removeUpdate(DocumentEvent e) {
                updateMenuItems();
            }
            public void changedUpdate(DocumentEvent e) {
            }
        });

        westPanel.add(searchField);
       
        System.out.print(menuItemNames.getSelectedIndex());
        westPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JLabel itemOptionsLabel= new JLabel("Item Options");
        itemOptionsLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        itemOptionsLabel.setForeground(Color.BLACK);
        itemOptionsLabel.setVisible(false);
        itemOptionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(itemOptionsLabel);

        JLabel optionLabel = new JLabel();
        optionLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        optionLabel.setForeground(Color.BLACK);
        optionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionLabel);

        JComboBox<String> optionComboBox = new JComboBox<>();
        optionComboBox.setVisible(false);
        optionComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionComboBox);

        JLabel optionLabel2 = new JLabel();
        optionLabel2.setFont(new Font("Verdana", Font.BOLD, 12));
        optionLabel2.setForeground(Color.BLACK);
        optionLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionLabel2);

        JComboBox<String> optionComboBox2 = new JComboBox<>();
        optionComboBox2.setVisible(false);
        optionComboBox2.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(optionComboBox2);

        
        menuItemNames.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() && !menuItemNames.isSelectionEmpty()) {  
                    if(menuItemNames.getSelectedIndex()>=0){
                        generateItemOptions(menuItemNames, itemOptionsLabel, optionLabel, optionComboBox, optionLabel2, optionComboBox2);
                    }
                }  
            }
        });
        
        westPanel.add(Box.createVerticalGlue());

        westPanel.add(Box.createVerticalStrut(600));

        // CENTER PANEL

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(centerPanel, BorderLayout.CENTER);
        
        JLabel menulabel= new JLabel("Menu", SwingConstants.CENTER);
        menulabel.setFont(new Font("Verdana", Font.BOLD, 16));
        menulabel.setBorder(BorderFactory.createLineBorder(Color.RED));
        menulabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(menulabel);

        menuScrollPane = new JScrollPane(menuItemNames);
        updateMenuItems();
        centerPanel.add(menuScrollPane);

        JPanel lowerCenterPanel = new JPanel();
        lowerCenterPanel.setLayout(new BoxLayout(lowerCenterPanel, BoxLayout.X_AXIS));
        centerPanel.add(lowerCenterPanel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        quantitySpinner.setMaximumSize(new Dimension(80,80));
        lowerCenterPanel.add(quantitySpinner);

        javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(
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
        };

        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = menuItemNames.getSelectedValue();
                int selectedItemIndex = menuItemNames.getSelectedIndex();
                if (selectedItem != null) {
                    // Perform actions with the selected item
                    System.out.println("Selected item: " + selectedItem);

                    System.out.println("Selected item index: " + selectedItemIndex);
                    Item i = menu.get(selectedItemIndex);
                    String optionsString = i.getOptions()[0][0] + ": "+optionComboBox.getSelectedItem().toString()+"\n"+i.getOptions()[1][0] + ": "+optionComboBox2.getSelectedItem().toString();

                    for(int j = 0; j<(int)quantitySpinner.getValue(); j++){
                        addItem(itemIndex++, i.getName(), i.getPrice(), optionsString);
                        if(getOrder(orderIndex).getID()==-1){
                            addOrder(orderIndex,i.getPrice(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),i.getName());
                            System.out.println("NEW ORDER " + getOrder(orderIndex).getID());
                        }else{
                            updateOrder(orderIndex,getOrder(orderIndex).getPrice()+i.getPrice(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),i.getName());
                            System.out.println("UPDATE ORDER " + getOrder(orderIndex).getID()+" | ITEMS: "+ itemIndex);
                        }
                    }
                    updateOrderTable(dtm, i, (int)quantitySpinner.getValue(), optionsString);
                    updatePrices(subtotalAmount,taxAmount,totalAmount);
                    quantitySpinner.setValue(1);
                    menuItemNames.clearSelection();
                } else {
                    System.out.println("No item selected.");
                }
            }
        });
        lowerCenterPanel.add(addButton);


        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(eastPanel, BorderLayout.EAST);

        JTable orderTable = new JTable();
        
        int o = 0;
        while(getOrder(o).getID()!=-1){
            o++;
        }
        orderIndex=o;
        int i = 0;
        while(getItem(i).getID()!=-1){  
            Item item = getItem(i);
            String[] options = item.getOptionsAsString().split("\n");
            
            boolean found = false;
            for(int j =0; j<dtm.getRowCount(); j++){
                Item previouslyAddedItem = getItem(j);
                System.out.print(previouslyAddedItem.getName() +" == "+item.getName() +" & "+previouslyAddedItem.getOptionsAsString() +" = "+item.getOptionsAsString());
                if(previouslyAddedItem.getName().equals(item.getName()) && previouslyAddedItem.getOptionsAsString().equals(item.getOptionsAsString())){
                    dtm.setValueAt((int)dtm.getValueAt(j, 2)+1, j, 2);
                    dtm.setValueAt(Double.parseDouble((dtm.getValueAt(j, 4).toString().replace("$", "")))+item.getPrice(), j, 4);
                    found = true;
                    break;
                }
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if(found==false) dtm.addRow(new Object[]{item.getName(), options[0]+", " +options[1], 1,"$"+df.format(item.getPrice()),"$"+df.format(item.getPrice())});
            i++;
        }
        itemIndex=i;
        orderTable.setModel(dtm);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 500));
        eastPanel.add(orderScroll);

        JPanel lowerEastPanel = new JPanel();
        lowerEastPanel.setLayout(new BorderLayout());

        JPanel lowerEastEastPanel = new JPanel();
        lowerEastEastPanel.setLayout(new BoxLayout(lowerEastEastPanel, BoxLayout.Y_AXIS));
        JLabel subtotalLabel = new JLabel("Subtotal:");
        lowerEastEastPanel.add(subtotalLabel);
        JLabel taxLabel = new JLabel("Tax (6%):");
        lowerEastEastPanel.add(taxLabel);
        JLabel totalPanel = new JLabel("Total:");
        totalPanel.setFont(new Font("Verdana", Font.BOLD, 16));
        lowerEastEastPanel.add(totalPanel);


        lowerEastPanel.add(lowerEastEastPanel, BorderLayout.WEST);

        JPanel lowerEastWestPanel = new JPanel();
        lowerEastWestPanel.setLayout(new BoxLayout(lowerEastWestPanel, BoxLayout.Y_AXIS));
        
        subtotalAmount = new JLabel("$0.00");
        taxAmount = new JLabel("$0.00");
        totalAmount = new JLabel("$0.00");
        totalAmount.setFont(new Font("Verdana", Font.BOLD, 16));
        if(getOrder(orderIndex).getID()!=-1) updatePrices(subtotalAmount,taxAmount,totalAmount);
        lowerEastWestPanel.add(subtotalAmount);
        lowerEastWestPanel.add(taxAmount);
        lowerEastWestPanel.add(totalAmount);

        JPanel lowerlowerEastPanel = new JPanel();
        lowerlowerEastPanel.setLayout(new BoxLayout(lowerlowerEastPanel, BoxLayout.X_AXIS));
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = itemIndex-1;
                Item item;
                while(getItem(i).getID()>=0){  
                    item = getItem(i);
                    deleteItem(item.getID(), item.getName(), item.getPrice(), item.getOptions());
                    i--;
                }
                Order order = getOrder(orderIndex);
                deleteOrder(order.getID(),order.getPrice(), order.getDateTime(), order.getItemName());

                itemIndex=0;
                dtm.setRowCount(0);

                subtotalAmount.setText("$0.00");
                taxAmount.setText("$0.00");
                totalAmount.setText("$0.00");
            }
        });
        lowerlowerEastPanel.add(clearButton);
        
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dtm.getRowCount()>0){
                    int i = itemIndex-1;

                    Item item;
                    while(getItem(i).getID()>=0){  
                        item = getItem(i);
                        deleteItem(item.getID(), item.getName(), item.getPrice(), item.getOptions());
                        i--;
                    }

                    dtm.setRowCount(0);

                    DecimalFormat df = new DecimalFormat("#.00");
                    JOptionPane popup = new JOptionPane();
                    popup.showMessageDialog(null, "Thank you for your purchase!\nYour total is $"+ df.format(getOrder(orderIndex).getPrice()));
                    itemIndex=0;
                    orderIndex++;
                    
                }
            }
        });
        lowerlowerEastPanel.add(checkoutButton);
        
        lowerEastPanel.add(lowerEastWestPanel, BorderLayout.EAST);

        eastPanel.add(lowerEastPanel);
        eastPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        eastPanel.add(lowerlowerEastPanel);

        setVisible(true);
    }

    private void generateItemOptions(JList<String> menuItemNames, JLabel itemsOptionsLabel, JLabel option1, JComboBox combo1, JLabel option2, JComboBox combo2){
        itemsOptionsLabel.setVisible(true);
        Item item = menu.get(menuItemNames.getSelectedIndex());
        option1.setText(item.getOptions()[0][0]+":");
        combo1.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[0][1],item.getOptions()[0][2]}));
        combo1.setVisible(true);
        try {
            option2.setText(item.getOptions()[1][0]+":");
            combo2.setModel(new DefaultComboBoxModel<>( new String[]{item.getOptions()[1][1],item.getOptions()[1][2]}));
            combo2.setVisible(true);
        } catch (Exception e){

        }
        revalidate();
    }

    private String[] filterMenuItems(String query){
        String category = (String)categoryComboBox.getSelectedItem();

        String[] menuStringArr = new String[menu.size()];
        int i = 0;
        int j = menu.size();
        if(category=="Food") {i = 0; j = 3;}
        if(category=="Drinks") {i = 3;  j = 5;}
        for(;i < j; i++){
            String menuItemAsString = menu.get(i).toString();
            if(menuItemAsString.toUpperCase().contains(query.toUpperCase())){
                menuStringArr[i] = menuItemAsString;
            }
        }
        return menuStringArr;
    }
    private void updateMenuItems(){
        String[] filteredItems = filterMenuItems(searchField.getText());

        // Clear the current list model (this removes existing items)
        listModel.clear();

        // Add filtered items to the list model
        for (String item : filteredItems) {
            listModel.addElement(item);  // Adds an item to the list model
        }

        // Revalidate and repaint the JScrollPane containing the JList
        menuScrollPane.revalidate();
        menuScrollPane.repaint();
    }

    private void updateOrderTable(DefaultTableModel dtm, Item item, int quanity, String optionsString){
        String[] options =optionsString.split("\n");
        DecimalFormat df = new DecimalFormat("#.00");
        dtm.addRow(new Object[]{item.getName(), options[0]+", " +options[1], quanity,"$"+df.format(item.getPrice()),"$"+df.format(item.getPrice()*quanity)});
    }
    private void updatePrices(JLabel subtotalAmount, JLabel taxAmount, JLabel totalAmount){
        DecimalFormat df = new DecimalFormat("#.00");
        subtotalAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()));
        taxAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()*0.06));
        totalAmount.setText("$"+(df.format((getOrder(orderIndex).getPrice()+getOrder(orderIndex).getPrice()*0.06))));
    }
    /**
     * ITEM CRUD FUNCTIONS
    */
    private static void addItem(int id, String name, double price, String options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.insert(item);
    }
    
    private static void updateItem(int id, String name, double price, String options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.update(item);
    }
    
    private static void deleteItem(int id, String name, double price, String[][] options) {
        Item item;
        item = new Item(id, name, price, options);
        itemDAO.delete(item);
    }
    
    static Item getItem(int id) {
        Optional<Item> item = itemDAO.get(id);
        return item.orElseGet(() -> new Item(-1, "Non-exist", -1,"Non-exist"));
    }

    /**
     * ORDER CRUD FUNCTIONS
    */
    private static void addOrder(int ID, double price, String dateTime,  String itemName) {
        Order order;
        order = new Order(ID, price, dateTime, itemName);
        orderDAO.insert(order);
    }
    
    private static void updateOrder(int ID, double price, String dateTime, String itemName) {
        Order order;
        order = new Order(ID, price, dateTime, itemName);
        orderDAO.update(order);
    }
    
    private static void deleteOrder(int ID, double price, String dateTime, String itemName) {
        Order order;
        order = new Order(ID, price, dateTime, itemName);
        orderDAO.delete(order);
    }
    
    static Order getOrder(int id) {
    Optional<Order> order = orderDAO.get(id);
    if (order!=null && order.isPresent()) {
        return order.get();
    } else {
        System.out.println("Order with ID " + id + " not found.");
        return new Order(-1, -1, "Non-exist", "Non-exist");
    }
}

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}
