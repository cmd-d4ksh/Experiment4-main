package com.dhruvijain.deliveryplanner.bll;

import com.dhruvijain.deliveryplanner.model.Delivery;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class ReportService {

    public static void generateDeliveriesCSV(List<Delivery> deliveries, String filePath) throws Exception {
        File file = new File(filePath);
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("ID,Address,Status,Driver_ID");
            for (Delivery d : deliveries) {
                pw.printf("%d,\"%s\",%s,%d%n", d.getId(), d.getAddress().replace("\"", "\"\""), d.getStatus(), d.getDriverId());
            }
        }
    }
}
