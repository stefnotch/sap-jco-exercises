package at.htl;
import com.sap.conn.jco.*;

import java.util.Scanner;

// SAP Exercise 13
// by Stefnotch

public class MainCustomerGetDetail {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            JCoDestination dest = JCoDestinationManager.getDestination("ABAP_B15");

            System.out.print("Kunde: ");
            String customerId = sc.nextLine(); // T-L69A01
            System.out.print("Verkaufsorg.: ");
            String salesOrg = sc.nextLine(); // 1000

            Customer customer = getCustomer(dest, customerId, salesOrg);
            System.out.println(customer);

            // And now, use CIC3 to look up the customer

        } catch(JCoException e) {
            System.err.println(e.getMessage());
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Customer getCustomer(JCoDestination dest, String customerId, String salesOrg) throws JCoException {
        Customer customer = new Customer();
        customer.customerId = customerId;
        customer.salesOrg = salesOrg;

        JCoFunction getDetailFunction = dest.getRepository().getFunction("BAPI_CUSTOMER_GETDETAIL1");
        getDetailFunction.getImportParameterList().setValue("CUSTOMERNO", customer.customerId);
        getDetailFunction.getImportParameterList().setValue("PI_SALESORG", customer.salesOrg);
        getDetailFunction.execute(dest);

        JCoStructure returnValue = getDetailFunction.getExportParameterList().getStructure("RETURN");
        if(!isSuccessfulReturnCode(returnValue.getString("TYPE"))) {
            throw new RuntimeException(returnValue.getString("CODE") + " : " + returnValue.getString("MESSAGE"));
        }

        JCoStructure personalData = getDetailFunction.getExportParameterList().getStructure("PE_PERSONALDATA");
        customer.name = personalData.getString("FIRSTNAME") + " " + personalData.getString("LASTNAME");
        customer.city = personalData.getString("CITY");
        customer.street = personalData.getString("STREET");
        customer.country = personalData.getString("COUNTRY");

        return customer;
    }

    public static boolean isSuccessfulReturnCode(String returnCode) {
        return returnCode.equals("") || returnCode.equals("S") || returnCode.equals("I");
    }
}
