package com.gketdev.meitasks.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gketdev.meitasks.adapter.TaskAdapter
import com.gketdev.meitasks.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    lateinit var binding: FragmentHomeBinding

    companion object {
        const val ACTIVE = 1
        const val ARCHIVED = 18
    }

    @Inject
    lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
        initListeners()
    }

    private fun initUi() {
        binding.imageViewSearchIcon.visibility = View.VISIBLE
        binding.textViewSearchProvide.visibility = View.VISIBLE
        binding.recyclerViewTask.adapter = adapter
    }

    private fun initObservers() {
        viewModel.viewState.observe(this.viewLifecycleOwner) {
            when (it) {
                is HomeViewState.Error -> {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                is HomeViewState.Tasks -> {
                    adapter.tasks = it.list
                    binding.imageViewSearchIcon.visibility = View.GONE
                    binding.textViewSearchProvide.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                is HomeViewState.RequestLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun searchListener() {
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            private fun emptyQueryController(query: String?) {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchQuery(query, findStatusFromChip())
                } else {
                    adapter.tasks = emptyList()
                    binding.textViewSearchProvide.visibility = View.VISIBLE
                    binding.imageViewSearchIcon.visibility = View.VISIBLE
                }
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                emptyQueryController(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                emptyQueryController(query)
                return false
            }
        })
    }


    private fun initListeners() {
        searchListener()
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val query = binding.searchView.query.toString()
            if (query.isEmpty()) return@setOnCheckedChangeListener
            when (checkedId) {
                binding.chipAll.id -> viewModel.searchQuery(query, null)
                binding.chipActive.id -> viewModel.searchQuery(query, ACTIVE)
                binding.chipArchived.id -> viewModel.searchQuery(query, ARCHIVED)
            }
        }
        adapter.onTaskClicked = {
            val direction = HomeFragmentDirections.actionHomeFragmentToTaskDialogFragment(it)
            findNavController().navigate(direction)
        }
    }

    private fun findStatusFromChip(): Int? {
        return when (binding.chipGroup.checkedChipId) {
            binding.chipAll.id -> null
            binding.chipActive.id -> ACTIVE
            binding.chipArchived.id -> ARCHIVED
            else -> null
        }
    }


}