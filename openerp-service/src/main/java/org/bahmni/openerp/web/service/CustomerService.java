package org.bahmni.openerp.web.service;

import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service
public class CustomerService {
    private OpenERPClient openERPClient;

    @Autowired
    public CustomerService(OpenERPClient openERPClient) {
        this.openERPClient = openERPClient;
    }

    public void create(String name, String patientId) {
        if (noCustomersFound(findCustomerWithPatientReference(patientId))) {
            openERPClient.create("res.partner", name, patientId);
        } else
            throw new OpenERPException(String.format("Customer with id, name already exists: %s, %s ", patientId, name));
    }

    public void deleteCustomerWithPatientReference(String patientId) {
        Object[] customerIds = findCustomerWithPatientReference(patientId);
        Vector params = new Vector();
        params.addElement(customerIds[0]);
        openERPClient.delete("res.partner", params);
    }

    private Object[] findCustomerWithPatientReference(String patientId) {
        Object args[] = {"ref", "=", patientId};
        Vector params = new Vector();
        params.addElement(args);
        return (Object[]) openERPClient.search("res.partner", params);
    }

    private boolean noCustomersFound(Object[] customers) {
        return customers.length == 0;
    }
}
