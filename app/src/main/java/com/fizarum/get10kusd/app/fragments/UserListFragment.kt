package com.fizarum.get10kusd.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fizarum.get10kusd.app.adapters.EditUserClickListener
import com.fizarum.get10kusd.app.adapters.UsersAdapter
import com.fizarum.get10kusd.app.extensions.viewModelFactory
import com.fizarum.get10kusd.app.viewmodels.EditDailyWageViewModel
import com.fizarum.get10kusd.app.viewmodels.UserListViewModel
import com.fizarum.get10kusd.data.repositories.UserRepositoryImpl
import com.fizarum.get10kusd.data.rest.RestClient
import com.fizarum.get10kusd.databinding.FragmentUserListBinding
import com.fizarum.get10kusd.domain.entities.Goal
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.repositories.UserRepository
import com.fizarum.get10kusd.domain.usecases.GetEstimatedDaysUseCase
import com.fizarum.get10kusd.domain.usecases.GetUserListUseCase

class UserListFragment : Fragment(), EditUserClickListener {

    private lateinit var binding: FragmentUserListBinding

    private val viewModel: UserListViewModel by activityViewModels(factoryProducer = viewModelFactory {
        val repo: UserRepository = UserRepositoryImpl(RestClient)
        val getUserListUseCase = GetUserListUseCase(repo)
        val getEstimatedDaysUseCase = GetEstimatedDaysUseCase()
        UserListViewModel(getUserListUseCase, getEstimatedDaysUseCase)
    })

    private val editDailyWageViewModel: EditDailyWageViewModel by activityViewModels()

    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(layoutInflater, this)
    }

    private val goalToGet10K = Goal(10000)

    private val userListObserver = Observer<List<User>> { list ->
        val sorted = viewModel.sortByDaysASC(list)
        val usersWithEstimatedDays = viewModel.estimatedDaysForUsers(sorted, goalToGet10K)
        usersAdapter.setUsersWithDays(usersWithEstimatedDays)
    }

    private val changedDailyWageObserver = Observer<User> {
        it?.let { newUserData ->
            viewModel.updateDailyWageForUser(newUserData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUserList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.userListLiveData.observe(viewLifecycleOwner, userListObserver)
        editDailyWageViewModel.userWithNewDailyWage.observe(
            viewLifecycleOwner,
            changedDailyWageObserver
        )
        viewModel.fetchUserList()
    }

    override fun onPause() {
        super.onPause()

        viewModel.userListLiveData.removeObserver(userListObserver)
        editDailyWageViewModel.userWithNewDailyWage.removeObserver(changedDailyWageObserver)
    }

    override fun onUserEditClicked(user: User) {
        val action = UserListFragmentDirections.openEditDialog(user)
        findNavController().navigate(action)
    }
}