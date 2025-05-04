package com.ese;

import com.ese.modbus.ModbusStringClient;

import java.io.InputStream;
import java.util.Date;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
//    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
//        String mainStatus = "";
//        String mainStatusBuffer = "";
        try (InputStream configFile = ModbusStringClient.class.getClassLoader().getResourceAsStream("logging.properties");){
            if (configFile != null) {
                LogManager.getLogManager().readConfiguration(configFile);
                System.out.println("Logging configuration loaded successfully.");
            } else {
                System.err.println("Could not find logging.properties in src/main/resources.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger(ModbusStringClient.class.getName());
        ModbusStringClient client = new ModbusStringClient("192.168.10.231", 502);    //MDEXA
//        ModbusStringClient client = new ModbusStringClient("192.168.10.232", 502);    //MDEXB
//        ModbusStringClient client = new ModbusStringClient("192.168.10.233", 502);  //MDEXC
        while (new Date().getHours() > 7 && new Date().getHours() < 21) {

            try {
                client.connect();
                String prqId = client.readString(394217, 6);
                logger.info(" PrqId: " + prqId);
                String mainStatus = client.readString(399217, 5);
                logger.info(" MainStatus: " + mainStatus);
                String readStr2 = client.readString(399222, 8);
                logger.info(" Job1Status: " + readStr2);
                int addressIndex = 399302;
                int registryCount = 8;
                int itemList = 30;
                for (int i = 1; i <= itemList; ++i) {
                    String readStr3 = client.readString(addressIndex, registryCount);
                    addressIndex += registryCount;
                    logger.info(" itemSet" + i + ": " + readStr3);
                }
                String readStr5 = client.readString(399616, 10);
                logger.info(" countQty: " + readStr5);
                String readStr4 = client.readString(399542, 8);
                logger.info(" machineStatus: " + readStr4);
                client.disconnect();

                if(prqId.length() >=12){
                    prqId = "2502PR000351";
                    WorkOrderAPIClient workOrderAPIClient = new WorkOrderAPIClient(prqId);
                }
//                if(mainStatus == "FIN" && mainStatusBuffer == "PRO" ){
//                    WorkorderManagement wm = new WorkorderManagement();
//                    wm.updateWorkorderStatus();
//                }
//                mainStatusBuffer = mainStatus;

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.toString());
            }
            System.out.println("");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
