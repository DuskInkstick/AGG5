package com.inkstick.agg5.screens.main

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.inkstick.agg5.R
import com.inkstick.agg5.data.AppDb
import com.inkstick.agg5.model.Item
import com.inkstick.agg5.screens.report.ReportActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File

class MainFragment : Fragment() {

    private lateinit var arrayAdapter: ArrayAdapter<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDb.getDatabase(requireContext())
        val dataList = db.itemDao().getAll()

        arrayAdapter = ArrayAdapter<Item>(requireContext(), android.R.layout.simple_list_item_1)

        view.apply {
            findViewById<Button>(R.id.b_add)?.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mainFragment_to_editFragment,
                    Bundle().apply {
                        putInt("itemId", -1)
                    })
            }

            findViewById<Button>(R.id.b_report)?.setOnClickListener {
                val i = Intent(activity, ReportActivity::class.java)
                startActivity(i)
            }

            findViewById<Button>(R.id.b_export).setOnClickListener {
                writeJson(dataList)
            }

            findViewById<Button>(R.id.b_import).setOnClickListener {
                readJson(db)
                arrayAdapter.clear()
                arrayAdapter.addAll(db.itemDao().getAll())
            }

            findViewById<ListView>(R.id.list_items)?.apply {
                adapter = arrayAdapter

                setOnItemClickListener { adapterView, view, position, id ->
                    val item = dataList[position]

                    findNavController().navigate(
                        R.id.action_mainFragment_to_editFragment,
                        Bundle().apply {
                            putInt("itemId", item.id!!)
                        })
                }
            }
        }
        arrayAdapter.addAll(db.itemDao().getAll())
    }

    private fun readJson(db: AppDb) {
        // Для чтения из асетов (src/main/res/assets)
       /* val strJson = requireContext().assets.open("data.json")
            .bufferedReader()
            .use {
                it.readText()
            }*/

        // Для чтения из данных приложения (/data/data/com.../files)
        val strJson = requireContext().openFileInput("data.json")
            .bufferedReader()
            .use {
                it.readText()
            }

        val items = Json.decodeFromString<List<Item>>(strJson)

        db.itemDao().deleteAll()
        db.itemDao().insertAll(items)
    }

    private fun writeJson(items: List<Item>) {
        val itemsStr = Json.encodeToString(value = items)

        requireContext().openFileOutput("data.json", Context.MODE_PRIVATE)
            .use {
                it.write(itemsStr.toByteArray())
            }
    }
}