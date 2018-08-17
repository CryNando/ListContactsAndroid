package com.example.luisfilho.listnativecontacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.widget.Toast

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager
    private var viewAdapter = MyAdapter(ArrayList<ContactInformation>())
    var contact = ArrayList<ContactInformation>()
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
            Log.e("result" , "-> ok ")
            loadContacts()
        }
}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchItem = menu!!.findItem(R.id.search_menu)
        var searchView = searchItem!!.getActionView() as SearchView
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var userInput : String = newText!!.toLowerCase()
        var newList : ArrayList<ContactInformation> = ArrayList<ContactInformation>()

        for (contacts : ContactInformation in contact){
           if(contacts.name.toLowerCase().contains(userInput)){
               newList.add(contacts)
           }
        }

        if (newText.isEmpty()){
            newList = contact
        }


        viewAdapter.updateRecycle(newList)
        return true

    }

    private fun loadContacts(){
        var getContats : getContactsData = getContactsData(this)
        val myDataset = getContats.execute().get()
        contact = myDataset

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



