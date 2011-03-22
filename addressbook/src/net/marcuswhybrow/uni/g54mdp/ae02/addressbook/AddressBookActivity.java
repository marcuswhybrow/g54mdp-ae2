package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;

import android.util.Log;

public class AddressBookActivity extends Activity
{
    private static Context context = null;
    
    private TextView fullName;
    private TextView age;
    private TextView address;
    private TextView telephoneNumber;
    private TextView emailAddress;
    
    public static Context getContext() {
        return context;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.main);
        
        fullName = (TextView) findViewById(R.id.full_name);
        age = (TextView) findViewById(R.id.age);
        address = (TextView) findViewById(R.id.address);
        telephoneNumber = (TextView) findViewById(R.id.telephone_number);
        emailAddress = (TextView) findViewById(R.id.email_address);
        
        Model contacts[] = Contact.objects.all();
        
        try {
            Contact contact = (Contact) contacts[0];
            fullName.setText(contact.getField(Contact.FULL_NAME));
            age.setText(contact.getField(Contact.AGE));
            address.setText(contact.getField(Contact.ADDRESS));
            telephoneNumber.setText(contact.getField(Contact.TELEPHONE_NUMBER));
            emailAddress.setText(contact.getField(Contact.EMAIL_ADDRESS));
        } catch(Exception ex) {}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.contact_add:
            startActivityForResult(new Intent(this, ContactFormActivity.class), 0);
            return true;
        case R.id.contact_edit:
            return true;
        case R.id.contact_delete:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
