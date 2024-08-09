/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.supermarketbillingsystem;

/**
 *
 * @author hp
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.concurrent.atomic.AtomicInteger;

public class SupermarketBillingSystem extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SupermarketBillingSystem app = new SupermarketBillingSystem();
            app.setVisible(true);
            new LoadingWindow(app);
        });
    }

    public SupermarketBillingSystem() {
        setTitle("Supermarket Billing System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    static class LoadingWindow extends JWindow {
        public LoadingWindow(JFrame parent) {
            setSize(800, 600);
            setLocationRelativeTo(parent);
            getContentPane().setBackground(Color.WHITE);
            ImageIcon imageIcon = new ImageIcon("C:\\Users\\hp\\OneDrive\\Desktop\\Screenshot 2024-06-02 125834.png");
            Image image = imageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(image));
            JLabel nameLabel = new JLabel("Supermarket Billing System", SwingConstants.CENTER);
            JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);

            setLayout(new GridLayout(3, 1));
            add(nameLabel);
            add(logoLabel);
            add(loadingLabel);
            setVisible(true);
            Timer timer = new Timer(3000, e -> {
                dispose();
                new LoginPage(parent);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    static class LoginPage extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JComboBox<String> userCategoryBox;

        public LoginPage(JFrame parent) {
            setTitle("Login");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(parent);

            JLabel categoryLabel = new JLabel("Category:");
            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Password:");

            String[] userCategories = {"Admin", "Seller"};
            userCategoryBox = new JComboBox<>(userCategories);
            usernameField = new JTextField(15);
            passwordField = new JPasswordField(15);

            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new LoginButtonListener(parent));
            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(categoryLabel);
            panel.add(userCategoryBox);
            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);
            panel.add(new JLabel());
            panel.add(loginButton);
            add(panel);
            setVisible(true);
        }

        private class LoginButtonListener implements ActionListener {
            private JFrame parent;
            public LoginButtonListener(JFrame parent) {
                this.parent = parent;
            }
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userCategory = (String) userCategoryBox.getSelectedItem();
                if (authenticate(username, password, userCategory)) {
                    dispose();
                    if ("Admin".equals(userCategory)) {
                        new AdminPage(parent);
                    } else {
                        new BillingPage(parent);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid login credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private boolean authenticate(String username, String password, String userCategory) {
                return ("admin".equals(username) && "admin123".equals(password) && "Admin".equals(userCategory)) ||
                        ("seller".equals(username) && "seller123".equals(password) && "Seller".equals(userCategory));
            }
        }
    }

    static class AdminPage extends JFrame {
        public AdminPage(JFrame parent) {
            setTitle("Admin Dashboard");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(parent);
            JPanel panel = new JPanel(new GridLayout(4, 1));
            add(panel);

            JButton manageProductsButton = new JButton("Manage Products");
            manageProductsButton.addActionListener(e -> new ManageProductsPage(this));
            JButton manageCategoriesButton = new JButton("Manage Categories");
            manageCategoriesButton.addActionListener(e -> new ManageCategoriesPage(this));
            JButton billingButton = new JButton("Billing");
            billingButton.addActionListener(e -> new BillingPage(this));
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                dispose();
                new LoginPage(parent);
            });

            panel.add(manageProductsButton);
            panel.add(manageCategoriesButton);
            panel.add(billingButton);
            panel.add(backButton);

            setVisible(true);
        }
    }

    static class Product {
        private int id;
        private String name;
        private double price;
        private int quantity;
        private String category;
        public Product(int id, String name, double price, int quantity, String category) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.category = category;
        }

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public double getPrice() {
            return price;
        }
        public int getQuantity() {
            return quantity;
        }
        public String getCategory() {
            return category;
        }

        @Override
        public String toString() {
            return String.format("%d - %s - %.2f - %d units - %s", id, name, price, quantity, category);
        }
    }

    static class ManageProductsPage extends JFrame {
        private ArrayList<Product> products = new ArrayList<>();
        private JList<Product> productList;
        private DefaultListModel<Product> productListModel;

        ManageProductsPage(JFrame parent) {
            setTitle("Manage Products");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(parent);

            productListModel = new DefaultListModel<>();
            productList = new JList<>(productListModel);
            JScrollPane productScrollPane = new JScrollPane(productList);

            // Prepopulate with sample products
            addProduct(new Product(1, "Apple", 50.00, 50, "Fruit"));
            addProduct(new Product(2, "Banana", 40.50, 40, "Fruit"));
            addProduct(new Product(3, "Bread", 200.50, 80, "Bakery"));
            addProduct(new Product(4, "Milk", 250.50, 30, "Dairy"));
            addProduct(new Product(5, "Chicken", 350.00, 20, "Meat"));
            addProduct(new Product(6, "Orange", 45.98, 60, "Fruit"));
            addProduct(new Product(7, "Rice", 650.28, 80, "Grains"));
            addProduct(new Product(8, "Sugar", 367.80, 70, "Pantry"));
            addProduct(new Product(9, "Salt", 350.60, 90, "Pantry"));
            addProduct(new Product(10, "Butter", 56.50, 25, "Dairy"));
            addProduct(new Product(11, "Cheese", 500.00, 50, "Dairy"));
            addProduct(new Product(12, "Eggs", 70.05, 100, "Dairy"));
            addProduct(new Product(13, "Coffee Beans", 500.05, 40, "Pantry"));
            addProduct(new Product(14, "Tea", 480.50, 30, "Pantry"));
            addProduct(new Product(15, "Flour", 1000.08, 20, "Pantry"));
            addProduct(new Product(16, "Tomato", 70.62, 60, "Fruit"));
            addProduct(new Product(17, "Potato", 90.78, 80, "Vegetable"));
            addProduct(new Product(18, "Onion", 65.58, 70, "Vegetable"));
            addProduct(new Product(19, "Pasta", 500.96, 90, "Pantry"));
            addProduct(new Product(20, "Carrot", 65.90, 25, "Vegetable"));

            JButton addButton = new JButton("Add Product");
            JButton editButton = new JButton("Edit Product");
            JButton removeButton = new JButton("Remove Product");
            JButton backButton = new JButton("Back");

            addButton.addActionListener(e -> new ProductDialog(this, "Add Product", null));
            editButton.addActionListener(e -> {
                Product selectedProduct = productList.getSelectedValue();
                if (selectedProduct != null) {
                    new ProductDialog(this, "Edit Product", selectedProduct);
                } else {
                    JOptionPane.showMessageDialog(ManageProductsPage.this, "Select a product to edit", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            removeButton.addActionListener(e -> {
                Product selectedProduct = productList.getSelectedValue();
                if (selectedProduct != null) {
                    productListModel.removeElement(selectedProduct);
                    products.remove(selectedProduct);
                } else {
                    JOptionPane.showMessageDialog(ManageProductsPage.this, "Select a product to remove", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            backButton.addActionListener(e -> {
                dispose();
                new AdminPage(parent);
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            buttonPanel.add(removeButton);
            buttonPanel.add(backButton);
            add(productScrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
            setVisible(true);
        }

        void addProduct(Product product) {
            products.add(product);
            productListModel.addElement(product);
        }

        class ProductDialog extends JDialog {
            private JTextField idField;
            private JTextField nameField;
            private JTextField priceField;
            private JTextField quantityField;
            private JTextField categoryField;
            private JButton saveButton;
            private Product existingProduct;

            public ProductDialog(JFrame parent, String title, Product existingProduct) {
                super(parent, title, true);
                setSize(300, 300);
                setLocationRelativeTo(parent);
                this.existingProduct = existingProduct;
                JLabel idLabel = new JLabel("ID:");
                JLabel nameLabel = new JLabel("Name:");
                JLabel priceLabel = new JLabel("Price:");
                JLabel quantityLabel = new JLabel("Quantity:");
                JLabel categoryLabel = new JLabel("Category:");
                idField = new JTextField(10);
                nameField = new JTextField(10);
                priceField = new JTextField(10);
                quantityField = new JTextField(10);
                categoryField = new JTextField(10);
                saveButton = new JButton("Save");
                saveButton.addActionListener(e -> {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    String category = categoryField.getText();

                    if (existingProduct != null) {
                        existingProduct.id = id;
                        existingProduct.name = name;
                        existingProduct.price = price;
                        existingProduct.quantity = quantity;
                        existingProduct.category = category;
                        productList.repaint();
                    } else {
                        addProduct(new Product(id, name, price, quantity, category));
                    }
                    dispose();
                });

                if (existingProduct != null) {
                    idField.setText(String.valueOf(existingProduct.getId()));
                    nameField.setText(existingProduct.getName());
                    priceField.setText(String.valueOf(existingProduct.getPrice()));
                    quantityField.setText(String.valueOf(existingProduct.getQuantity()));
                    categoryField.setText(existingProduct.getCategory());
                }
                setLayout(new GridLayout(6, 2));
                add(idLabel);
                add(idField);
                add(nameLabel);
                add(nameField);
                add(priceLabel);
                add(priceField);
                add(quantityLabel);
                add(quantityField);
                add(categoryLabel);
                add(categoryField);
                add(new JLabel());
                add(saveButton);
                setVisible(true);
            }
        }
    }

     static class Category {
        private int id;
        private String name;
        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        @Override
        public String toString() {
            return String.format("%d - %s", id, name);
        }
    }
     
    static class ManageCategoriesPage extends JFrame {
        private ArrayList<Category> categories = new ArrayList<>();
        private JList<Category> categoryList;
        private DefaultListModel<Category> categoryListModel;

        ManageCategoriesPage(JFrame parent) {
            setTitle("Manage Categories");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(parent);
            categoryListModel = new DefaultListModel<>();
            categoryList = new JList<>(categoryListModel);
            JScrollPane categoryScrollPane = new JScrollPane(categoryList);
            // Prepopulate with sample categories
            addCategory(new Category(1, "Fruit"));
            addCategory(new Category(2, "Bakery"));
            addCategory(new Category(3, "Dairy"));
            addCategory(new Category(4, "Meat"));
            addCategory(new Category(5, "Grains"));
            addCategory(new Category(6, "Pantry"));
            addCategory(new Category(6, "Vegetable"));
            JButton addButton = new JButton("Add Category");
            JButton editButton = new JButton("Edit Category");
            JButton removeButton = new JButton("Remove Category");
            JButton backButton = new JButton("Back");
            addButton.addActionListener(e -> new CategoryDialog(this, "Add Category", null));
            editButton.addActionListener(e -> {
                Category selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    new CategoryDialog(this, "Edit Category", selectedCategory);
                } else {
                    JOptionPane.showMessageDialog(ManageCategoriesPage.this, "Select a category to edit", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            removeButton.addActionListener(e -> {
                Category selectedCategory = categoryList.getSelectedValue();
                if (selectedCategory != null) {
                    categoryListModel.removeElement(selectedCategory);
                    categories.remove(selectedCategory);
                } else {
                    JOptionPane.showMessageDialog(ManageCategoriesPage.this, "Select a category to remove", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            backButton.addActionListener(e -> {
                dispose();
                new AdminPage(parent);
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            buttonPanel.add(removeButton);
            buttonPanel.add(backButton);
            setLayout(new BorderLayout());
            add(categoryScrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
            setVisible(true);
        }

        public void addCategory(Category category) {
            categories.add(category);
            categoryListModel.addElement(category);
        }

        public void updateCategory(Category oldCategory, Category newCategory) {
            int index = categories.indexOf(oldCategory);
            if (index >= 0) {
                categories.set(index, newCategory);
                categoryListModel.setElementAt(newCategory, index);
            }
        }

        private class CategoryDialog extends JDialog {
            private JTextField idField;
            private JTextField nameField;
            private JButton saveButton;
            private Category originalCategory;
            public CategoryDialog(JFrame parent, String title, Category category) {
                super(parent, title, true);
                setSize(300, 200);
                setLocationRelativeTo(parent);
                originalCategory = category;
                idField = new JTextField();
                nameField = new JTextField();
                saveButton = new JButton("Save");
                if (category != null) {
                    idField.setText(String.valueOf(category.getId()));
                    idField.setEnabled(false); // ID should not be editable
                    nameField.setText(category.getName());
                }
                saveButton.addActionListener(e -> {
                    try {
                        int id = Integer.parseInt(idField.getText());
                        String name = nameField.getText();

                        Category newCategory = new Category(id, name);

                        if (originalCategory != null) {
                            updateCategory(originalCategory, newCategory);
                        } else {
                            addCategory(newCategory);
                        }

                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(CategoryDialog.this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                setLayout(new GridLayout(3, 2));
                add(new JLabel("ID:"));
                add(idField);
                add(new JLabel("Name:"));
                add(nameField);
                add(new JLabel());
                add(saveButton);
                setVisible(true);
            }
        }
    }

    static class BillingPage extends JFrame {
        private JTextField itemIDField;
        private JTextField itemNameField;
        private JTextField priceField;
        private JTextField quantityField;
        private JTable billTable;
        private DefaultTableModel tableModel;
        private JLabel totalAmountLabel;
        private AtomicInteger saleIDGenerator = new AtomicInteger(1);

        private static final ArrayList<Product> productCatalog = new ArrayList<>();
        static {
            // Prepopulate with sample products for quick testing
            productCatalog.add(new Product(1, "Apple", 50.00, 50, "Fruit"));
            productCatalog.add(new Product(2, "Banana", 40.50, 40, "Fruit"));
            productCatalog.add(new Product(3, "Bread", 200.50, 80, "Bakery"));
            productCatalog.add(new Product(4, "Milk", 250.50, 30, "Dairy"));
            productCatalog.add(new Product(5, "Chicken", 350.00, 20, "Meat"));
            productCatalog.add(new Product(6, "Orange", 45.98, 60, "Fruit"));
            productCatalog.add(new Product(7, "Rice", 650.28, 80, "Grains"));
            productCatalog.add(new Product(8, "Sugar", 367.80, 70, "Pantry"));
            productCatalog.add(new Product(9, "Salt", 350.60, 90, "Pantry"));
            productCatalog.add(new Product(10, "Butter", 56.50, 25, "Dairy"));
            productCatalog.add(new Product(11, "Cheese", 500.00, 50, "Dairy"));
            productCatalog.add(new Product(12, "Eggs", 70.05, 100, "Dairy"));
            productCatalog.add(new Product(13, "Coffee Beans", 500.05, 40, "Pantry"));
            productCatalog.add(new Product(14, "Tea", 480.50, 30, "Pantry"));
            productCatalog.add(new Product(15, "Flour", 1000.08, 20, "Pantry"));
            productCatalog.add(new Product(16, "Tomato", 70.62, 60, "Fruit"));
            productCatalog.add(new Product(17, "Potato", 90.78, 80, "Vegetable"));
            productCatalog.add(new Product(18, "Onion", 65.58, 70, "Vegetable"));
            productCatalog.add(new Product(19, "Pasta", 500.96, 90, "Pantry"));
            productCatalog.add(new Product(20, "Carrot", 65.90, 25, "Vegetable"));
        }

        public BillingPage(JFrame parent) {
            setTitle("Billing Page");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(parent);
            JLabel saleIDLabel = new JLabel("Sale ID:");
            JTextField saleIDField = new JTextField(10);
            saleIDField.setText(String.valueOf(saleIDGenerator.getAndIncrement()));
            saleIDField.setEditable(false);
            JLabel dateLabel = new JLabel("Date:");
            JTextField dateField = new JTextField(10);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateField.setText(sdf.format(new Date()));
            dateField.setEditable(false);
            JLabel itemIDLabel = new JLabel("Item ID:");
            JLabel itemNameLabel = new JLabel("Item Name:");
            JLabel priceLabel = new JLabel("Price:");
            JLabel quantityLabel = new JLabel("Quantity:");
            itemIDField = new JTextField(10);
            itemNameField = new JTextField(10);
            priceField = new JTextField(10);
            quantityField = new JTextField(10);
            itemNameField.setEditable(false);
            priceField.setEditable(false);
            JButton addButton = new JButton("Add to Bill");
            addButton.addActionListener(new AddButtonListener());
            JButton saveButton = new JButton("Save Sale");
            saveButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(BillingPage.this, "Sale saved successfully!");
            });

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                dispose();
                new LoginPage(parent);
            });

            itemIDField.addActionListener(e -> autoFillItemDetails());

            tableModel = new DefaultTableModel(new Object[]{"Item ID", "Item Name", "Price", "Quantity", "Total"}, 0);
            billTable = new JTable(tableModel);
            JScrollPane tableScrollPane = new JScrollPane(billTable);

            JPanel inputPanel = new JPanel(new GridLayout(6, 2));
            inputPanel.add(saleIDLabel);
            inputPanel.add(saleIDField);
            inputPanel.add(dateLabel);
            inputPanel.add(dateField);
            inputPanel.add(itemIDLabel);
            inputPanel.add(itemIDField);
            inputPanel.add(itemNameLabel);
            inputPanel.add(itemNameField);
            inputPanel.add(priceLabel);
            inputPanel.add(priceField);
            inputPanel.add(quantityLabel);
            inputPanel.add(quantityField);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(backButton);

            add(inputPanel, BorderLayout.NORTH);
            add(tableScrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);
            totalAmountLabel = new JLabel("Total Amount: Rs 0.00");
            JPanel totalPanel = new JPanel();
            totalPanel.add(totalAmountLabel);
            add(totalPanel, BorderLayout.WEST);
            }
        
         private void updateTotalAmount() {
        double totalAmount = 0;
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            totalAmount += (double) tableModel.getValueAt(row, 4); // Total is in the 5th column (index 4)
        }
        totalAmountLabel.setText(String.format("Total Amount: RS %.2f", totalAmount));
    }

        private void autoFillItemDetails() {
            String itemIDText = itemIDField.getText();
            if (!itemIDText.isEmpty()) {
                int itemID = Integer.parseInt(itemIDText);
                for (Product product : productCatalog) {
                    if (product.getId() == itemID) {
                        itemNameField.setText(product.getName());
                        priceField.setText(String.valueOf(product.getPrice()));
                        return;
                    }
                }
                JOptionPane.showMessageDialog(BillingPage.this, "Item ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                itemNameField.setText("");
                priceField.setText("");
            }
        }

        private class AddButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int itemID = Integer.parseInt(itemIDField.getText());
                    String itemName = itemNameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    double total = price * quantity;
                    tableModel.addRow(new Object[]{itemID, itemName, price, quantity, total});
                    updateTotalAmount();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BillingPage.this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
} 
