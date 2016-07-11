package com.conestogac.receipt_keeper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.conestogac.receipt_keeper.MultiSpinnerSearch.MultiSpinnerSearchListener;
import com.conestogac.receipt_keeper.helpers.KeyPairBoolData;
import com.conestogac.receipt_keeper.models.Receipt;
import com.conestogac.receipt_keeper.models.Tag;
import com.conestogac.receipt_keeper.uploader.CustomerRepository;
import com.strongloop.android.loopback.RestAdapter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class AddReceiptActivity extends Activity {

    // [Layout views]
    private EditText storeNamEditText;
    private EditText totalEditText;
    private EditText dateEditText;
    private EditText commentEditText;
    private EditText paymentEditText;
    private MultiSpinnerSearch categorySearchMultiSpinner;
    private MultiSpinnerSearch tagSearchSpinner;
    private Button saveReceiptButton;


    private SQLController dbController;
    private Calendar dateAndTime = Calendar.getInstance();
    private LinkedList<Tag> tags = new LinkedList<>();
    private ReceiptKeeperApplication app;
    private static final String LOG_NAME = "AddReceiptActivity";

    // [ from TestUplaodActivity]
    private CustomerRepository userRepo;
    private RestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

        // from TestUplaodActivity

        app = (ReceiptKeeperApplication) this.getApplication();
        adapter = app.getLoopBackAdapter();
        userRepo = adapter.createRepository(CustomerRepository.class);
        dbController = new SQLController(this);


        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.tags));
        TreeMap<String, Boolean> items = new TreeMap<>();
        for (String item : list) {
            items.put(item, Boolean.FALSE);
        }


        // [ Setting IDs to Views ]
        totalEditText = (EditText) findViewById(R.id.totalEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        storeNamEditText = (EditText) findViewById(R.id.storeNamEditText);
        commentEditText = (EditText) findViewById(R.id.commentEditText);
        paymentEditText = (EditText) findViewById(R.id.paymentEditText);
        saveReceiptButton = (Button) findViewById(R.id.saveReceiptButton);
        categorySearchMultiSpinner = (MultiSpinnerSearch) findViewById(R.id.categorySearchMultiSpinner);
        tagSearchSpinner = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinner);


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(dateEditText.getContext(), d, dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        // [ onClick will get data from views and insert them into database]
        saveReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Receipt receipt = new Receipt();
                //Date date = convertStringToDate(dateString);
                saveReceiptDataInDB(receipt, tags);*/

                //==========================
                long _id;

                dbController.open();
                Receipt receipt = new Receipt();
                final String image = "/storage/emulated/0/ReceiptKeeperFolder/2016_07_05_20_00_04.Receipt.bmp";

                Log.d(LOG_NAME, "on saving receipt");
                if (app.getCurrentUser().getId() == null) {
                    receipt.setCustomerId(null);
                } else {
                    receipt.setCustomerId(app.getCurrentUser().getId().toString());
                }
                receipt.setStoreId(dbController.insertStoreByName(storeNamEditText.getText().toString()));
                receipt.setTotal(Float.parseFloat(totalEditText.getText().toString()));
                receipt.setDate(dateEditText.getText().toString());
                receipt.setComment(commentEditText.getText().toString());
                receipt.setPaymentMethod(paymentEditText.getText().toString());
                receipt.setCategoryId(1);
                receipt.setUrl(image);

                //tags = tagSearchSpinner.getAllTags();


                dbController.insertReceipt(receipt, null);
                dbController.close();

                Intent goToHomePage = new Intent(AddReceiptActivity.this, Home2Activity.class);
                startActivity(goToHomePage);
            }

        });


        /**
         * Search MultiSelection Spinner (With Search/Filter Functionality)
         *
         *  Using MultiSpinnerSearch class
         */
        final LinkedList<KeyPairBoolData> listArray = new LinkedList<>();

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray.add(h);
        }

        /*LinkedList<Tag> tags = tagSearchSpinner.saveAllTags();
        for (Tag tag : tags)
            Log.d("tag.getTagName()", tag.getTagName());*/
        /***
         * -1 is no by default selection
         * 0 to length will select corresponding values
         */
        tagSearchSpinner.setItems(listArray, "Tag search", -1, new MultiSpinnerSearchListener() {

            @Override
            public void onItemsSelected(LinkedList<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());

                    }
                }

            }
        });
    }

    // []
    private void saveReceiptDataInDB(Receipt receipt, LinkedList<Tag> tag) {
        dbController.open();
        dbController.insertReceipt(receipt, tag);
        dbController.close();
    }

    // [DatePickerDialog population]
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    // [Show date on TextView]
    private void updateDate() {
        dateEditText.setText(DateUtils
                .formatDateTime(this,
                        dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    // [Convert string we got from the EditText to Date ]
    /*private Date convertStringToDate(String dateString) {
        Date date = new Date();
        Log.d("dateString", dateString + "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateString);
            Log.d("date", date + "");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }*/
}
