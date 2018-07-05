package com.zzh.mvvm.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ContactsHelper {
    private static String uri_rawcontacts = "content://com.android.contacts/raw_contacts";
    private static String uri_contacts_phones = "content://com.android.contacts/data/phones";
    private static String uri_contacts_emails = "content://com.android.contacts/data/emails";
    private String uri_contacts_data = "content://com.android.contacts/data";

    // 查询联系人的信息
    public static List<Map<String, Object>> selectContactsInfo(
            ContentResolver resolver) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor contactsCursor = resolver.query(Uri.parse(uri_rawcontacts),
                new String[]{"_id", "display_name"}, null, null, null);
        while (contactsCursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            int contactsId = contactsCursor.getInt(contactsCursor
                    .getColumnIndex("_id"));
            String displayName = contactsCursor.getString(contactsCursor
                    .getColumnIndex("display_name"));
            map.put("_id", contactsId);
            map.put("display_name", displayName);

            // 根据联系人的id去data表获取电话号码的信息
            Cursor phoneCursor = resolver.query(Uri.parse(uri_contacts_phones),
                    new String[]{"raw_contact_id", "data1"},
                    "raw_contact_id=?", new String[]{contactsId + ""}, null);

            StringBuilder sb = new StringBuilder();
            while (phoneCursor.moveToNext()) {
                sb.append(phoneCursor.getString(1));
                sb.append("|");
            }
            map.put("phones", sb.toString());
            if (phoneCursor != null) {
                phoneCursor.close();
            }

            // 根据联系人的id去data表获取email的信息
            Cursor emailCursor = resolver.query(Uri.parse(uri_contacts_emails),
                    new String[]{"raw_contact_id", "data1"},
                    "raw_contact_id=?", new String[]{contactsId + ""}, null);

            StringBuilder sb2 = new StringBuilder();
            while (emailCursor.moveToNext()) {
                sb2.append(emailCursor.getString(1));
                sb2.append("|");
            }
            map.put("emails", sb2.toString());
            if (emailCursor != null) {
                emailCursor.close();
            }

            list.add(map);
        }
        if (contactsCursor != null) {
            contactsCursor.close();
        }
        return list;
    }

    /**
     * 修改联系人的姓名
     */
    public boolean updateContactsName(ContentResolver resolver,
                                      Map<String, Object> map, String id) {
        ContentValues values = new ContentValues();
        // 更改raw_contacts表中的姓名
        values.put("display_name", map.get("display_name").toString());
        values.put("display_name_alt", map.get("display_name").toString());
        values.put("sort_key", map.get("display_name").toString());
        values.put("sort_key_alt", map.get("display_name").toString());
        int result1 = resolver.update(Uri.parse(uri_rawcontacts), values,
                "_id=?", new String[]{id});
        // 更改data表中的姓名
        values.clear();
        values.put("data1", map.get("display_name").toString());
        values.put("data2", map.get("display_name").toString());
        int result2 = resolver.update(Uri.parse(uri_contacts_data), values,
                "raw_contact_id=? and mimetype_id=?", new String[]{id, "7"});
        // 更改data表中的phone
        values.clear();
        values.put("data1", map.get("phone").toString());
        values.put("data2", 2);
        int result3 = resolver.update(Uri.parse(uri_contacts_data), values,
                "raw_contact_id=? and mimetype_id=?", new String[]{id, "5"});
        // 更改data表中的phone
        values.clear();
        values.put("data1", map.get("email").toString());
        values.put("data2", 1);
        int result4 = resolver.update(Uri.parse(uri_contacts_data), values,
                "raw_contact_id=? and mimetype_id=?", new String[]{id, "1"});

        // 更改data表中的email
        if (result1 > 0 && result2 > 0 && result3 > 0 && result4 > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据联系人姓名删除联系人信息
     */
    public boolean deleteContacts(ContentResolver resolver, String displayName) {
        int data = resolver.delete(Uri.parse(uri_rawcontacts),
                "display_name=?", new String[]{displayName});
        if (data > 0) {
            return true;
        }
        return false;
    }

    /**
     * 新增数据
     */
    public void insertContact(ContentResolver resolver, Map<String, Object> map) {
        ContentValues values = new ContentValues();
        // 往raw_contacts表中插入一条空数据，目的是获取联系人的id
        Uri newUri = resolver.insert(Uri.parse(uri_rawcontacts), values);
        long id = ContentUris.parseId(newUri);

        // 往data表中插入联系人姓名的数据
        values.put("raw_contact_id", id);
        // values.put("mimetype_id", 7);//必须要插入mimetype字段，而不可以直接插入mimetype_id.
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data1", map.get("display_name").toString());
        values.put("data2", map.get("display_name").toString());
        resolver.insert(Uri.parse(uri_contacts_data), values);

        // 往data表中插入联系人的电话信息
        values.clear();
        values.put("raw_contact_id", id);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data1", map.get("phone").toString());
        values.put("data2", 2);
        resolver.insert(Uri.parse(uri_contacts_data), values);

        // 往data表中插入联系人的email
        values.clear();
        values.put("raw_contact_id", id);
        values.put("mimetype", "vnd.android.cursor.item/email_v2");
        values.put("data1", map.get("email").toString());
        values.put("data2", 1);
        resolver.insert(Uri.parse(uri_contacts_data), values);
    }
}
