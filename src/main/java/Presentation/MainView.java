package Presentation;

import Business.ClientBLL;
import Business.ProductBLL;
import Business.OrderBLL;
import Model.Client;
import Model.Product;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Clasa principala a interfetei grafice (UI) dezvoltata in JavaFX.
 * Gestioneaza panourile pentru Clienti, Produse si Comenzi si controleaza evenimentele butoanelor.
 * Toate iterațiile peste colecții utilizează paradigma funcțională (Streams și Lambda).
 * @author Poliec Elena
 * @version 1.2
 */
public class MainView extends BorderPane {

    private final String currentTabType; // "Client", "Product" sau "Order"
    private TableView<Object> tableView; // Tabelul principal pentru Client/Product

    // Tabele suplimentare folosite doar în panoul de Comenzi
    private TableView<Object> clientOrderTable;
    private TableView<Object> productOrderTable;

    private final ClientBLL clientBLL = new ClientBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final OrderBLL orderBLL = new OrderBLL();

    public MainView(String currentTabType) {
        this.currentTabType = currentTabType;
        this.setPadding(new Insets(15));
        initializeUI();
    }

    /**
     * Initializeaza si aranjeaza componentele grafice (campuri text, butoane, tabele)
     * in functie de secțiunea selectata (Client, Product, Order).
     */
    private void initializeUI() {
        VBox controlsLayout = new VBox(10);
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        HBox buttonsLayout = new HBox(10);
        tableView = new TableView<>();

        // ---------------- PANOU CLIENȚI ----------------
        if (currentTabType.equals("Client")) {
            TextField txtId = new TextField(); txtId.setPromptText("Only for Edit/Delete");
            TextField txtName = new TextField();
            TextField txtAddress = new TextField();
            TextField txtEmail = new TextField();

            inputGrid.add(new Label("ID:"), 0, 0); inputGrid.add(txtId, 1, 0);
            inputGrid.add(new Label("Name:"), 0, 1); inputGrid.add(txtName, 1, 1);
            inputGrid.add(new Label("Address:"), 0, 2); inputGrid.add(txtAddress, 1, 2);
            inputGrid.add(new Label("Email:"), 0, 3); inputGrid.add(txtEmail, 1, 3);

            Button btnAdd = new Button("Add");
            Button btnEdit = new Button("Edit");
            Button btnDelete = new Button("Delete");
            Button btnRefresh = new Button("View All");

            buttonsLayout.getChildren().addAll(btnAdd, btnEdit, btnDelete, btnRefresh);

            btnRefresh.setOnAction(e -> populateTableWithReflection(tableView, clientBLL.findAllClients()));

            btnAdd.setOnAction(e -> {
                clientBLL.insertClient(new Client(txtName.getText(), txtAddress.getText(), txtEmail.getText()));
                populateTableWithReflection(tableView, clientBLL.findAllClients());
            });

            btnEdit.setOnAction(e -> {
                clientBLL.updateClient(new Client(Integer.parseInt(txtId.getText()), txtName.getText(), txtAddress.getText(), txtEmail.getText()));
                populateTableWithReflection(tableView, clientBLL.findAllClients());
            });

            btnDelete.setOnAction(e -> {
                try {
                    int id = Integer.parseInt(txtId.getText());
                    clientBLL.deleteClient(id);
                    populateTableWithReflection(tableView, clientBLL.findAllClients());
                    txtId.clear();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid ID to delete.");
                }
            });

            // ---------------- PANOU PRODUSE ----------------
        } else if (currentTabType.equals("Product")) {
            TextField txtId = new TextField(); txtId.setPromptText("Only for Edit/Delete");
            TextField txtName = new TextField();
            TextField txtStock = new TextField();
            TextField txtPrice = new TextField();

            inputGrid.add(new Label("ID:"), 0, 0); inputGrid.add(txtId, 1, 0);
            inputGrid.add(new Label("Product Name:"), 0, 1); inputGrid.add(txtName, 1, 1);
            inputGrid.add(new Label("Stock:"), 0, 2); inputGrid.add(txtStock, 1, 2);
            inputGrid.add(new Label("Price:"), 0, 3); inputGrid.add(txtPrice, 1, 3);

            Button btnAdd = new Button("Add");
            Button btnEdit = new Button("Edit");
            Button btnDelete = new Button("Delete");
            Button btnRefresh = new Button("View All");

            buttonsLayout.getChildren().addAll(btnAdd, btnEdit, btnDelete, btnRefresh);

            btnRefresh.setOnAction(e -> populateTableWithReflection(tableView, productBLL.findAllProducts()));

            btnAdd.setOnAction(e -> {
                productBLL.insertProduct(new Product(txtName.getText(), Integer.parseInt(txtStock.getText()), Double.parseDouble(txtPrice.getText())));
                populateTableWithReflection(tableView, productBLL.findAllProducts());
            });

            btnEdit.setOnAction(e -> {
                productBLL.updateProduct(new Product(Integer.parseInt(txtId.getText()), txtName.getText(), Integer.parseInt(txtStock.getText()), Double.parseDouble(txtPrice.getText())));
                populateTableWithReflection(tableView, productBLL.findAllProducts());
            });

            btnDelete.setOnAction(e -> {
                try {
                    int id = Integer.parseInt(txtId.getText());
                    productBLL.deleteProduct(id);
                    populateTableWithReflection(tableView, productBLL.findAllProducts());
                    txtId.clear();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid ID to delete.");
                }
            });

            // ---------------- PANOU COMENZI ----------------
        } else if (currentTabType.equals("Order")) {
            TextField txtClientId = new TextField();
            TextField txtProductId = new TextField();
            TextField txtQuantity = new TextField();

            inputGrid.add(new Label("Client ID:"), 0, 0); inputGrid.add(txtClientId, 1, 0);
            inputGrid.add(new Label("Product ID:"), 0, 1); inputGrid.add(txtProductId, 1, 1);
            inputGrid.add(new Label("Quantity:"), 0, 2); inputGrid.add(txtQuantity, 1, 2);

            Button btnCreateOrder = new Button("Place Order");
            Button btnRefreshLists = new Button("Load/Refresh Lists");
            buttonsLayout.getChildren().addAll(btnCreateOrder, btnRefreshLists);

            clientOrderTable = new TableView<>();
            productOrderTable = new TableView<>();

            btnRefreshLists.setOnAction(e -> {
                populateTableWithReflection(clientOrderTable, clientBLL.findAllClients());
                populateTableWithReflection(productOrderTable, productBLL.findAllProducts());
            });

            btnCreateOrder.setOnAction(e -> {
                try {
                    orderBLL.createOrder(
                            Integer.parseInt(txtClientId.getText()),
                            Integer.parseInt(txtProductId.getText()),
                            Integer.parseInt(txtQuantity.getText())
                    );
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order created! Check the database log.");

                    populateTableWithReflection(clientOrderTable, clientBLL.findAllClients());
                    populateTableWithReflection(productOrderTable, productBLL.findAllProducts());
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Execution Error", ex.getMessage());
                }
            });

            HBox tablesContainer = new HBox(15);
            VBox clientBox = new VBox(5, new Label("Available Clients:"), clientOrderTable);
            VBox productBox = new VBox(5, new Label("Available Products:"), productOrderTable);

            HBox.setHgrow(clientBox, Priority.ALWAYS);
            HBox.setHgrow(productBox, Priority.ALWAYS);
            tablesContainer.getChildren().addAll(clientBox, productBox);

            this.setCenter(tablesContainer);
        }

        controlsLayout.getChildren().addAll(inputGrid, buttonsLayout);
        this.setTop(controlsLayout);

        if (!currentTabType.equals("Order")) {
            this.setCenter(tableView);
        }
    }

