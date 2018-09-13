package edu.lu.uni.serval.fixminer.cluster;

/**
 * Created by anilkoyuncu on 17/04/2018.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
public class CallShell {




    public void runShell(String command) throws Exception {

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            System.out.println("Script output: " + s);
        }

    }

    public void runShell(String command,String serverWait) throws Exception {

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            System.out.println("Script output: " + s);
        }
        Thread.sleep(Integer.valueOf(serverWait));

    }




}

