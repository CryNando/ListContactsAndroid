package com.example.luisfilho.listnativecontacts

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.provider.ContactsContract
import java.io.BufferedInputStream
import java.io.InputStream

/**
 * Created by luis.filho on 17/08/2018.
 */
class getContactsData constructor(val contextA : Context) : AsyncTask<Void, Void, ArrayList<ContactInformation>>() {

    var contact : ArrayList<ContactInformation> = ArrayList()

    override fun doInBackground(vararg params: Void?): ArrayList<ContactInformation> {

        val cursor = contextA.contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        val defaultImg: Bitmap = BitmapFactory.decodeResource(contextA.resources, R.drawable.avatar)

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val imgUser = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
            val hasPhone = cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
            var phone = ""
            if (hasPhone != null) {
                phone = getPhone(id)
            }

            if (imgUser == null) {
                contact.add(ContactInformation(id, name, defaultImg,phone))
            } else {
                val imgUser : Bitmap = BitmapFactory.decodeStream(getPhoto(id))
                contact.add(ContactInformation(id, name, imgUser,phone))
            }
        }
        cursor.close()
        return contact
    }

    private fun getPhone(id : String) : String{
        val cursorPhone : Cursor = contextA.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null)
        cursorPhone.moveToFirst()
        val number = cursorPhone.getString(cursorPhone.getColumnIndex("data1"))
        cursorPhone.close()
        return number
    }

    private fun getPhoto(id : String) : BufferedInputStream {
        val photoContact : Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
        val photoStream : InputStream = ContactsContract.Contacts.openContactPhotoInputStream(contextA.contentResolver, photoContact)
        return BufferedInputStream(photoStream)
    }
}