package com.example.luisfilho.listnativecontacts

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Filter
import android.widget.SearchView
import android.widget.Toast
import java.io.BufferedInputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var viewAdapter = MyAdapter(ArrayList<ContactInformation>())
    val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        }else {
            loadContacts()
        }
}

    private fun getContacts() : ArrayList<ContactInformation> {
        val resolver: ContentResolver = contentResolver
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        val contact = ArrayList<ContactInformation>()
        val defaultImg: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar)

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val imgUser = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
            val hasPhone = cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
            var phone = ""
            if (hasPhone != null) {
                phone = getPhoneNumber(id)
            }

            if (imgUser == null) {
                contact.add(ContactInformation(id, name, defaultImg,phone))
            } else {
                val imgUser : Bitmap = BitmapFactory.decodeStream(getPhoto(id))
                contact.add(ContactInformation(id, name, imgUser,phone))
            }
        }
        return contact
    }

    private fun getPhoto(id : String) : BufferedInputStream{
        val photoContact : Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
        val photoStream : InputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, photoContact)
        return BufferedInputStream(photoStream)

    }

    private fun getPhoneNumber(id : String) : String{
        val cursorPhone : Cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null)
        cursorPhone.moveToFirst()
        val number = cursorPhone.getString(cursorPhone.getColumnIndex("data1"))
        return number
    }

    private fun loadContacts(){
        val myDataset = getContacts()

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset)

        recyclerView = findViewById<RecyclerView>(R.id.my_recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        recyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadContacts()
                } else {
                    Toast.makeText(this,"É necessário Permissões de Contato para continuar",Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }




}



