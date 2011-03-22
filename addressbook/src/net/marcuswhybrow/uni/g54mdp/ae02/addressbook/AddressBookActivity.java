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
import java.util.Map;
import java.util.Set;
import android.provider.BaseColumns;
import android.util.SparseArray;
import android.widget.Toast;

import android.util.Log;

public class AddressBookActivity extends Activity
{
    public static final String CONTACT_ADD = "net.marcuswhybrow.uni.g54mdp.ae02.addressbook.CONTACT_ADD";
    public static final String CONTACT_EDIT = "net.marcuswhybrow.uni.g54mdp.ae02.addressbook.CONTACT_EDIT";
    
    public static final int CONTACT_ADD_CODE = 1;
    public static final int CONTACT_EDIT_CODE = 2;
    
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
    
    private SparseArray<Model> contacts;
    private int index = -1;
    
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
                int i = contacts.indexOfKey(index);
                if (i + 1 < contacts.size()) {
                    populateFields((Contact) contacts.valueAt(++i));
                    index = contacts.keyAt(i);
                    updateMovementButtons();
                }
            }
        });
        
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i = contacts.indexOfKey(index);
                if (i - 1 >= 0) {
                    populateFields((Contact) contacts.valueAt(--i));
                    index = contacts.keyAt(i);
                    updateMovementButtons();
                }
            }
        });
        
        buttonFirst.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                index = contacts.keyAt(0);
                populateFields((Contact) contacts.get(index));
                updateMovementButtons();
            }
        });
        
        buttonLast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                index = contacts.keyAt(Math.max(0, contacts.size() - 1));
                populateFields((Contact) contacts.get(index));
                updateMovementButtons();
            }
        });
        
        fullName = (TextView) findViewById(R.id.full_name);
        age = (TextView) findViewById(R.id.age);
        address = (TextView) findViewById(R.id.address);
        telephoneNumber = (TextView) findViewById(R.id.telephone_number);
        emailAddress = (TextView) findViewById(R.id.email_address);
        
        this.refreshContacts(-1);
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
                startActivityForResult(new Intent(CONTACT_ADD), CONTACT_ADD_CODE);
                return true;
            case R.id.contact_edit:
                Intent intent = new Intent(CONTACT_EDIT);
                for (Map.Entry entry : (Set<Map.Entry>) contacts.get(index).getFields().entrySet())
                    intent.putExtra((String) entry.getKey(), entry.getValue().toString());
                startActivityForResult(intent, CONTACT_EDIT_CODE);
                return true;
            case R.id.contact_delete:
                contacts.get(index).delete();
                int i = contacts.indexOfKey(index);
                if (i + 1 < contacts.size()) {
                    this.refreshContacts((int) contacts.keyAt(++i));
                } else if (i - 1 >= 0) {
                    this.refreshContacts((int) contacts.keyAt(--i));
                } else {
                    this.refreshContacts(-1);
                }
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); 
        switch (requestCode) {
            case CONTACT_ADD_CODE:
            case CONTACT_EDIT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    long index = data.getLongExtra(BaseColumns._ID, -1);
                    if (index >= 0)
                        this.refreshContacts((int) index);
                }
                break;
        }
        
        switch (requestCode) {
            case CONTACT_ADD_CODE:
                Toast.makeText(getApplicationContext(), "Contact Created", Toast.LENGTH_SHORT).show();
                break;
            case CONTACT_EDIT_CODE:
                Toast.makeText(getApplicationContext(), "Contact Saved", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    
    private void refreshContacts(int newPosition) {
        contacts = Contact.objects.all();
        
        if (newPosition == -1) {
            if (contacts.size() > 0) {
                newPosition = contacts.keyAt(0);
            }
        }
        
        Model c = contacts.get(newPosition, null);
        if (c != null) {
            this.index = newPosition;
            this.populateFields((Contact) c);
        }
        updateMovementButtons();
    }
    
    private void updateMovementButtons() {
        boolean startEnabled = contacts.indexOfKey(index) > 0;
        boolean endEnabled = contacts.indexOfKey(index) < contacts.size() - 1;
        
        buttonFirst.setEnabled(startEnabled);
        buttonPrevious.setEnabled(startEnabled);
        buttonNext.setEnabled(endEnabled);
        buttonLast.setEnabled(endEnabled);
    }
    
    private void populateFields(Contact contact) {
        if (contact == null) {
            fullName.setText("");
            age.setText("");
            address.setText("");
            telephoneNumber.setText("");
            emailAddress.setText("");
        } else {
            fullName.setText(contact.getField(Contact.FULL_NAME));
            age.setText(contact.getField(Contact.AGE));
            address.setText(contact.getField(Contact.ADDRESS));
            telephoneNumber.setText(contact.getField(Contact.TELEPHONE_NUMBER));
            emailAddress.setText(contact.getField(Contact.EMAIL_ADDRESS));
        }
    }
}
