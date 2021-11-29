package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.IOUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1.remove item from list
                listOfTasks.removeAt(position)
                //2.notify data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Caren", "MEssage")
//        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up the button and the input field so the user can add stuff
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //get ref to button
        //set onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            //1.grab text user inputted into task field
            val userInputtedTask = inputTextField.text.toString()

            //2.add string to list of tasks
            listOfTasks.add(userInputtedTask)

            //notify adapter that data is updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3.reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //save the data that the user has inputted
    //create a method to get the data file we need

    //create a method to get the file we need
    fun getDataFile() : File {
        //every line will represent a task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //load items by reading every line in file
    fun loadItems(){
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //save items by writing them into our data file
    fun saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}