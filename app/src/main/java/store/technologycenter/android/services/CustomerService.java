package store.technologycenter.android.services;

import store.technologycenter.android.models.Customer;

import io.realm.Realm;

public class CustomerService {

    public static String CUSTOMER_UUID = "ea17-b4aa011a-9a03-11eb-0242ac130003"; //DO NOT CHANGE

    public static Customer getCustomer(Realm realm){
        return realm.where(Customer.class).equalTo(Customer.COL_UUID, CUSTOMER_UUID).findFirst();
    }

    public static void createCustomer(Realm realm){
        if(getCustomer(realm) != null) return;
        Customer customer = new Customer(
                CUSTOMER_UUID,
                null,
                null,
                null,
                null,
                null
        );

        realm.executeTransaction(r -> r.insert(customer));
    }


}
