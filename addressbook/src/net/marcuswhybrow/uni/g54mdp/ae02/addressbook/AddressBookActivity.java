package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.widget.Button;

import android.util.Log;

public class AddressBookActivity extends Activity
{
    private static Context context = null;
    
    private TextView fullName;
    private TextView age;
    private TextView address;
    private TextView telephoneNumber;
    private TextView emailAddress;
    
    private Button buttonFirst;
    private Button buttonPrevious;
    private Button buttonNext;
    private Button buttonLast;
    
    private Model contacts[];
    private int position = -1;
    
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
        
        // Buttons
        buttonFirst = (Button) findViewById(R.id.contact_first);
        buttonPrevious = (Button) findViewById(R.id.contact_previous);
        buttonNext = (Button) findViewById(R.id.contact_next);
        buttonLast = (Button) findViewById(R.id.contact_last);
        
        buttonFirst.setEnabled(false);
        buttonPrevious.setEnabled(false);
        
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (position + 1 < contacts.length)
                    populateFields((Contact) contacts[++position]);
                    updateMovementButtons();
            }
        });
        
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (position - 1 >= 0) {
                    populateFields((Contact) contacts[--position]);
                    updateMovementButtons();
                }
            }
        });
        
        buttonFirst.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                position = 0;
                populateFields((Contact) contacts[position]);
                updateMovementButtons();
            }
        });
        
        buttonLast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                position = Math.max(0, contacts.length - 1);
                populateFields((Contact) contacts[position]);
                updateMovementButtons();
            }
        });
        
        // Populate fields
        fullName = (TextView) findViewById(R.id.full_name);
        age = (TextView) findViewById(R.id.age);
        address = (TextView) findViewById(R.id.address);
        telephoneNumber = (TextView) findViewById(R.id.telephone_number);
        emailAddress = (TextView) findViewById(R.id.email_address);
        
        contacts = Contact.objects.all();
        
        if (contacts.length > 0)
            this.position = 0;
        
        Contact contact = (Contact) contacts[0];
        this.populateFields(contact);
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
                contacts[position].delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void updateMovementButtons() {
        boolean startEnabled = position > 0;
        boolean endEnabled = position < contacts.length - 1;
        
        buttonFirst.setEnabled(startEnabled);
        buttonPrevious.setEnabled(startEnabled);
        buttonNext.setEnabled(endEnabled);
        buttonLast.setEnabled(endEnabled);
    }
    
    private void populateFields(Contact contact) {
        fullName.setText(contact.getField(Contact.FULL_NAME));
        age.setText(contact.getField(Contact.AGE));
        address.setText(contact.getField(Contact.ADDRESS));
        telephoneNumber.setText(contact.getField(Contact.TELEPHONE_NUMBER));
        emailAddress.setText(contact.getField(Contact.EMAIL_ADDRESS));
    }
}
