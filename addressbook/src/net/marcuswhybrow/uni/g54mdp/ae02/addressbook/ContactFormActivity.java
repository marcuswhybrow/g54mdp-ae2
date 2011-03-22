package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;

import android.util.Log;

public class ContactFormActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_form);
        
        Button save = (Button) findViewById(R.id.contact_save);
        
        final EditText fullName = (EditText) findViewById(R.id.fullName);
        final EditText age = (EditText) findViewById(R.id.age);
        final EditText address = (EditText) findViewById(R.id.address);
        final EditText telephoneNumber = (EditText) findViewById(R.id.telephoneNumber);
        final EditText emailAddress = (EditText) findViewById(R.id.emailAddress);
        
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button b = (Button) v;
                
                Integer ageInteger = null;
                try {
                    ageInteger = new Integer(age.getText().toString());
                } catch(NumberFormatException nfe) {}
                
                Contact contact = new Contact();
                contact.setField(Contact.FULL_NAME, fullName.getText().toString());
                contact.setField(Contact.AGE, ageInteger);
                contact.setField(Contact.ADDRESS, address.getText().toString());
                contact.setField(Contact.TELEPHONE_NUMBER, telephoneNumber.getText().toString());
                contact.setField(Contact.EMAIL_ADDRESS, emailAddress.getText().toString());
                
                contact.save();
            }
        });
    }
}