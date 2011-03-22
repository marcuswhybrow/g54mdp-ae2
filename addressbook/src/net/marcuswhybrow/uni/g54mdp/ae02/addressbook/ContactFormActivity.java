package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.provider.BaseColumns;

import android.util.Log;

public class ContactFormActivity extends Activity
{
    private Contact contact = new Contact();
    
    private EditText fullName;
    private EditText age;
    private EditText address;
    private EditText telephoneNumber;
    private EditText emailAddress;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_form);
        
        Button save = (Button) findViewById(R.id.contact_save);
        
        fullName = (EditText) findViewById(R.id.fullName);
        age = (EditText) findViewById(R.id.age);
        address = (EditText) findViewById(R.id.address);
        telephoneNumber = (EditText) findViewById(R.id.telephoneNumber);
        emailAddress = (EditText) findViewById(R.id.emailAddress);
        
        if (savedInstanceState != null)
            this.onResumeInstanceState(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(BaseColumns._ID))
                contact.setField(BaseColumns._ID, extras.getString(BaseColumns._ID));
        
            fullName.setText(extras.getString(Contact.FULL_NAME));
            age.setText(extras.getString(Contact.AGE));
            address.setText(extras.getString(Contact.ADDRESS));
            telephoneNumber.setText(extras.getString(Contact.TELEPHONE_NUMBER));
            emailAddress.setText(extras.getString(Contact.EMAIL_ADDRESS));
        }
        
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button b = (Button) v;
                
                Integer ageInteger = null;
                try {
                    ageInteger = new Integer(age.getText().toString());
                } catch(NumberFormatException nfe) {}
                
                contact.setField(Contact.FULL_NAME, fullName.getText().toString());
                contact.setField(Contact.AGE, ageInteger);
                contact.setField(Contact.ADDRESS, address.getText().toString());
                contact.setField(Contact.TELEPHONE_NUMBER, telephoneNumber.getText().toString());
                contact.setField(Contact.EMAIL_ADDRESS, emailAddress.getText().toString());
                
                contact.save();
                
                Intent resultIntent = new Intent();
                long id = -1;
                try {
                    id = Long.parseLong(contact.getField(BaseColumns._ID));
                } catch (NumberFormatException nfe) {}
                
                resultIntent.putExtra(BaseColumns._ID, id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState); 
        outState.putString(Contact.FULL_NAME, fullName.getText().toString());
        outState.putString(Contact.AGE, age.getText().toString());
        outState.putString(Contact.ADDRESS, address.getText().toString());
        outState.putString(Contact.TELEPHONE_NUMBER, telephoneNumber.getText().toString());
        outState.putString(Contact.EMAIL_ADDRESS, emailAddress.getText().toString());
    }
    
    protected void onResumeInstanceState (Bundle inState) {
        fullName.setText(inState.getString(Contact.FULL_NAME));
        age.setText(inState.getString(Contact.AGE));
        address.setText(inState.getString(Contact.ADDRESS));
        telephoneNumber.setText(inState.getString(Contact.TELEPHONE_NUMBER));
        emailAddress.setText(inState.getString(Contact.EMAIL_ADDRESS));
    }
}