package R8000_DMR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static java.lang.Integer.parseInt;
import java.net.NoRouteToHostException;


public class R8000 {


    //TODO use config file for IP address information. --KV pairs might include serial number info, version info and features.
    private static final String IP = "169.254.227.014"; //Default IP address using APIPA
    private static final int PORT = 4000; //Service monitor communication port.  Doesn't change.
    private String[] lastResult;  //Return data from service monitor. No true history.
    private int retryCount;  //Number of times to try any command.




    public R8000() {
        //TODO add a logger file.
        this.retryCount = 5;
        this.lastResult = new String[3];
    }

    public String[] getLastResult() {
        return lastResult;
    }

    public String sendCommand(String command) {
        PrintWriter out = null;
        BufferedReader in = null;


        try {
            Socket sock = new Socket(IP, PORT);
            out = new PrintWriter(sock.getOutputStream(), true);


            out.println(command);

            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println(command); //add log.info


            this.lastResult = (in.readLine().split(":"));
            System.out.println(lastResult[0]);

           out.close();
           in.close();
           sock.close();

           if (parseInt(lastResult[0]) > 2 ){
               //TODO this should be a check for valid result from monitor.
               retryCount = 5;
               return "error";
           }

           if (lastResult.length >1){
               retryCount = 5;
               return lastResult[1];
           }else {
               if (retryCount == 0){return "error";}  //Should throw some sort of exception to be caught.
               retryCount--;
               sendCommand(command);
               //add log.error
           }


        } catch (NoRouteToHostException e){
            return " Link Err";
        }


        catch (IOException e) {
            System.out.println(e.getMessage());
            //add log.fatal
        }
        return "    error";
    }

    public void initCommand(String command) {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            Socket sock = new Socket(IP, PORT);
            out = new PrintWriter(sock.getOutputStream(), true);


            out.println(command);

            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println(command);  //change this to log.info


            this.lastResult = (in.readLine().split(":"));
            System.out.println(lastResult[0]);


           out.close();
           in.close();
           sock.close();
            if (parseInt(lastResult[0]) < 3 ){
                //TODO this should be a check for valid result from monitor.
                retryCount = 5;

                return;
            }else{
                //TODO add popup error
                retryCount--;
                initCommand(command);
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
            //add log.fatal
        }

    }

    public String sendCommand(String command, Long delay) {
        PrintWriter out = null;
        BufferedReader in = null;


        try {
            Socket sock = new Socket(IP, PORT);
            out = new PrintWriter(sock.getOutputStream(), true);


            out.println(command);

            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println(command);
            Thread.sleep(delay);

            this.lastResult = (in.readLine().split(":"));
            System.out.println(lastResult[0]);


           out.close();
           in.close();
           sock.close();
            if (parseInt(lastResult[0]) > 2 ){
                //TODO this should be a check for valid result from monitor.
                return "error";
            }

            if (lastResult.length >1){
                retryCount = 5;
                return lastResult[1];
            }else {
                if (retryCount == 0){return "error";}  //Should throw some sort of exception to be caught.
                retryCount--;
                sendCommand(command, delay);
                //add log.error
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
