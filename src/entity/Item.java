package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;

/**
 * @author braydenphanna
 */
public class Item 
{
    private int ID;
    private String name;
    private double price;
    private ArrayList<String> options;
    
    public Item(int ID, String name, double price, ArrayList<String> options){
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.options = options;
    }
    public Item(int ID, String name, double price, String options){
        this.ID = ID;
        this.name = name;
        this.price = price;

        String[] stringArray = options.split(",");
        List<String> stringList = Arrays.asList(stringArray);
        this.options = new ArrayList<>(stringList);
    }

    public int getID() { return ID; }

    public String getName() { return name; }

    public double getPrice() { return price; }

    public ArrayList<String> getOptions() { return options; }
    
    public String getOptionsAsString() { return String.join(",",options); }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return name + " — $"+df.format(price);
    }
}
