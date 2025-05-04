package com.ese.modbus;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ModbusStringClient {

    private ModbusTCPMaster modbusMaster;

    public ModbusStringClient(String ip, int port) {
        modbusMaster = new ModbusTCPMaster(ip, port);
        modbusMaster.setTimeout(5000); // Increase timeout to 5 seconds
        modbusMaster.setRetries(10);   // Increase retries
    }

    public void connect() throws Exception {
        modbusMaster.connect();
    }

    public void disconnect() {
        modbusMaster.disconnect();
    }

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




}
