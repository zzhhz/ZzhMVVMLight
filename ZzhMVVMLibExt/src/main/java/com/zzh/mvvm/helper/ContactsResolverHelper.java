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

public class ContactsResolverHelper {

    /**
     * / 以下代码是插入新联系人的子过程。其中第三个参数：List集合表示要插入到通讯录中的新数据(该集合中有三个值，分别是：姓名、电话、email)。
     */
    public void insertContacts(ContentResolver resolver, ContentValues values,
                               List<String> list) {
        String uri_contacts = "content://com.android.contacts/data";
        Uri uri = Uri.parse(uri_contacts);
        // 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
        Uri rawContactUri = resolver.insert(Uri.parse(uri_contacts), values);
        long contact_id = ContentUris.parseId(rawContactUri);

        // 往data表中插入一条用户的名称信息
        values.put("raw_contact_id", contact_id);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data1", list.get(0));
        values.put("data2", list.get(0));
        resolver.insert(uri, values);

        // 往data表中插入电话信息
        values.clear();
        values.put("raw_contact_id", contact_id);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data1", list.get(1));
        values.put("data2", 2);// 2，Phone.TYPE_MOBILE ，表示手机号码
        resolver.insert(uri, values);

        // 往data表中插入Email信息
        values.clear();
        values.put("raw_contact_id", contact_id);
        values.put("mimetype", "vnd.android.cursor.item/email_v2");
        values.put("data1", list.get(2));
        values.put("data2", 2);// 2，Email.TYPE_WORK ， 表示工作用Email号码
        resolver.insert(uri, values);
    }

    // 以下代码是删除联系人信息的方法。
    public static int deleteContacts(ContentResolver resolver, String where,
                                     String[] whereArgs) {
        String uri_contacts = "content://com.android.contacts/raw_contacts";
        return resolver.delete(Uri.parse(uri_contacts), where, whereArgs);
    }

    // 以下代码是查询联系人信息的方法。
    public static Cursor selectContactsName(ContentResolver resolver,
                                            String[] projection, String where, String[] whereArgs,
                                            String sortOrder) {
        String uri_contacts = "content://com.android.contacts/raw_contacts";
        return resolver.query(Uri.parse(uri_contacts), projection, where,
                whereArgs, sortOrder);
    }

    public static List<Map<String, Object>> selectContactsMsg(
            ContentResolver resolver, ContentValues values) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String uri_contacts = "content://com.android.contacts/raw_contacts";
        String uri_contacts_phones = "content://com.android.contacts/data/phones";
        String uri_contacts_emails = "content://com.android.contacts/data/emails";
        // 从raw_contacts表中或许联系人的id和联系人的姓名。
        Cursor cursor_contacts = resolver.query(Uri.parse(uri_contacts),
                new String[]{"_id", "display_name"}, null, null, null);

        // 遍历所有的联系人的信息
        while (cursor_contacts.moveToNext()) {
            int contacts_id = cursor_contacts.getInt(cursor_contacts
                    .getColumnIndex("_id"));
            String display_name = cursor_contacts.getString(cursor_contacts
                    .getColumnIndex("display_name"));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", contacts_id);
            map.put("display_name", display_name);

            // 以下开始获取电话号码
            // 根据每个联系人的id再去data表中查找相应的电话号码。
            Cursor cursor_phones = resolver.query(
                    Uri.parse(uri_contacts_phones), new String[]{
                            "raw_contact_id", "data1"}, "raw_contact_id=?",
                    new String[]{contacts_id + ""}, null);

            // 因为电话号码可能是多个，所以需要再遍历，组合在一起形成一个电话号码的字符串，放到StringBuilder中
            StringBuilder sb = new StringBuilder();
            while (cursor_phones.moveToNext()) {
                sb.append(cursor_phones.getString(1));
                sb.append(" | ");
            }
            // 将生成的电话号码放到map集合中
            map.put("phones", sb.toString());

            // 以下开始或许Email信息
            Cursor cursor_emails = resolver.query(
                    Uri.parse(uri_contacts_emails), new String[]{
                            "raw_contact_id", "data1"}, "raw_contact_id=?",
                    new String[]{contacts_id + ""}, null);
            StringBuilder sb2 = new StringBuilder();
            while (cursor_emails.moveToNext()) {
                sb2.append(cursor_emails.getString(1));
                sb2.append(" | ");
            }
            map.put("emails", sb2.toString());

            // 将包含有id、联系人姓名、手机号码、emails的map放到list集合中
            list.add(map);
        }
        return list;
    }

    // 以下代码是更新联系人姓名的方法。
    public static int updateContactsName(ContentValues values,
                                         ContentResolver resolver, String oldName, String newName) {
        String uri_contacts = "content://com.android.contacts/raw_contacts";
        String uri_contacts_data = "content://com.android.contacts/data";
        // 先更新raw_contacts表中的联系人姓名，共有四列需要更新
        values.put("display_name", newName);
        values.put("display_name_alt", newName);
        values.put("sort_key", newName);
        values.put("sort_key_alt", newName);
        int count1 = resolver.update(Uri.parse(uri_contacts), values,
                "display_name=?", new String[]{oldName});
        // 再更新data表中联系人的姓名，共有两列
        values.clear();
        values.put("data1", newName);
        values.put("data2", newName);
        int count2 = resolver.update(Uri.parse(uri_contacts_data), values,
                "data1=? and mimetype_id=?", new String[]{oldName, "7"});
        // 返回2，则说明六项都被更新。如果只返回1，则说明没有修改完全
        return count1 + count2;
    }
}
