package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.model.SelectContact;
import com.tied.android.tiedapp.ui.adapters.SelectContactAdapter;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Emmanuel on 6/15/2016.
 */
public class InviteContactActivity extends Activity implements View.OnClickListener{

    public static final String TAG = SignUpActivity.class
            .getSimpleName();

    private ArrayList<SelectContact> contacts;
    private ListView listView;
    private Cursor phones, email;

    // Pop up
    private ContentResolver resolver;
    private EditText search;
    private SelectContactAdapter adapter;

    private LinearLayout invite_all;
    private ImageView icon_check;
    private TextView invite;
    private boolean check_all = false;

    private ArrayList<Map.Entry<String, String>> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_invite_employee);

        contacts = new ArrayList<SelectContact>();
        resolver = this.getContentResolver();
        listView = (ListView) findViewById(R.id.contacts_list);

        invite = (TextView) findViewById(R.id.invite);
        invite_all = (LinearLayout) findViewById(R.id.invite_all);
        icon_check = (ImageView) findViewById(R.id.icon_check);

        invite_all.setOnClickListener(this);
        invite.setOnClickListener(this);

        phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        LoadContact loadContact = new LoadContact();
        loadContact.execute();

        selected = new ArrayList<Map.Entry<String, String>>();

        search = (EditText) findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String searchData = search.getText().toString().trim().toLowerCase();
                adapter.filter(searchData);
            }
        });

        // Select item on listclick
//        listView.setOnItemClickListener(new ContactListAdapter());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invite_all:
                check_all = !check_all;
                if(check_all){
                    icon_check.setBackgroundResource(R.mipmap.circle_check);
                }else{
                    icon_check.setBackgroundResource(R.mipmap.circle_uncheck);
                }
                for(SelectContact contact: contacts){
                    contact.setCheckStatus(check_all);
                }
                Log.d(TAG, "check_all "+check_all);
                adapter.notifyDataSetChanged();
                break;
            case R.id.invite:
                for(SelectContact contact: contacts){
                    if (contact.getCheckStatus()){
                        Map.Entry entry = new AbstractMap.SimpleEntry<>(contact.getEmail(), contact.getPhone());
                        selected.add(entry);
                    }
                }
                Log.d(TAG, selected.toString());
                break;
        }
    }

    private class ContactListAdapter implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("search", "here---------------- listener");

            SelectContact data = contacts.get(position);
            Map.Entry entry = new AbstractMap.SimpleEntry<>(data.getEmail(), data.getPhone());
            selected.add(entry);
            Log.d("SelectContact", data.toString());
        }
    }

    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(InviteContactActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (image_thumb != null) {
                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    name = (name.length() < 15) ? name : name.substring(0, 15) + "...";
                    EmailAddr = (EmailAddr.length() < 15) ? EmailAddr : EmailAddr.substring(0, 15) + "...";

                    SelectContact contact = new SelectContact();
                    contact.setThumb(bit_thumb);
                    contact.setName(name);
                    contact.setPhone(phoneNumber);
//                    SelectContact.setEmail(id);
                    contact.setEmail(EmailAddr);
                    contact.setCheckStatus(false);
                    contacts.add(contact);
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectContactAdapter(contacts, InviteContactActivity.this);
            listView.setAdapter(adapter);
            listView.setFastScrollEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        phones.close();
    }
}
