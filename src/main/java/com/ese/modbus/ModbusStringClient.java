package com.ese.modbus;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.logging.LogManager;

public class ModbusStringClient {

    private ModbusTCPMaster modbusMaster;

    public ModbusStringClient(String ip, int port) {
        modbusMaster = new ModbusTCPMaster(ip, port);
    }

    public void connect() throws Exception {
        modbusMaster.connect();
    }

    public void disconnect() {
        modbusMaster.disconnect();
    }

//    public void writeString(int ref, String str) throws ModbusException {
//        byte[] strBytes = str.getBytes();
//        int registerCount = (strBytes.length + 1) / 2;
//        Register[] registers = new Register[registerCount];
//
//        for (int i = 0; i < registerCount; i++) {
//            int byte1 = (i * 2 < strBytes.length) ? (strBytes[i * 2] & 0xFF) : 0;
//            int byte2 = (i * 2 + 1 < strBytes.length) ? (strBytes[i * 2 + 1] & 0xFF) : 0;
//            int value = (byte1 << 8) | byte2;
//            registers[i] = new SimpleRegister(value);
//        }
//
//        modbusMaster.writeMultipleRegisters(ref, registers);
//    }

    public String readString(int ref, int registerCount) throws ModbusException {
        Register[] registers = modbusMaster.readMultipleRegisters(ref, registerCount);
        byte[] resultBytes = new byte[registerCount * 2];

        for (int i = 0; i < registerCount; i++) {
            int value = registers[i].getValue();

            resultBytes[i * 2] = (byte) (value & 0xFF);
            resultBytes[i * 2 + 1] = (byte) (value >> 8);
        }

        return new String(resultBytes).trim();
    }

    public static void main(String[] args) throws IOException {

        try (InputStream configFile = ModbusStringClient.class.getClassLoader().getResourceAsStream("logging.properties")) {
            if (configFile != null) {
                LogManager.getLogManager().readConfiguration(configFile);
                System.out.println("Logging configuration loaded successfully.");
            } else {
                System.err.println("Could not find logging.properties in src/main/resources.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ModbusStringClient.class.getName());

        ModbusStringClient client = new ModbusStringClient("192.168.10.232", Modbus.DEFAULT_PORT); // Replace with your PLC IP
        while(new Date().getHours() >7 && new Date().getHours() < 21){
            try {
                client.connect();
                String readStr = client.readString(399200, 100); // read from PLC return
//                System.out.println(new Date()+" Read String1: " + readStr);
                logger.info(" Read String1: " + readStr);

                String readStr2 = client.readString(399300, 100); // read from PLC return
//                System.out.println(new Date()+"Read String2: " + readStr2);
                logger.info(" Read String2: " + readStr2);

                String readStr3 = client.readString(399400, 100); // read from PLC return
//                System.out.println(new Date()+"Read String3: " + readStr3);
                logger.info(" Read String3: " + readStr3);

                String readStr4 = client.readString(399517, 100); // read from PLC return
//                System.out.println(new Date()+"Read String4: " + readStr4);
                logger.info(" Read String4: " + readStr4);

                String readStr5 = client.readString(399616, 10); // read from PLC return
//                System.out.println(new Date()+"Read Qty: " + readStr5);
                logger.info(" Read String5: " + readStr5);
                client.disconnect();

//                logger.info(readStr+" "+readStr2);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.toString());
            }

            System.out.println("");
            try {
                Thread.sleep( 1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

        }

    }
}
