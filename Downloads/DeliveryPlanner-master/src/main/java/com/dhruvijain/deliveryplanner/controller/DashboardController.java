package com.dhruvijain.deliveryplanner.controller;

import com.dhruvijain.deliveryplanner.bll.DeliveryManager;
import com.dhruvijain.deliveryplanner.bll.ReportService;
import com.dhruvijain.deliveryplanner.bll.ValidationException;
import com.dhruvijain.deliveryplanner.model.Delivery;
import com.dhruvijain.deliveryplanner.model.Driver;
import com.dhruvijain.deliveryplanner.model.UserSession;
import com.dhruvijain.deliveryplanner.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DashboardController {

    private final DeliveryManager manager = new DeliveryManager();

    @FXML private Label roleLabel;

    // Filters
    @FXML private TextField searchAddressField;
    @FXML private ComboBox<String> filterStatusCombo;

    // WebView
    @FXML private WebView mapWebView;

    // Deliveries controls
    @FXML private TableView<Delivery> deliveriesTable;
    @FXML private TableColumn<Delivery, Integer> idCol;
    @FXML private TableColumn<Delivery, String> addressCol;
    @FXML private TableColumn<Delivery, String> statusCol;
    @FXML private TableColumn<Delivery, Integer> driverIdCol;
    @FXML private TextField addressField;
    @FXML private TextField driverIdField;

    // Drivers controls
    @FXML private VBox driverAdminPanel;
    @FXML private TableView<Driver> driversTable;
    @FXML private TableColumn<Driver, Integer> driverIdColTb;
    @FXML private TableColumn<Driver, String> driverNameCol;
    @FXML private TableColumn<Driver, String> driverContactCol;
    @FXML private TableColumn<Driver, String> driverLicenseCol;

    @FXML private TextField driverNameField;
    @FXML private TextField driverContactField;
    @FXML private TextField driverLicenseField;
    @FXML private Button addDriverButton;

    private ObservableList<Delivery> deliveriesObservableList;
    private FilteredList<Delivery> filteredDeliveries;
    private ObservableList<Driver> driversObservableList;

    // Tracks whether map.html has finished loading in the WebView.
    // executeScript() is a no-op (or throws) before the page is ready.
    private boolean mapReady = false;

    @FXML
    public void initialize() {
        // RBAC logic
        User currentUser = UserSession.getInstance().getLoggedInUser();
        roleLabel.setText("Role: " + currentUser.getRole().toString());
        if (currentUser.getRole() != User.Role.ADMIN) {
            driverAdminPanel.setDisable(true); // Example of RBAC restricting UI
        }

        // Setup Delivery Table
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        driverIdCol.setCellValueFactory(new PropertyValueFactory<>("driverId"));

        // Setup Driver Table
        driverIdColTb.setCellValueFactory(new PropertyValueFactory<>("id"));
        driverNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        driverContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        driverLicenseCol.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));

        // Status Combo
        filterStatusCombo.setItems(FXCollections.observableArrayList("ALL", "PENDING", "IN-TRANSIT", "DELIVERED"));
        filterStatusCombo.setValue("ALL");

        refreshTables();
        setupFilters();
        setupMap();
        setupTableSelection();
    }

    private void setupFilters() {
        searchAddressField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        filterStatusCombo.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void applyFilters() {
        String search = searchAddressField.getText().toLowerCase();
        String status = filterStatusCombo.getValue();

        filteredDeliveries.setPredicate(delivery -> {
            boolean matchesSearch = delivery.getAddress().toLowerCase().contains(search);
            boolean matchesStatus = "ALL".equals(status) || delivery.getStatus().equalsIgnoreCase(status);
            return matchesSearch && matchesStatus;
        });
    }

    private void setupMap() {
        java.net.URL mapUrl = getClass().getResource("/com/dhruvijain/deliveryplanner/map.html");
        if (mapUrl == null) return;

        // On page load: mark ready and push delivery data.
        // map.html handles all its own sizing (window.load + staggered invalidateSize calls).
        // If showAllDeliveries() is called before the map is initialized in JS,
        // the data is queued inside pendingData and rendered after init.
        mapWebView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                mapReady = true;
                refreshMapMarkers();
            }
        });

        mapWebView.getEngine().load(mapUrl.toExternalForm());
    }

    private void setupTableSelection() {
        deliveriesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Use JSON-safe escaping instead of naive quote replacement
                String js = "routeToAddress(" + toJsonString(newSelection.getAddress()) + ");";
                safeExecuteScript(js);
            }
        });
    }

    /** Passes all current deliveries to the map as colored markers with per-driver route lines. */
    private void refreshMapMarkers() {
        safeExecuteScript("showAllDeliveries(" + toJsonString(buildDeliveriesJson()) + ");");
    }

    /** Only calls executeScript() once the page is fully loaded. */
    private void safeExecuteScript(String js) {
        if (mapReady) {
            mapWebView.getEngine().executeScript(js);
        }
    }

    /** Serialises the current delivery list to a JSON array string. */
    private String buildDeliveriesJson() {
        List<Delivery> deliveries = manager.getAllDeliveries();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < deliveries.size(); i++) {
            Delivery d = deliveries.get(i);
            sb.append("{")
              .append("\"id\":").append(d.getId()).append(",")
              .append("\"address\":").append(toJsonString(d.getAddress())).append(",")
              .append("\"status\":").append(toJsonString(d.getStatus())).append(",")
              .append("\"driverId\":").append(d.getDriverId())
              .append("}");
            if (i < deliveries.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /** Wraps a Java string as a properly escaped JSON string literal (including the quotes). */
    private String toJsonString(String s) {
        if (s == null) return "\"\"";
        return "\"" + s.replace("\\", "\\\\")
                       .replace("\"", "\\\"")
                       .replace("\n", "\\n")
                       .replace("\r", "\\r")
                       .replace("\t", "\\t") + "\"";
    }

    private void refreshTables() {
        deliveriesObservableList = FXCollections.observableArrayList(manager.getAllDeliveries());
        filteredDeliveries = new FilteredList<>(deliveriesObservableList, p -> true);
        deliveriesTable.setItems(filteredDeliveries);
        applyFilters();

        driversObservableList = FXCollections.observableArrayList(manager.getAllDrivers());
        driversTable.setItems(driversObservableList);
    }

    @FXML
    public void handleAddDelivery() {
        try {
            String address = addressField.getText();
            int driverId = Integer.parseInt(driverIdField.getText());
            Delivery newDelivery = new Delivery(address, "PENDING", driverId);
            manager.addDelivery(newDelivery);
            refreshTables();
            addressField.clear();
            driverIdField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Invalid input! Please ensure Driver ID is a number.");
        } catch (ValidationException ex) {
            showAlert("Validation Error: " + ex.getMessage());
        }
    }

    @FXML
    public void handleAddDriver() {
        String name = driverNameField.getText();
        String contact = driverContactField.getText();
        String license = driverLicenseField.getText();

        try {
            Driver newDriver = new Driver(name, contact, license);
            manager.addDriver(newDriver);
            refreshTables();

            driverNameField.clear();
            driverContactField.clear();
            driverLicenseField.clear();
        } catch (ValidationException ex) {
            showAlert("Validation Error: " + ex.getMessage());
        }
    }

    @FXML
    public void handleSimulateTransit() {
        Delivery selected = deliveriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a delivery from the table first.");
            return;
        }

        if (!"PENDING".equals(selected.getStatus())) {
            showAlert("Can only simulate transit for PENDING deliveries.");
            return;
        }

        manager.simulateTransit(selected, () -> {
            Platform.runLater(() -> {
                refreshTables();
                refreshMapMarkers();  // update marker colors after status change
            });
        });
        
        showAlert("Transit Simulation Started for Delivery ID: " + selected.getId() + ". UI will update dynamically via threads.");
    }

    @FXML
    public void handleDownloadReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Deliveries Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        Stage stage = (Stage) addressField.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                ReportService.generateDeliveriesCSV(manager.getAllDeliveries(), file.getAbsolutePath());
                showAlert("Report Downloaded Successfully to: " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error generating report.");
            }
        }
    }

    @FXML
    public void handleLogout() throws IOException {
        UserSession.clearSession();
        // Load Login
        java.net.URL fxmlUrl = getClass().getResource("/com/dhruvijain/deliveryplanner/views/login-view.fxml");
        if(fxmlUrl == null)
            fxmlUrl = getClass().getResource("/com/dhruvijain/deliveryplanner/login-view.fxml");
        
        if (fxmlUrl != null) {
             javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(fxmlUrl);
             javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 400, 300);
             Stage stage = (Stage) addressField.getScene().getWindow();
             stage.setScene(scene);
             stage.centerOnScreen();
        } else {
            Stage stage = (Stage) addressField.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
