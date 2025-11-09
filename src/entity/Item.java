package entity;

import java.util.ArrayList;

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

    public int getID() { return ID; }

    public String getName() { return name; }

    public double getPrice() { return price; }

    public ArrayList<String> getOptions() { return options; }

    @Override
    public String toString() {
        return "Customer{" + "ID=" + ID + ", name=" + name + ", price=" + price + ", options=" + options + '}';
    }
}
