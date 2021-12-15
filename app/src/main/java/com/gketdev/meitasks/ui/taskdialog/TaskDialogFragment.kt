package com.gketdev.meitasks.ui.taskdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.gketdev.meitasks.databinding.FragmentDialogBinding

class TaskDialogFragment : DialogFragment() {

    private val args: TaskDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.task = args.selectedTask
        return binding.root
    }

}