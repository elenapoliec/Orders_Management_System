package Model;

/**
 * Reprezintă entitatea Produs din baza de date.
 * Această clasă stochează informațiile despre produsele din depozit, inclusiv stocul și prețul.
 * * @author Poliec Elena
 * @version 1.0
 */

public class Product {
    private int id;
    private String name;
    private int stock;
    private double price;


    /**
     * Constructor implicit (fără parametri) utilizat de tehnologia Reflection.
     */
    // Constructor gol - Obligatoriu pentru Reflection!
    public Product() {
    }

    /**
     * Constructor utilizat pentru editarea sau preluarea unui produs existent din baza de date.
     * * @param id    ID-ul unic al produsului.
     * @param name  Denumirea produsului.
     * @param stock Cantitatea disponibilă în stoc.
     * @param price Prețul unitar al produsului.
     */

    public Product(int id, String name, int stock, double price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    /**
     * Constructor utilizat pentru crearea unui produs nou în sistem.
     * * @param name  Denumirea produsului.
     * @param stock Cantitatea inițială în stoc.
     * @param price Prețul unitar al produsului.
     */

    // Constructor fara ID (folositor pentru INSERT unde ID-ul e Auto-Increment)
    public Product(String name, int stock, double price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    // getters si setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", stock=" + stock + ", price=" + price + "]";
    }
}
