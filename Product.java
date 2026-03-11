// --- Product.java ---
// A simple class (POJO) to hold product data
public class Product {
    String name;
    double price;
    String imageFileName; // NEW FIELD

    public Product(String name, double price, String imageFileName) { // CONSTRUCTOR UPDATED
        this.name = name;
        this.price = price;
        this.imageFileName = imageFileName; // NEW
    }
    
    // Overloaded constructor for old code (just in case, not really needed now)
    public Product(String name, double price) {
        this(name, price, "default.png"); // Default image if none provided
    }

    @Override
    public String toString() {
        return name + " - ₹" + String.format("%.2f", price);
    }

    // Implement equals and hashCode so this can be a Key in a HashMap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}