package com.eazytec.bpm.app.contact.usercontact.localcontact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.UserLocalContactAdapter;
import com.eazytec.bpm.app.contact.data.UserContactData;
import com.eazytec.bpm.app.contact.utils.CnToSpell;
import com.eazytec.bpm.appstub.view.listview.LetterSideBar;
import com.eazytec.bpm.lib.common.activity.CommonActivity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 本地通讯录
 * @author Administrator
 * @version Id: LocalContactActivity, v 0.1 2017/7/5 16:34 Administrator Exp $$
 */
public class LocalContactActivity extends CommonActivity{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;
    private EditText searcheditText;
    private ListView listView;
    private LetterSideBar sideBar;
    private ArrayList<UserContactData> list;

    private UserLocalContactAdapter adapter;

    private MaterialDialog progressDialog;


    /** 获取库Phon表字段 **/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    /** 联系人显示名称 **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /** 电话号码 **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /** 头像ID **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /** 联系人的ID **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /** 联系人名称 **/
    private ArrayList<String> mContactsName = new ArrayList<String>();

    /** 联系人号码 **/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    /** 联系人头像 **/
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_contact);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("手机通讯录");

        listView = (ListView) findViewById(R.id.local_contact_listview);
        searcheditText = (EditText) findViewById(R.id.local_input_edittext);
        sideBar = (LetterSideBar) findViewById(R.id.local_contact_sidebar);

        initData();

        sideBar.setOnStrSelectCallBack(new LetterSideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String s) {
                for (int i = 0; i < list.size(); i++) {
                    if (s.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });




            searcheditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String keyword = searcheditText.getText().toString();
                    adapter.getFilter().filter(keyword);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalContactActivity.this.finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("name", list.get(position).getName());
                bundle.putString("phoneNum", list.get(position).getPhoneNum());

                startActivity(LocalContactActivity.this,LocalContactDetailActivity.class,bundle);
            }
        });

    }

    private void initData(){
        list = new ArrayList<>();
        /** 得到手机通讯录联系人信息 **/
        getPhoneContacts();
        getSIMContacts();
        Comparator comp = new Mycomparator();
        Collections.sort(list, comp);
        adapter = new UserLocalContactAdapter(getContext(),list);
        listView.setAdapter(adapter);
    }

    // 通讯社按中文拼音排序
    public class Mycomparator implements Comparator {
        public int compare(Object o1, Object o2) {
            UserContactData c1 = (UserContactData) o1;
            UserContactData c2 = (UserContactData) o2;
            Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);
            return cmp.compare(c1.getFirstLetter(), c2.getFirstLetter());
        }
    }

    /** 得到手机通讯录联系人信息 **/
    private void getPhoneContacts() {
        showProgressDialog();


        ContentResolver resolver = getContext().getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                UserContactData contactData = new UserContactData();

                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                String contactSort = CnToSpell.getPinYin(contactName)
                        .toUpperCase().substring(0, 1);
                if (contactSort.equals("0") || contactSort.equals("1")
                        || contactSort.equals("2") || contactSort.equals("3")
                        || contactSort.equals("4") || contactSort.equals("5")
                        || contactSort.equals("6") || contactSort.equals("7")
                        || contactSort.equals("8") || contactSort.equals("9")) {
                    contactSort="#";
                }

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                // 得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                /**
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts
                            .openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
                    contactPhoto = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_contact_photo);
                }
                 **/
                if(!contactSort.equals("#")) {
                    contactData.setName(contactName);
                    contactData.setPhoneNum(phoneNumber);
                    contactData.setFirstLetter(contactSort);
                    list.add(contactData);
                }
            }
            phoneCursor.close();
            dismissProgressDialog();

        }
    }

    /** 得到手机SIM卡联系人人信息 **/
    private void getSIMContacts() {
        ContentResolver resolver = getContext().getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                UserContactData contactData = new UserContactData();
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);
                String contactSort = CnToSpell.getPinYin(contactName)
                        .toUpperCase().substring(0, 1);
                if (contactSort.equals("0") || contactSort.equals("1")
                        || contactSort.equals("2") || contactSort.equals("3")
                        || contactSort.equals("4") || contactSort.equals("5")
                        || contactSort.equals("6") || contactSort.equals("7")
                        || contactSort.equals("8") || contactSort.equals("9")) {
                    contactSort="#";
                }
                /**
                Bitmap contactPhoto = null;
                // Sim卡中没有联系人头像,使用默认的
                contactPhoto = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_contact_photo);

                 **/
                    if(!contactSort.equals("#")) {
                        contactData.setName(contactName);
                        contactData.setPhoneNum(phoneNumber);
                        contactData.setFirstLetter(contactSort);
                        list.add(contactData);
                    }
            }

            phoneCursor.close();
        }
    }

    public void showProgressDialog() {
        this.progressDialog = new MaterialDialog.Builder(this)
                .content("正在加载通讯录……")
                .cancelable(false)
                .progress(true, 0)
                .show();

    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

}
