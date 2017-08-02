/*
 * Copyright 2017 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.crux.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

/**
 *
 * @author joelauer
 */
public class OshiDemo {
    static public void dumpProcessTree(Map<Integer,OSProcess> processesById, Map<Integer,List<OSProcess>> childrenById, int pid) {
        OSProcess p = processesById.get(pid);
        
        System.out.println("process pid=" + p.getProcessID()
            + " ppid=" + p.getParentProcessID()
            + " name=" + p.getName()
            + " rss=" + p.getResidentSetSize());
        
        List<OSProcess> children = childrenById.get(pid);
        if (children != null) {
            for (OSProcess child : children) {
                dumpProcessTree(processesById, childrenById, child.getProcessID());
            }
        }
    }
    
    static public void main(String[] args) throws Exception {
//        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        root.setLevel(Level.INFO);
        
//        Process childProcess = new ProcessBuilder()
//            .inheritIO()
//            .command("firefox")
//            .start();
        
        Process childProcess = new ProcessBuilder().inheritIO()
            .command("google-chrome", "--user-data-dir=" + TemporaryPath.tempDirectory())
            .start();
        
        
        Thread.sleep(10000L);
        
        
        SystemInfo si = new SystemInfo();

        //HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        //int pid = ProcessHelper.getCurrentProcessId().orElse(0L).intValue();
        
        
        //OSProcess process = os.getProcess(pid);
        int pid = os.getProcessId();
        
        long start = System.currentTimeMillis();
        
        OSProcess process = os.getProcess(pid);
        
//        LOG.info("process pid: {}", process.getProcessID());
//        LOG.info("process parent pid: {}", process.getParentProcessID());
//        LOG.info("process name: {}", process.getName());
//        LOG.info("process rss: {}", process.getResidentSetSize());
        
        OSProcess[] processes = os.getProcesses(0, null);
        
        // build tree of processes
        Map<Integer,OSProcess> processesById = new TreeMap<>();
        Map<Integer,List<OSProcess>> childrenById = new TreeMap<>();
        
        for (OSProcess p : processes) {
            processesById.put(p.getProcessID(), p);
            int parentPid = p.getParentProcessID();
            // add self as child
            if (parentPid != 0) {
                List<OSProcess> children = childrenById.computeIfAbsent(parentPid, (key) -> new ArrayList<>());
                children.add(p);
            }
        }
        
        // navigate tree of processes
        dumpProcessTree(processesById, childrenById, pid);
        
        
        System.out.println("Getting all processes took " + (System.currentTimeMillis() - start) + " ms");
        
        
        /**
        System.out.println(os);
        LOG.info("Checking computer system...");
        printComputerSystem(hal.getComputerSystem());
        LOG.info("Checking Processor...");
        printProcessor(hal.getProcessor());
        LOG.info("Checking Memory...");
        printMemory(hal.getMemory());
        LOG.info("Checking CPU...");
        printCpu(hal.getProcessor());
        LOG.info("Checking Processes...");
        printProcesses(os, hal.getMemory());
        LOG.info("Checking Sensors...");
        printSensors(hal.getSensors());
        LOG.info("Checking Power sources...");
        printPowerSources(hal.getPowerSources());
        LOG.info("Checking Disks...");
        printDisks(hal.getDiskStores());
        LOG.info("Checking File System...");
        printFileSystem(os.getFileSystem());
        LOG.info("Checking Network interfaces...");
        printNetworkInterfaces(hal.getNetworkIFs());
        LOG.info("Checking Network parameterss...");
        printNetworkParameters(os.getNetworkParams());
        // hardware: displays
        LOG.info("Checking Displays...");
        printDisplays(hal.getDisplays());
        // hardware: USB devices
        LOG.info("Checking USB Devices...");
        printUsbDevices(hal.getUsbDevices(true));
         */

        
        childProcess.waitFor();
    }
}
