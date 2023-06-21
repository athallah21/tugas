import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

class User {
  String name;
  String address;
  String email;

  User(String name, String address, String email) {
    this.name = name;
    this.address = address;
    this.email = email;
  }
}

abstract class Product {
  String name;
  String description;
  double price;
  int stock;
  Category category;

  Product(String name, String description, double price, int stock, Category category) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.category = category;
  }

  void displayInfo() {
    System.out.println(this.name);
    System.out.println(this.description);
    System.out.println(this.price);
    System.out.println(this.stock);
    System.out.println(this.category);
  }
}

class Clothing extends Product {
  String size;

  Clothing(String name, String description, double price, int stock, String size, Category category) {
    super(name, description, price, stock, category);
    this.size = size;
  }

  @Override
  void displayInfo() {
    System.out.println("Clothing - " + name + " - " + price + " - " + size + " - Stock: " + stock);
  }
}

class Electronics extends Product {
  String brand;

  Electronics(String name, String description, double price, int stock, String brand, Category category) {
    super(name, description, price, stock, category);
    this.brand = brand;
  }

  @Override
  void displayInfo() {
    System.out.println("Electronics - " + name + " - " + price + " - " + brand + " - Stock: " + stock);
  }
}

class Cart {
  Queue<Product> products = new LinkedList<>();

  void addProduct(Product product) {
    products.add(product);
  }

  void removeProduct(Product product) {
    products.remove(product);
  }

  double calculateTotalPrice() {
    double total = 0;
    for (var product : products) {
      total += product.price;
    }
    return total;
  }
}

class Order {
  User user;
  Queue<Product> products;

  Order(User user, Queue<Product> products) {
    this.user = user;
    this.products = products;
  }
}

class Payment {
  boolean processPayment(Order order) {
    // Proses pembayaran
    return true; // Jika pembayaran berhasil
  }
}

interface PaymentGateway {
  boolean processPayment(Order order);
}

class TransactionHandler {
  private Database database;
  private PaymentGateway paymentGateway;

  public TransactionHandler(Database database, PaymentGateway paymentGateway) {
    this.database = database;
    this.paymentGateway = paymentGateway;
  }

  public boolean processTransaction(User user, Cart cart) {
    Order order = new Order(user, cart.products);

    boolean paymentStatus = paymentGateway.processPayment(order);
    if (paymentStatus) {
      database.saveOrder(order);
      Notification.sendNotification(user, "Order has been placed and payment is successful.");
      updateStock(cart);
      return true;
    } else {
      Notification.sendNotification(user, "Payment failed for the order.");
      return false;
    }
  }

  private void updateStock(Cart cart) {
    for (Product product : cart.products) {
      product.stock--;
    }
  }
}

class Category {
  String name;

  Category(String name) {
    this.name = name;
  }
}

class Database {
  void saveOrder(Order order) {
    // Simpan pesanan ke basis data
  }
}

class Authentication {
  static User login() {
    // Proses login
    return new User("John Doe", "123 Main Street", "johndoe@example.com");
  }
}

class Notification {
  static void sendNotification(User currentUser, String message) {
    // Kirim notifikasi ke pengguna
    System.out.println("Notification sent to " + currentUser.name + ": " + message);
  }
}

class ShoppingApp {
  User currentUser;
  Cart cart;
  Database database;
  Queue<Product> availableProducts;

  ShoppingApp() {
    currentUser = Authentication.login();
    cart = new Cart();
    database = new Database();
    availableProducts = new LinkedList<>();
    listProducts();
  }

  void listProducts() {
    Category clothingCategory = new Category("Clothing");
    Category electronicsCategory = new Category("Electronics");

    Product product1 = new Clothing("Shirt", "Comfortable shirt", 20.0, 10, "XL", clothingCategory);
    Product product2 = new Clothing("Pants", "Stylish pants", 30.0, 5, "L", clothingCategory);
    Product product3 = new Electronics("Shoes", "Sporty shoes", 50.0, 8, "Nike", electronicsCategory);

    availableProducts.add(product1);
    availableProducts.add(product2);
    availableProducts.add(product3);
  }

