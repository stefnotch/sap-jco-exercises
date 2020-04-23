package at.htl;

// SAP Exercise 14
// by Stefnotch

import com.sap.conn.jco.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainListServiceNotifications {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            JCoDestination dest = JCoDestinationManager.getDestination("ABAP_B15");

            System.out.print("Kunde: ");
            String customerId = sc.nextLine(); // T-L69A01

            List<ServiceNotification> notifications = getServiceNotifications(dest, customerId);
            System.out.println(notifications);

          //  notifications.forEach(System.out::println);

            // And now, use IW53 to look up the service notifications

        } catch(JCoException e) {
            System.err.println(e.getMessage());
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static List<ServiceNotification> getServiceNotifications(JCoDestination dest, String customerId) throws JCoException {
        ArrayList<ServiceNotification> notifications = new ArrayList<>();

        JCoFunction getServiceNotificationsFunction = dest.getRepository().getFunction("BAPI_SERVICENOTIFICAT_GETLIST");
        getServiceNotificationsFunction.getImportParameterList().setValue("CUSTOMERNO", customerId);
        getServiceNotificationsFunction.getImportParameterList().setValue("NOTIFICATION_DATE", "2000-01-01");
        getServiceNotificationsFunction.execute(dest);

        JCoStructure returnValue = getServiceNotificationsFunction.getExportParameterList().getStructure("RETURN");
        if(!isSuccessfulReturnCode(returnValue.getString("TYPE"))) {
            throw new RuntimeException(returnValue.getString("CODE") + " : " + returnValue.getString("MESSAGE"));
        }

        JCoTable notifTable = getServiceNotificationsFunction.getTableParameterList().getTable("NOTIFICATION");
        int length = notifTable.getNumRows();
        for (int i = 0; i < length; i++) {
            notifTable.setRow(i);

            ServiceNotification serviceNotification = new ServiceNotification();
            serviceNotification.notification = notifTable.getString("NOTIFICAT");
            serviceNotification.description = notifTable.getString("DESCRIPT");
            notifications.add(serviceNotification);
        }

        return notifications;
    }

    public static boolean isSuccessfulReturnCode(String returnCode) {
        return returnCode.equals("") || returnCode.equals("S") || returnCode.equals("I");
    }
}
