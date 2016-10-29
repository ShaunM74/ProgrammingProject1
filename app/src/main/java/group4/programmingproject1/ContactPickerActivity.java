package group4.programmingproject1;

        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.ContentResolver;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.Toast;

        import group4.programmingproject1.SelectUserAdapter;
        import group4.programmingproject1.SelectUser;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;


public class ContactPickerActivity extends AppCompatActivity {

    // ArrayList
    ArrayList<SelectUser> selectUsers;
    List<SelectUser> temp;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;

    // Pop up
    ContentResolver resolver;
    SearchView search;
    SelectUserAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        context = this;
        selectUsers = new ArrayList<SelectUser>();
        resolver = this.getContentResolver();
        listView = (ListView) findViewById(R.id.contacts_list);

        phones = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.ADDRESS +" like '%@gmail.com'", null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


        LoadContact loadContact = new LoadContact();
        loadContact.execute();

        search = (SearchView) findViewById(R.id.searchView);

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                adapter.filter(newText);
                return false;
            }
        });

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
            ContentResolver cr = getContentResolver();
            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {


                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                    Log.d("Debug",id);
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.d("Debug",name);
                    String phoneNumber="";
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            phoneNumber = pCur.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    }
                    pCur.close();
                    //String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));
                    Log.d("Debug",phoneNumber);
                    String EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                    String contactID= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID));

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

                    SelectUser selectUser = new SelectUser();
                    selectUser.setThumbURI(image_thumb);
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectUser.setEmail(EmailAddr);
                    selectUser.setID(contactID);
                    //selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);
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
            adapter = new SelectUserAdapter(selectUsers, context);
            listView.setAdapter(adapter);

            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    SelectUser data = selectUsers.get(i);
                    Intent returnData = new Intent();
                    returnData.putExtra("contactID",data.getID());
                    returnData.putExtra("name",data.getName());
                    returnData.putExtra("email",data.getEmail());
                    returnData.putExtra("phone",data.getPhone());
                    returnData.putExtra("thumbURI",data.getThumbURI());
                    setResult(RESULT_OK,returnData);
                    finish();
                }
            });

            listView.setFastScrollEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        phones.close();
    }
}