  void addToCart(Product product) {
    if (product.stock > 0) {
      cart.addProduct(product);
      product.stock--;
      System.out.println(product.name + " added to cart.");
    } else {
      System.out.println(product.name + " is out of stock.");
    }
  }

  void removeFromCart(Product product) {
    cart.removeProduct(product);
    product.stock++;
    System.out.println(product.name + " removed from cart.");
  }

  void placeOrder() {
    double totalPrice = cart.calculateTotalPrice();
    Order order = new Order(currentUser, cart.products);

    boolean paymentStatus = new Payment().processPayment(order);
    if (paymentStatus) {
      database.saveOrder(order);
      Notification.sendNotification(currentUser, "Order has been placed.");
      for (Product product : cart.products) {
        if (product.stock <= 0) {
          cart.removeProduct(product);
        }
      }
    }
  }

  void displayCart() {
    if (cart.products.isEmpty()) {
      System.out.println("\nYour cart is empty!");
    } else {
      System.out.println("\nProducts in your cart:");
      int i = 1;
      for (var product : cart.products) {
        System.out.println(i + ". " + product.name + " - " + product.price);
        i++;
      }
      System.out.println("Total Price: " + cart.calculateTotalPrice());
    }
  }

  void displayAvailableProducts() {
    System.out.println("\nAvailable products:");
    int i = 1;
    for (var product : availableProducts) {
      System.out.println(i + ". " + product.name + " - " + product.price + " - Stock: " + product.stock + " - Category: " + product.category.name);
      i++;
    }
  }

  void searchProductByName(String productName) {
    boolean found = false;
    for (var product : availableProducts) {
      if (product.name.equalsIgnoreCase(productName)) {
        System.out.println("Product found:");
        System.out.println(product.name + " - " + product.price + " - Stock: " + product.stock + " - Category: " + product.category.name);
        found = true;
        break;
      }
    }
    if (!found) {
      System.out.println("Product not found!");
    }
  }
}

public class projek1 {
  public static void main(String[] args) {
    ShoppingApp app = new ShoppingApp();
    Scanner scanner = new Scanner(System.in);
    int choice;

    do {
      System.out.println("\n==== Shopping App ====");
      System.out.println("1. View available products");
      System.out.println("2. Add product to cart");
      System.out.println("3. Remove product from cart");
      System.out.println("4. View cart");
      System.out.println("5. Search product by name");
      System.out.println("6. Place order");
      System.out.println("0. Exit");

      System.out.print("Enter your choice: ");
      choice = scanner.nextInt();

      switch (choice) {
        case 1:
          app.displayAvailableProducts();
          break;
        case 2:
          System.out.print("Enter the product number to add to cart: ");
          int productNumber = scanner.nextInt();
          if (productNumber > 0 && productNumber <= app.availableProducts.size()) {
            Product product = (Product) app.availableProducts.toArray()[productNumber - 1];
            app.addToCart(product);
          } else {
            System.out.println("Invalid product number!");
          }
          break;
        case 3:
          System.out.print("Enter the product number to remove from cart: ");
          int productNumberToRemove = scanner.nextInt();
          if (productNumberToRemove > 0 && productNumberToRemove <= app.cart.products.size()) {
            Product productToRemove = (Product) app.cart.products.toArray()[productNumberToRemove - 1];
            app.removeFromCart(productToRemove);
          } else {
            System.out.println("Invalid product number!");
          }
          break;
        case 4:
          app.displayCart();
          break;
        case 5:
          System.out.print("Enter the product name to search: ");
          scanner.nextLine(); // Consume newline character
          String productName = scanner.nextLine();
          app.searchProductByName(productName);
          break;
        case 6:
          app.placeOrder();
          break;
        case 0:
          System.out.println("Exiting the application. Goodbye!");
          break;
        default:
          System.out.println("Invalid choice. Please try again.");
          break;
      }
    } while (choice != 0);
  }
}
