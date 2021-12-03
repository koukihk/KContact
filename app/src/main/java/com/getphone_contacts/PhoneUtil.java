package com.getphone_contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.getphone_contacts.a_z.BeanPhoneDto;

import java.util.ArrayList;
import java.util.List;


public class PhoneUtil {

    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public PhoneUtil(Context context){
        this.context = context;
    }

    //获取所有联系人
    public List<BeanPhoneDto> getPhone(){
        List<BeanPhoneDto> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String phone = cursor.getString(cursor.getColumnIndex(NUM));
//            phone = phone.replace("-", "");
            phone = phone.replace(" ", "");
            BeanPhoneDto phoneDto = new BeanPhoneDto(name,phone);
            phoneDtos.add(phoneDto);
        }
        return phoneDtos;
    }
    /**
     * 添加联系人信息
     *
     */
    public static void insertConstacts(Context context, String name,String phone1) {
        try {
            //用户是否存在，存在的话，就追加号码，不存在新增联系人
            long rawContactId;
            boolean flag = isThePhoneExist(context, name);
            ContentValues values = new ContentValues();
            if(flag == false){
                rawContactId = 0;
            }else {
                rawContactId = 1;
            }
            if (rawContactId == 0) {
                //插入raw_contacts表，并获取_id属性
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = context.getContentResolver();
                rawContactId = ContentUris.parseId(resolver.insert(uri, values));

                //插入data表
                uri = Uri.parse("content://com.android.contacts/data");

                //add Name
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/name");
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
                resolver.insert(uri, values);
                values.clear();

                //写入头像
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_poraital);
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                out.flush();
//                out.close();
//                values.clear();
//                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
//                values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, out.toByteArray());
//                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

            }

            //写入手机号码
            if(phone1.isEmpty() == false) {
                values.clear();
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone1);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                //插入data表
                Uri uri = Uri.parse("content://com.android.contacts/data");
                context.getContentResolver().insert(uri, values);
                Toast.makeText(context, "添加完毕", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "添加失败，请检查姓名或号码字段是否合法", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            //Log.i(TAG, "insertConstacts:  e = " + e.getMessage());
            Toast.makeText(context, "添加失败，请检查应用权限是否被系统禁用", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    /**
     * 判断某个手机号是否存在
     */
    public static boolean isThePhoneExist(Context context, String phoneNum) {
        //uri=  content://com.android.contacts/data/phones/filter/#
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phoneNum);
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME},
                    null, null, null); //从raw_contact表中返回display_name
            if (cursor.moveToFirst()) {
                //Log.i(TAG, "name=" + cursor.getString(0) + " , phoneNum = " + phoneNum);
                cursor.close();
                return true;
            }
        } catch (Exception e) {
            //Log.i(TAG, "163 e =" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }
    public static void contactDelete(Context context, String name){
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID},"display_name=?", new String[]{name}, null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id+""});
        }
    }

    /**
     * 判断是否为汉字
     *
     * @param string
     * @return
     */

    public static boolean isChinese(String string) {
        int n;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

}
