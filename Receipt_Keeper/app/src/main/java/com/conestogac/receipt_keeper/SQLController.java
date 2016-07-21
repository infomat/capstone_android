package com.conestogac.receipt_keeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.conestogac.receipt_keeper.helpers.DBHelper;
import com.conestogac.receipt_keeper.models.Receipt;
import com.conestogac.receipt_keeper.models.Store;
import com.conestogac.receipt_keeper.models.Tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class SQLController {

    private DBHelper dbhelper;
    private Context ourContext;
    private SQLiteDatabase database;

    // Logcat tag
    private static final String LOG_NAME = "DatabaseHelper";


    public SQLController(Context C) {
        ourContext = C;
    }

    public SQLController open() throws SQLException {
        dbhelper = new DBHelper(ourContext);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbhelper.close();
        database.close();
    }


    /*public Cursor getAllReceipts() {

        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_RECEIPT + " re, "
                + DBHelper.TABLE_STORE + " st "
                + " WHERE re." + DBHelper.RECEIPT_FK_STORE_ID + "=st." + DBHelper.STORE_ID
                + " ORDER BY re." + DBHelper.RECEIPT_DATE;
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }*/




   /* public Cursor getAllReceipts() {

        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_RECEIPT + " re, "
                + DBHelper.TABLE_STORE + " st " *//*+ DBHelper.TABLE_STORE + " st " *//*
                + " WHERE re." + DBHelper.RECEIPT_FK_STORE_ID + "=st." + DBHelper.STORE_ID
                + " ORDER BY re." + DBHelper.RECEIPT_DATE;
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }*/

    /*
    SELECT *
    FROM receipt re INNER JOIN tag tg
    ON re.tag_id=tag._id
    WHERE tg.name=""
    ORDER BY re.receiptdate
     */

    public Cursor getAllReceiptsWithTagName(String tagName) {

        String sqlQuery = "SELECT * FROM "
                + DBHelper.TABLE_RECEIPT + " re INNER JOIN " + DBHelper.TABLE_TAG + " tg"
                + " ON re." + DBHelper.RECEIPT_ID
                + " =tg." + DBHelper.TAG_ID
                + " WHERE tg." + DBHelper.TAG_NAME + "= '" + tagName + "'"
                + " ORDER BY re." + DBHelper.RECEIPT_DATE;
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }


/*    public Cursor getAllReceipts() {

        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_RECEIPT + " re, "
                + DBHelper.TABLE_STORE + " st " *//*+ DBHelper.TABLE_STORE + " st " *//*
                + " WHERE re." + DBHelper.RECEIPT_FK_STORE_ID + "=st." + DBHelper.STORE_ID
                + " ORDER BY re." + DBHelper.RECEIPT_DATE;
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }*/

    public Cursor getAllReceipts() {

        String sqlQuery = "SELECT re.*, tg.* , st.*" + " FROM "
                + DBHelper.TABLE_STORE + " st, "
                + DBHelper.TABLE_RECEIPT + " re "
                + " INNER JOIN " + DBHelper.TABLE_RECEIPT_TAG + " rt "
                + " ON rt." + DBHelper.FK_RECEIPT_ID + "=re." + DBHelper.RECEIPT_ID
                + " INNER JOIN " + DBHelper.TABLE_TAG + " tg "
                + " ON rt." + DBHelper.FK_TAG_ID + "=tg." + DBHelper.TAG_ID
                //+ " WHERE re." + DBHelper.RECEIPT_ID + "=rt." + DBHelper.FK_RECEIPT_ID
                + " WHERE re." + DBHelper.RECEIPT_FK_STORE_ID + "=st." + DBHelper.STORE_ID
                + " AND re." + DBHelper.RECEIPT_ID + "=rt." + DBHelper.FK_RECEIPT_ID
//                + " AND re." + DBHelper.RECEIPT_FK_CUSTOMER_ID + "<> -1"
                // + " AND tg." + DBHelper.TAG_ID + "=rt." + DBHelper.FK_TAG_ID
                + " GROUP BY re." + DBHelper.RECEIPT_ID
                + " ORDER BY re." + DBHelper.RECEIPT_DATE;

        Log.d(LOG_NAME, sqlQuery);
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }


    public Cursor getAllReceiptsWithValue(String tagName) {

        String sqlQuery = "SELECT re.*, tg.* , st.*" + " FROM "
                + DBHelper.TABLE_STORE + " st, "
                + DBHelper.TABLE_RECEIPT + " re "
                + " INNER JOIN " + DBHelper.TABLE_RECEIPT_TAG + " rt "
                + " ON rt." + DBHelper.FK_RECEIPT_ID + "=re." + DBHelper.RECEIPT_ID
                + " INNER JOIN " + DBHelper.TABLE_TAG + " tg "
                + " ON rt." + DBHelper.FK_TAG_ID + "=tg." + DBHelper.TAG_ID
                //+ " WHERE re." + DBHelper.RECEIPT_ID + "=rt." + DBHelper.FK_RECEIPT_ID
                + " WHERE re." + DBHelper.RECEIPT_FK_STORE_ID + "=st." + DBHelper.STORE_ID
                + " AND re." + DBHelper.RECEIPT_ID + "=rt." + DBHelper.FK_RECEIPT_ID
                + " AND tg." + DBHelper.TAG_NAME + "= '" + tagName + "'" + " COLLATE NOCASE "
                // + " AND tg." + DBHelper.TAG_ID + "=rt." + DBHelper.FK_TAG_ID
                + " GROUP BY re." + DBHelper.RECEIPT_ID
                + " ORDER BY re." + DBHelper.RECEIPT_DATE;

        Log.d(LOG_NAME, sqlQuery);
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }

    public long updateReceipt(Receipt receipt, LinkedList<Tag> tags) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.RECEIPT_FK_CUSTOMER_ID, receipt.getCustomerId());
        values.put(DBHelper.RECEIPT_FK_STORE_ID, receipt.getStoreId());
        values.put(DBHelper.RECEIPT_FK_CATEGORY_ID, receipt.getCategoryId());
        values.put(DBHelper.RECEIPT_COMMENT, receipt.getComment());
        values.put(DBHelper.RECEIPT_CREATEDATE, getDateTime());
        values.put(DBHelper.RECEIPT_DATE, receipt.getDate());
        values.put(DBHelper.RECEIPT_TOTAL, receipt.getTotal());
        values.put(DBHelper.RECEIPT_PAYMENT_METHOD, receipt.getPaymentMethod());


        if (receipt.getUrl() != null) {
            values.put(DBHelper.RECEIPT_URL, receipt.getUrl());
        }

        long receiptId = database.update(DBHelper.TABLE_RECEIPT, values, DBHelper.RECEIPT_ID
                + " = '" + receipt.getLocalId() + "'", null);

        if (tags != null) {
            // Assigning tags to
            for (Tag tag : tags) {
                long tagId = getTagIdByName(tag.getTagName());
                if (tagId != -1) {
                    updateReceiptTag(receipt.getLocalId(), tagId);
                } else {
                    long newTagId = insertTag(tag);
                    updateReceiptTag(receipt.getLocalId(), newTagId);

                }
            }
        }

        insertStoreCategory(receipt.getStoreId(), receipt.getCategoryId());
        return receiptId;
    }

    public long updateReceiptTag(long receiptId, long tagId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.FK_RECEIPT_ID, receiptId);
        values.put(DBHelper.FK_TAG_ID, tagId);

        return database.update(DBHelper.TABLE_RECEIPT_TAG, values, DBHelper.FK_RECEIPT_ID
                + " = " + receiptId, null);
    }

    public long insertReceipt(Receipt receipt, LinkedList<Tag> tags) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.RECEIPT_FK_CUSTOMER_ID, receipt.getCustomerId());
        values.put(DBHelper.RECEIPT_FK_STORE_ID, receipt.getStoreId());
        values.put(DBHelper.RECEIPT_FK_CATEGORY_ID, receipt.getCategoryId());
        values.put(DBHelper.RECEIPT_COMMENT, receipt.getComment());
        values.put(DBHelper.RECEIPT_CREATEDATE, getDateTime());
        values.put(DBHelper.RECEIPT_DATE, receipt.getDate());
        values.put(DBHelper.RECEIPT_TOTAL, receipt.getTotal());
        values.put(DBHelper.RECEIPT_PAYMENT_METHOD, receipt.getPaymentMethod());


        if (receipt.getUrl() != null) {
            values.put(DBHelper.RECEIPT_URL, receipt.getUrl());
        }

        // Insert row
        long receiptId = database.insert(DBHelper.TABLE_RECEIPT, null, values);

        if (tags != null) {
            // Assigning tags to
            for (Tag tag : tags) {
                long tagId = getTagIdByName(tag.getTagName());
                if (tagId != -1)
                    insertReceiptTag(receiptId, tagId);
                else {
                    long newTagId = insertTag(tag);
                    insertReceiptTag(receiptId, newTagId);

                }
            }
        }
        insertStoreCategory(receipt.getStoreId(), receipt.getCategoryId());


        return receiptId;
    }

    public Cursor getStoreCategoryIds() {
        Cursor localCursor = this.database.query(DBHelper.TABLE_STORE_CATEGORY,
                new String[]{
                        DBHelper.STORE_CATEGORY_FK_CATEGORY_ID,
                        DBHelper.STORE_CATEGORY_FK_STORE_ID,
                }
                , null,
                null, null, null, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;
    }

    public long insertStoreCategory(long storeId, long categoryId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.STORE_CATEGORY_FK_CATEGORY_ID, categoryId);
        values.put(DBHelper.STORE_CATEGORY_FK_STORE_ID, storeId);

        return database.insert(DBHelper.TABLE_STORE_CATEGORY, null, values);
    }

    public long insertReceiptTag(long receiptId, long tagId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.FK_RECEIPT_ID, receiptId);
        values.put(DBHelper.FK_TAG_ID, tagId);

        return database.insert(DBHelper.TABLE_RECEIPT_TAG, null, values);
    }

    private int getTagIdByName(String tagName) {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_TAG + " WHERE " + DBHelper.TAG_NAME + "=\'" + tagName + "\'";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor != null) {
            localCursor.moveToFirst();
            return localCursor.getInt(localCursor.getColumnIndex(DBHelper.TAG_ID));
        } else {
            return -1;
        }
    }


    protected String getTagNameById(int tagId) {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_TAG + " WHERE " + DBHelper.TAG_ID + "=\'" + tagId + "\'";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor != null) {
            localCursor.moveToFirst();
            return localCursor.getString(localCursor.getColumnIndex(DBHelper.TAG_NAME));
        } else {
            return null;
        }
    }


    public int getCategoryIdByName(String categoryName) {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_CATEGORY + " WHERE " + DBHelper.CATEGORY_NAME + "=\'" + categoryName + "\'";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor != null) {
            localCursor.moveToFirst();
            return localCursor.getInt(localCursor.getColumnIndex(DBHelper.CATEGORY_ID));
        } else {
            return -1;
        }
    }


    public String getCategoryNameById(int categoryId) {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_CATEGORY + " WHERE " + DBHelper.CATEGORY_ID + "=\'" + categoryId + "\'";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor != null) {
            localCursor.moveToFirst();
            return localCursor.getString(localCursor.getColumnIndex(DBHelper.CATEGORY_NAME));
        } else {
            return null;
        }
    }


    public long insertTag(Tag tag) {
        ContentValues values = new ContentValues();
        //values.put(TAG_ID, tag.getTagId());
        values.put(DBHelper.TAG_NAME, tag.getTagName());


        return database.insert(DBHelper.TABLE_TAG, null, values);
    }

    public Cursor getAllTags() {

        Cursor localCursor = this.database.query(DBHelper.TABLE_TAG,
                new String[]{
                        DBHelper.TAG_ID,
                        DBHelper.TAG_NAME,
                }
                , null,
                null, null, null, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }


    public Cursor getAllReceiptTag() {

        Cursor localCursor = this.database.query(DBHelper.TABLE_RECEIPT_TAG,
                new String[]{
                        DBHelper.FK_RECEIPT_ID,
                        DBHelper.FK_TAG_ID,
                }
                , null,
                null, null, null, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;

    }

    public Cursor getAllUnSyncTag() {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_TAG + " WHERE " + DBHelper.TAG_IS_SYNCED + "=0";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            return localCursor;
        } else {
            return null;
        }
    }

    public int insertStoreByName(String name) {
        Long storeId;
        ContentValues cv = new ContentValues();

        storeId = findStore(name);

        if (storeId == 0) {
            cv.put(DBHelper.STORE_NAME, name);
            storeId = database.insert(DBHelper.TABLE_STORE, null, cv);
        }

        return storeId.intValue();
    }

    public long insertStore(Store store) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.STORE_NAME, store.getName());

        return database.insert(DBHelper.TABLE_STORE, null, values);
    }

    private long findStore(String name) {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_STORE + " WHERE " + DBHelper.STORE_NAME + "=\'" + name + "\'";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            return localCursor.getInt(localCursor.getColumnIndex(DBHelper.STORE_ID));
        } else {
            return 0;
        }
    }

    public Cursor getAllUnSyncStore() {
        String sqlQuery = "SELECT * FROM " + DBHelper.TABLE_STORE + " WHERE " + DBHelper.STORE_IS_SYNCED + "=0";
        Cursor localCursor = this.database.rawQuery(sqlQuery, null);

        if (localCursor.getCount() > 0) {
            localCursor.moveToFirst();
            return localCursor;
        } else {
            return null;
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean isSync(long receiptId) {
        boolean flag = false;
        Cursor localCursor = database.query(DBHelper.TABLE_RECEIPT,
                new String[]{
                        DBHelper.RECEIPT_IS_SYNCED
                }
                , DBHelper.RECEIPT_ID + "=?",
                new String[]{String.valueOf(receiptId)},
                null, null, null);

        if (localCursor != null) {
            localCursor.moveToFirst();
            if (localCursor.getInt(localCursor.getColumnIndexOrThrow(DBHelper.RECEIPT_IS_SYNCED)) == 1) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    // [ Delete record using id in the database]
    // [if isSync with remote set customerId to -1 , else delete it ]
    public void deleteReceipt(long receiptID) {
        long id;
        if (!isSync(receiptID)) {
            id = database.delete(DBHelper.TABLE_RECEIPT,
                    DBHelper.RECEIPT_ID + "=?",
                    new String[]{String.valueOf(receiptID)});

            Cursor cursor = this.getAllReceiptTag();

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FK_RECEIPT_ID)) == receiptID) {
                        database.delete(DBHelper.TABLE_RECEIPT_TAG, DBHelper.FK_RECEIPT_ID + "=?", new String[]{String.valueOf(receiptID)});
                    }
                }
            }
        } else {

            ContentValues values = new ContentValues();
            values.put(DBHelper.RECEIPT_FK_CUSTOMER_ID, -1);

            id = database.update(DBHelper.TABLE_RECEIPT, values, DBHelper.RECEIPT_ID
                    + " = '" + receiptID + "'", null);
        }


    }


    /*public String convertDateToString() {
        String datetime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        datetime = dateFormat.format(date);
        System.out.println("Current Date Time : " + datetime);


        return datetime;
    }*/


    /*// [ Delete record using its location in the list]
    public void delete(int orderInList) {
        List<Integer> database_ids = new ArrayList<Integer>();
        Cursor c = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_USER, null);
        while (c.moveToNext()) {
            database_ids.add(Integer.parseInt(c.getString(0)));
        }
        database.delete(DBHelper.TABLE_USER, DBHelper.USER_ID + " =?",
                new String[]{String.valueOf(database_ids.get(orderInList))});
    }
*/


}
