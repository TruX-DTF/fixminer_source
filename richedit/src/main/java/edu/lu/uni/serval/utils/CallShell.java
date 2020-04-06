package edu.lu.uni.serval.utils;

/**
 * Created by anilkoyuncu on 17/04/2018.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CallShell {

    private static Logger log = LoggerFactory.getLogger(CallShell.class);



    public void runShell(String command) throws Exception {

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            System.out.println("Script output: " + s);
        }

    }

    public static void runShell(String command, String port) throws Exception {

        log.trace(command);

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
//            System.out.println("Script output: " + s);
            log.trace("Script output: " + s);
        }
//        Thread.sleep(Integer.valueOf(serverWait));

        String cmd = "redis-cli -p %s ping";
        String cmd1 = String.format(cmd,Integer.valueOf(port));
        runPing(cmd1);


    }

    public static void runPing(String command) throws Exception {
try{
        StringBuffer output = new StringBuffer();
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));


        // read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
//        while ((s = reader.readLine()) != null && !s.equals("PONG")) {
//            System.out.println(s);
//        }

//        while(true){
            s = reader.readLine();

            if(s !=null && s.equals("PONG")){
//                System.out.println(s);
                log.trace(s);

            }else{
                String e;
                if((e = stdError.readLine()) == null) {
                    TimeUnit.MINUTES.sleep(1);

                    runPing(command);
                }else{
                    TimeUnit.MINUTES.sleep(1);
                    System.out.println(e);

                }
            }

//            System.out.println(s);
//        }
        // read any errors from the attempted command
//        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.print(s);
        }

//        System.exit(0);
    }
        catch (IOException e) {
        System.out.println("exception happened - here's what I know: ");
        e.printStackTrace();
        System.exit(-1);
    }

    }

    public static void main(String[] args) throws Exception {

//        runPing("redis-cli -p 6380 ping");
        runShell("bash /Users/anilkoyuncu/bugStudy/release/code/redis/startServer.sh /Users/anilkoyuncu/bugStudy/release/code/redis Defects4JALL0.txt.rdb 6380","6380");
    }




}

