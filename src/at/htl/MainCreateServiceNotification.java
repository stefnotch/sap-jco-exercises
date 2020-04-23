package at.htl;

import com.sap.conn.jco.*;

import java.util.Scanner;

// SAP Exercise 12
// by Stefnotch

public class MainCreateServiceNotification {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            JCoDestination dest = JCoDestinationManager.getDestination("ABAP_B15");

            System.out.print("Message: ");
            String msg = sc.nextLine();
            String messageId = sendMessage(dest, msg, "T-L69A01");
            System.out.println(messageId);

            // And now, use IW53 to look up the message ID

        } catch(JCoException e) {
            System.err.println(e.getMessage());
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static String sendMessage(JCoDestination dest, String msg, String customer) throws JCoException {
        JCoFunction fun = dest.getRepository().getFunction("BAPI_SERVICENOTIFICAT_CREATE");
        JCoStructure notifStructure = fun.getImportParameterList().getStructure("NOTIF_HEADER");
        notifStructure.setValue("NOTIF_TYPE", "S3");
        notifStructure.setValue("CUSTOMER", customer);
        notifStructure.setValue("DESCRIPT", msg);

        fun.execute(dest);

        JCoStructure returnValue = fun.getExportParameterList().getStructure("RETURN");
        if(returnValue.getString("TYPE").equals("E")) {
            throw new RuntimeException(returnValue.getString("CODE") + " : " + returnValue.getString("MESSAGE"));
        }

        String number = fun.getExportParameterList().getString("NUMBER");
        return number;
    }
}
