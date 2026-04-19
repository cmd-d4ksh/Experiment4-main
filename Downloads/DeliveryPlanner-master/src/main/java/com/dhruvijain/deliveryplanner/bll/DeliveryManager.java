package com.dhruvijain.deliveryplanner.bll;

import com.dhruvijain.deliveryplanner.dao.DeliveryDAO;
import com.dhruvijain.deliveryplanner.dao.DriverDAO;
import com.dhruvijain.deliveryplanner.model.Delivery;
import com.dhruvijain.deliveryplanner.model.Driver;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryManager {
    private final DeliveryDAO deliveryDAO;
    private final DriverDAO driverDAO;

    public DeliveryManager() {
        this.deliveryDAO = new DeliveryDAO();
        this.driverDAO = new DriverDAO();
    }

    public void addDelivery(Delivery delivery) throws ValidationException {
        if (delivery.getAddress() == null || delivery.getAddress().trim().isEmpty()) {
            throw new ValidationException("Address cannot be empty.");
        }
        if (delivery.getDriverId() <= 0) {
            throw new ValidationException("Invalid Driver ID.");
        }
        deliveryDAO.create(delivery);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryDAO.readAll();
    }

    public List<Delivery> searchDeliveriesByStatus(String status) {
        return getAllDeliveries().stream()
                .filter(d -> d.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public void addDriver(Driver driver) throws ValidationException {
        if (driver.getName() == null || driver.getName().trim().isEmpty()) {
            throw new ValidationException("Driver name cannot be empty.");
        }
        if (driver.getContact() == null || !driver.getContact().matches("^[+]?[0-9\\s\\-]{10,15}$")) {
            throw new ValidationException("Invalid contact number format.");
        }
        if (driver.getLicenseNumber() == null || driver.getLicenseNumber().trim().isEmpty()) {
            throw new ValidationException("Invalid license number.");
        }
        driverDAO.create(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverDAO.readAll();
    }

    // Example of Threading: Simulating delivery transit update
    public void simulateTransit(Delivery delivery, Runnable onComplete) {
        Thread transitThread = new Thread(() -> {
            try {
                System.out.println("Delivery " + delivery.getId() + " is now IN-TRANSIT.");
                delivery.setStatus("IN-TRANSIT");
                deliveryDAO.update(delivery);
                
                // Simulate time taken
                Thread.sleep(3000); 

                System.out.println("Delivery " + delivery.getId() + " is now DELIVERED.");
                delivery.setStatus("DELIVERED");
                deliveryDAO.update(delivery);
                
                if (onComplete != null) {
                    onComplete.run(); // Callback to update UI
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        transitThread.start();
    }
}
