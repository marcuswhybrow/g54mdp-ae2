package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import java.util.Collections;

public class Contact extends Model {
    public static final String FULL_NAME = "full_name";
    public static final String AGE = "age";
    public static final String ADDRESS = "address";
    public static final String TELEPHONE_NUMBER = "telephone_number";
    public static final String EMAIL_ADDRESS = "email_address";
    
    static {
        TABLE_NAME = "contacts";
    }
    
    public static Manager objects = new Manager(Contact.class);
    
    static {
        fields = new HashMap<String, Object>();
        fields.put(FULL_NAME, new String());
        fields.put(AGE, new Integer(0));
        fields.put(ADDRESS, new String());
        fields.put(TELEPHONE_NUMBER, new String());
        fields.put(EMAIL_ADDRESS, new String());
    }
}