package com.stepa0751.mynotebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stepa0751.mynotebook.databinding.ActivityMainBinding
import com.stepa0751.mynotebook.db.MyAdapter
import com.stepa0751.mynotebook.db.MyDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")

    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun init() {
        binding.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.rcView)
        binding.rcView.adapter = myAdapter
    }

    private fun initSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MyLog", "New Query Text: $query")
//                По слову целиком и кнопке поиска на клавиатуре уже
//                не нужно, потому что и так уже находит все по сочетанию букв!
//                val list = myDbManager.readDbData(query!!)
//                myAdapter.updateAdapter(list)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MyLog", "New Text: $newText")
//                val list = myDbManager.readDbData(newText!!)
//                myAdapter.updateAdapter(list)
                fillAdapter(newText!!)
                return true
            }
        })
    }

    private fun fillAdapter(text: String) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDbData(text)
            myAdapter.updateAdapter(list)
            if (list.size > 0) {
                binding.tvNoElements.visibility = View.GONE
            } else {
                binding.tvNoElements.visibility = View.VISIBLE
            }
        }

    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }

        })
    }
}