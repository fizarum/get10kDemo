package com.fizarum.get10kusd.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.fizarum.get10kusd.app.viewmodels.EditDailyWageViewModel
import com.fizarum.get10kusd.databinding.DialogEditDailyWageBinding

class EditDailyWageDialog : DialogFragment() {

    private lateinit var binding: DialogEditDailyWageBinding

    private val args: EditDailyWageDialogArgs by navArgs()
    private val viewModel: EditDailyWageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEditDailyWageBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userWithNewDailyWage.value = args.user

        binding.btCancel.setOnClickListener {
            dismiss()
        }

        binding.btSave.setOnClickListener {
            viewModel.saveDailyWage()
            dismiss()
        }
    }
}