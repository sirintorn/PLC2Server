package com.ese;

import com.ese.bean.Workorder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WorkOrderAPIClient {
    String prqId;
    private static final String BASE_URL = "http://metalbuildingthai-app.com:8080/MBTService/api/getWorkOrderByPrqId/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
    }

    public WorkOrderAPIClient(String prqId) {
        this.prqId = prqId;
    }

    public static Workorder getWorkOrder(String prqId) throws Exception {
        // Construct the full URL with the prqId parameter
        String apiUrl = BASE_URL + prqId;

        // Create URL object
        URL url = new URL(apiUrl);

        // Open connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method
        connection.setRequestMethod("GET");

        // Set headers
        connection.setRequestProperty("Accept", "application/json");

        // Get response code
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            // Read the response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Convert JSON to Workorder object
            return objectMapper.readValue(response.toString(), Workorder.class);
        } else {
            throw new RuntimeException("Failed to get work order. HTTP error code: " + responseCode);
        }
    }

    public Workorder WorkOrderAPIClient() {
        try {
            // Example usage with the PRQ ID from your example
            Workorder workorder = getWorkOrder(prqId);

            // Print workorder details
            System.out.println("Work Order Details:");
            System.out.println("ID: " + workorder.getId());
            System.out.println("WO ID: " + workorder.getWoId());
            System.out.println("WO Barcode: " + workorder.getWoBarcode());
            System.out.println("WO Date: " + workorder.getWoDate());
            System.out.println("Project: " + workorder.getProject());
            System.out.println("Status: " + workorder.getStatus());
            System.out.println("Actual Start: " + workorder.getActualStart());
            System.out.println("Actual Finish: " + workorder.getActualFinish());
            return workorder;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}