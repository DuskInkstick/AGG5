package com.inkstick.agg5.screens.edit

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import com.inkstick.agg5.R
import com.inkstick.agg5.data.AppDb
import com.inkstick.agg5.model.Item

class EditFragment : Fragment() {

    private val viewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDb.getDatabase(requireContext())

        val id = requireArguments().getInt("itemId")

        if (id > 0) {
            val item = db.itemDao().get(id)
            setItem(item)
        }

        view.apply {
            findViewById<EditText>(R.id.et_name)?.doOnTextChanged { text, start, before, count ->
                viewModel.name = text.toString()
            }

            findViewById<EditText>(R.id.et_code)?.doOnTextChanged { text, start, before, count ->
                viewModel.code = text.toString()
            }

            findViewById<Button>(R.id.b_save)?.setOnClickListener {

                if (id > 0) {
                    val newItem = Item(viewModel.name, viewModel.code.toIntOrNull() ?: 0, id)
                    db.itemDao().update(newItem)

                } else {
                    val newItem = Item(viewModel.name, viewModel.code.toIntOrNull() ?: 0)
                    db.itemDao().insert(newItem)
                }
                findNavController().popBackStack()
            }

        }
    }

    private fun setItem(item: Item) {
        view?.apply {
            findViewById<EditText>(R.id.et_name)?.setText(item.name)

            findViewById<EditText>(R.id.et_code)?.setText(item.code.toString())
        }
    }
}