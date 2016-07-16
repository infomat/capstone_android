package com.conestogac.receipt_keeper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.conestogac.receipt_keeper.helpers.DBHelper;
import com.conestogac.receipt_keeper.helpers.GlideUtil;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by infomat on 16-07-09.
 */
public class ReceiptCursorAdapter extends CursorAdapter {
    /**
     * Cursor Adapter to show cursor value on list item by reading from Database
     * This will be bind to listview which layout is defined at task_item.xml
     *
     */
    private static final String TAG = ReceiptCursorAdapter.class.getSimpleName();
    private Context curConext;
    private File file;

    // Default constructor
    public ReceiptCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        curConext = context;
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, final ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.receipt_item_layout, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the data on a TextView.
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ImageView ivReceiptImage = (ImageView) view.findViewById(R.id.receiptImage);
        TextView tvStoreName = (TextView) view.findViewById(R.id.storeNameTextView);
        TextView tvTotal = (TextView) view.findViewById(R.id.totalTextView);
        TextView tvDateTime = (TextView) view.findViewById(R.id.dateTextView);
        TextView tvId = (TextView) view.findViewById(R.id.idTextView);
        TextView tvTags = (TextView) view.findViewById(R.id.tagsTextView);
        Button btIsSync = (Button) view.findViewById(R.id.isSync);
        String imagePath;

        // Read value with cursor and set value to widget
        // Id is set to invisible text, to make easy to read data from database
        curConext = context;

        imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECEIPT_URL));
        if (imagePath != null) {
            file = new java.io.File(imagePath);
            GlideUtil.loadImage(file, ivReceiptImage);
        }

        tvStoreName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STORE_NAME)));
        tvTotal.setText("$ "+String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(DBHelper.RECEIPT_TOTAL))));
        tvDateTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECEIPT_DATE)));
        tvId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.RECEIPT_ID))));
        tvTags.setText("TAG NAME");
        //todo should get from DB
        btIsSync.setBackgroundColor(getColorFromValue(1));
    }

    public Integer getColorFromValue(Integer value) {
        Integer retColor;

        switch (value) {
            case 1:
                retColor = android.R.color.holo_green_light;
                break;

            default:
                retColor = android.R.color.holo_blue_light;
                break;

        }
        return (curConext.getResources().getColor(retColor));
    }

}

