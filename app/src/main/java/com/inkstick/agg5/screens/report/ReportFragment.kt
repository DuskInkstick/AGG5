package com.inkstick.agg5.screens.report

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.inkstick.agg5.R
import com.inkstick.agg5.data.AppDb
import com.inkstick.agg5.model.Item

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = AppDb.getDatabase(requireContext())

        // Какое-то условие выборки, мб на экране должно быть поле для ввода критерия, но пока так
        val data = db.itemDao().loadWithCodeLessThen(3)
        val arrayAdapter = ArrayAdapter<Item>(requireContext(), android.R.layout.simple_list_item_1)
        arrayAdapter.addAll(data)

        view.findViewById<ListView>(R.id.report_list)?.adapter = arrayAdapter
    }
}