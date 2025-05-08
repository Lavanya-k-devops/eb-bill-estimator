package eb.bill.estimator;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BillEstimatorController {

    // POST request for calculating the bill
    @PostMapping("/calculateBill")
    public ResponseEntity<BillResponse> calculateBill(@RequestBody ApplianceData applianceData) {
        // Calculate total units and bill amount based on appliance data
        double totalUnits = 0;

        for (int i = 0; i < applianceData.getWattage().size(); i++) {
            double wattage = applianceData.getWattage().get(i);
            double usageHours = applianceData.getUsageHours().get(i);

            // Calculate units for each appliance (kWh = (Wattage * Hours) / 1000) and multiply by 60 for 60 days
            totalUnits += (wattage * usageHours) / 1000 * 60;  // Multiply by 60 days
        }

        // Calculate bill based on total units and TNEB tariff
        double billAmount = calculateBillAmount(totalUnits);

        // Return the response containing total units and bill amount
        return ResponseEntity.ok(new BillResponse(totalUnits, billAmount));
    }

    // Method to calculate the bill amount based on total units consumed
    private double calculateBillAmount(double totalUnits) {
        double billAmount = 0;
		
		if (totalUnits <= 500) {
            if (totalUnits <= 100) {
                billAmount = totalUnits * 0.00;
            } else if (totalUnits <= 200) {
                billAmount = 100 * 0.00 + (totalUnits - 100) * 2.35;
            } else if (totalUnits <= 400) {
                billAmount = 100 * 0.00 + 100 * 2.35 + (totalUnits - 200) * 4.70;
            } else if (totalUnits <= 500) {
                billAmount = 100 * 0.00 + 100 * 2.35 + 200 * 4.70 + (totalUnits - 400) * 6.30;
            }
        } else {
            if (totalUnits <= 100) {
                billAmount = totalUnits * 0.00;
            } else if (totalUnits <= 400) {
                billAmount = 100 * 0.00 + (totalUnits - 100) * 4.70;
            } else if (totalUnits <= 500) {
                billAmount = 100 * 0.00 + 300 * 4.70 + (totalUnits - 400) * 6.30;
            } else if (totalUnits <= 600) {
                billAmount = 100 * 0.00 + 300 * 4.70 + 100 * 6.30 + (totalUnits - 500) * 8.40;
            } else if (totalUnits <= 800) {
                billAmount = 100 * 0.00 + 300 * 4.70 + 100 * 6.30 + 100 * 8.40 + (totalUnits - 600) * 9.45;
            } else if (totalUnits <= 1000) {
                billAmount = 100 * 0.00 + 300 * 4.70 + 100 * 6.30 + 100 * 8.40 + 200 * 9.45 + (totalUnits - 800) * 10.50;
            } else {
                billAmount = 100 * 0.00 + 300 * 4.70 + 100 * 6.30 + 100 * 8.40 + 200 * 9.45 + 200 * 10.50 + (totalUnits - 1000) * 11.55;
            }
        }
        return billAmount;
    }
}

// Response object to hold the total units and bill amount
class BillResponse {
    private double totalUnits;
    private double billAmount;

    public BillResponse(double totalUnits, double billAmount) {
        this.totalUnits = totalUnits;
        this.billAmount = billAmount;
    }

    public double getTotalUnits() {
        return totalUnits;
    }

    public double getBillAmount() {
        return billAmount;
    }
}

// Request object to capture the appliance data
class ApplianceData {
    private List<String> applianceName;
    private List<Double> wattage;
    private List<Double> usageHours;

    // Getters and Setters
    public List<String> getApplianceName() {
        return applianceName;
    }

    public void setApplianceName(List<String> applianceName) {
        this.applianceName = applianceName;
    }

    public List<Double> getWattage() {
        return wattage;
    }

    public void setWattage(List<Double> wattage) {
        this.wattage = wattage;
    }

    public List<Double> getUsageHours() {
        return usageHours;
    }

    public void setUsageHours(List<Double> usageHours) {
        this.usageHours = usageHours;
    }
}
