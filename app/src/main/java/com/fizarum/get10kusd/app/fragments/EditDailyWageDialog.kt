package com.fizarum.get10kusd.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fizarum.get10kusd.databinding.DialogEditDailyWageBinding

class EditDailyWageDialog : DialogFragment() {

    private lateinit var binding: DialogEditDailyWageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEditDailyWageBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        return binding.root
    }

}