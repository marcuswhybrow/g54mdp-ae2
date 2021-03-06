package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.content.ContentValues;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import android.content.Context;

import android.util.Log;

public class Model {
    protected static Map<String, Object> fields;
    private Map<String, Object> instanceFields;
    
    private ModelOpenHelper openHelper;
    
    protected static String TABLE_NAME = "";
    
    public Model() {
        this.openHelper = new ModelOpenHelper(AddressBookActivity.getContext());
        this.instanceFields = new HashMap<String, Object>();
    }
    
    public void setField(String columnName, Object value) {
        this.instanceFields.put(columnName, value);
    }
    
    public String getField(String columnName) {
        try {
            return this.instanceFields.get(columnName).toString();
        } catch (NullPointerException npe) {}
        return "";
    }
    
    public Map getFields() {
        return this.instanceFields;
    }
    
    public ModelOpenHelper getOpenHelper() {
        return this.openHelper;
    }
    
    public void save() {
        SQLiteDatabase db = this.openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String fieldName;
        Object value;
        for (Map.Entry entry : this.instanceFields.entrySet()) {
            fieldName = (String) entry.getKey();
            value = entry.getValue();
            if (value instanceof Integer)
                contentValues.put(fieldName, (Integer) value);
            else if (value instanceof String)
                contentValues.put(fieldName, (String) value);
        }
        
        if (this.instanceFields.containsKey(BaseColumns._ID) == false) {
            long id = db.insert(TABLE_NAME, null, contentValues);
            
            // id will be -1 if an error occurred
            if (id > -1)
                this.setField(BaseColumns._ID, id);
        } else {
            String whereClause = BaseColumns._ID + "=?";
            String[] whereArgs = {this.instanceFields.get(BaseColumns._ID).toString()};
            db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        }
        db.close();
    }
    
    public void delete() {
        if (this.instanceFields.containsKey(BaseColumns._ID)) {
            String whereClause = BaseColumns._ID + "=?";
            String[] whereArgs = {this.instanceFields.get(BaseColumns._ID).toString()};
            SQLiteDatabase db = this.openHelper.getWritableDatabase();
            int result = db.delete(TABLE_NAME, whereClause, whereArgs);
            db.close();
        }
    }
    
    public class ModelOpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "contact.db";
        
        public ModelOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Set the SQL for creating the table.
            String tableCreate = "CREATE TABLE " + TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
            Set<Map.Entry<String,Object>> entrySet = fields.entrySet();
            int i = entrySet.size();
            for (Map.Entry entry : entrySet) {
                i--;
                String columnName = (String) entry.getKey();
                Object value = entry.getValue();
                
                // column name
                tableCreate += columnName + " ";
                
                // field
                if (value instanceof Integer)
                    tableCreate += "INTEGER";
                else if (value instanceof String)
                    tableCreate += "TEXT";
                else
                    tableCreate += "TEXT";
                    
                if (i > 0)
                    tableCreate += ", ";
            }
            tableCreate += ");";
            
            db.execSQL(tableCreate);
        }
        
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // do nothing
        }
    }
}