package com.stepa0751.mynotebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.stepa0751.mynotebook.databinding.ActivityEditBinding
import com.stepa0751.mynotebook.databinding.ActivityMainBinding
import com.stepa0751.mynotebook.db.MyDbManager
import com.stepa0751.mynotebook.db.MyIntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    var id = 0
    var isEditState = false
    val myDbManager = MyDbManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    fun onClickSave(view: View) {
        val myTitle = binding.edTitle.text.toString()
        val myDescription = binding.edDescription.text.toString()

        if (myTitle != "" && myDescription != "") {

                if (isEditState) {
                    CoroutineScope(Dispatchers.Main).launch {
                    myDbManager.updateItem(myTitle, myDescription, id, getCurrentTime())}
                    val message = Toast.makeText(this, getString(R.string.updated), Toast.LENGTH_SHORT)
                    message.setGravity(Gravity.CENTER, 0, 0)
                    message.show()
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                    myDbManager.insertToDb(myTitle, myDescription, getCurrentTime())}
                    val message = Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT)
                    message.setGravity(Gravity.CENTER, 0, 0)
                    message.show()
                }
                /* Здесь мы закрываем эту активити: */
                finish()

        } else {
            val message = Toast.makeText(this,getString(R.string.no_title_or_description), Toast.LENGTH_SHORT)
            message.setGravity(Gravity.CENTER, 0, 0)
            message.show()
        }
    }

    fun getMyIntents() {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                isEditState = true
                binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                binding.edDescription.setText(i.getStringExtra(MyIntentConstants.I_DESCRIPTION_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
            }
        }
    }

    fun onClickHome(view: View) {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getCurrentTime(): String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}