package com.example.contactapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var cols= listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone._ID
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,Array(1){
                Manifest.permission.READ_CONTACTS
            },111)
        }else{
            readContact()
        }
    }
    override fun onRequestPermissionsResult(requestCode:Int,permissions:Array<out String>,grantResult:IntArray){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult)
        if (requestCode==111 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
            readContact()
        }
    }

    private fun readContact() {
        var from= listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        ).toTypedArray()

        var to = intArrayOf(android.R.id.text1)

        var rs=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        cols,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        val adapter=SimpleCursorAdapter(this,android.R.layout.activity_list_item,
        rs, from,to,0)

        listview1.adapter=adapter
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var rs=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols,"${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
                    , Array(1){"%$p0%"},
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                adapter.changeCursor(rs)
                return false
            }

        })

    }


}