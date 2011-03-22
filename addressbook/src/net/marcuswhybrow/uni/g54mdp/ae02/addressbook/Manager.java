package net.marcuswhybrow.uni.g54mdp.ae02.addressbook;

import android.provider.BaseColumns;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class Manager {
    private Class cls;
    private Model model = null;
    
    public Manager(Class cls) {
        this.cls = cls;
        try {
            model = (Model) cls.newInstance();
        } catch(InstantiationException ie) {
        } catch(IllegalAccessException iae) {
        }
    }
    
    public Model[] all() {
        SQLiteDatabase db = model.getOpenHelper().getReadableDatabase();
        Cursor cursor = db.query(
            model.TABLE_NAME,   // the table name to query
            null,                   // retrieve all columns
            null, null,             // select all rows
            null, null,             // do not group rows
            BaseColumns._ID         // the column to order by
        );
        
        Model[] objects = new Model[cursor.getCount()];
        cursor.moveToFirst();
        int count = 0;
        while (cursor.isAfterLast() == false) {
            try {
                Model newModel = (Model) cls.newInstance();
                for (int i = cursor.getColumnCount() - 1; i >= 0; i--)
                    newModel.setField(cursor.getColumnName(i), cursor.getString(i));
                objects[count++] = newModel;
            } catch(InstantiationException ie) {}
              catch(IllegalAccessException iae) {}
            cursor.moveToNext();
        }
        
        cursor.close();
        db.close();
        
        return objects;
    }
    
/*    private Object getValue(Cursor cursor, int i) {
        switch (cursor.getType(i)) {
            case 0: //Cursor.FIELD_TYPE_NULL:
            case 2: //Cursor.FIELD_TYPE_FLOAT:
            case 4: //Cursor.FIELD_TYPE_BLOB:
                break;
            case 1: //Cursor.FIELD_TYPE_INTEGER:
                return (Object) cursor.getInt(i);
            case 3: //Cursor.FIELD_TYPE_STRING:
                return (Object) cursor.getString(i);
        }
        return null;
    }*/
}