    /**
     * Genereaza dinamic antetul si populeaza randurile unui TableView specific folosind Reflection si Java Streams.
     * Extrage automat proprietatile declarate ale obiectelor din lista fara bucle iterative clasice
     * @param targetTable Tabelul în care urmează să fie introduse datele.
     * @param objects  Lista de obiecte generice (clienti sau produse).
     */
    private void populateTableWithReflection(TableView<Object> targetTable, List<?> objects) {
        targetTable.getColumns().clear();
        if (objects == null || objects.isEmpty()) {
            return;
        }

        Class<?> objectClass = objects.get(0).getClass();

        // STREAMS & LAMBDA
        Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> !field.isSynthetic()) // filtrez campurile sintetice generate de compilator
                .forEach(field -> {
                    String columnName = field.getName();
                    TableColumn<Object, Object> column = new TableColumn<>(columnName.toUpperCase());

                    column.setCellValueFactory(cellData -> {
                        Object rowObject = cellData.getValue();
                        try {
                            Field f = rowObject.getClass().getDeclaredField(columnName);
                            f.setAccessible(true);
                            return new javafx.beans.property.SimpleObjectProperty<>(f.get(rowObject));
                        } catch (Exception e) {
                            return null;
                        }
                    });

                    targetTable.getColumns().add(column);
                });

        targetTable.setItems(FXCollections.observableArrayList(objects));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}