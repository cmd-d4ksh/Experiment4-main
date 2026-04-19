package com.dhruvijain.deliveryplanner.bll;

import com.dhruvijain.deliveryplanner.model.Driver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeliveryManagerTest {
    
    // We instantiate a manager. (Note: DAO tests would ideally use mocks, but demonstrating logical Unit Testing based on rules).
    private final DeliveryManager manager = new DeliveryManager();

    @Test
    public void testValidationExceptionOnEmptyDriverName() {
        Driver driver = new Driver("", "+919876543210", "DL123456");
        assertThrows(ValidationException.class, () -> {
            manager.addDriver(driver);
        });
    }

    @Test
    public void testValidationExceptionOnInvalidPhone() {
        // Invalid phone strings
        Driver driver = new Driver("Jane Doe", "invalid_phone", "DL123456");
        assertThrows(ValidationException.class, () -> {
            manager.addDriver(driver);
        });
    }
}
