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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fizarum.get10kusd.app.adapters.EditUserClickListener
import com.fizarum.get10kusd.app.adapters.UsersAdapter
import com.fizarum.get10kusd.app.extensions.viewModelFactory
import com.fizarum.get10kusd.app.viewmodels.EditDailyWageViewModel
import com.fizarum.get10kusd.app.viewmodels.UserListViewModel
import com.fizarum.get10kusd.data.db.AppDatabase
import com.fizarum.get10kusd.data.repositories.UserRepositoryImpl
import com.fizarum.get10kusd.data.rest.RestClient
import com.fizarum.get10kusd.databinding.FragmentUserListBinding
import com.fizarum.get10kusd.domain.entities.Goal
import com.fizarum.get10kusd.domain.entities.User
import com.fizarum.get10kusd.domain.repositories.UserRepository
import com.fizarum.get10kusd.domain.usecases.GetEstimatedDaysUseCase
import com.fizarum.get10kusd.domain.usecases.GetUserListUseCase
import com.fizarum.get10kusd.domain.usecases.LoadDailyWagesUseCase
import com.fizarum.get10kusd.domain.usecases.SaveDailyWageUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class UserListFragment : Fragment(), EditUserClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentUserListBinding

    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(requireContext().applicationContext)
    }

    private val viewModel: UserListViewModel by activityViewModels(factoryProducer = viewModelFactory {
        val repo: UserRepository = UserRepositoryImpl(RestClient, db)
        val getUserListUseCase = GetUserListUseCase(repo)
        val getEstimatedDaysUseCase = GetEstimatedDaysUseCase()
        val saveDailyWageUseCase = SaveDailyWageUseCase(repo)
        val loadDailyWageUseCase = LoadDailyWagesUseCase(repo)
        UserListViewModel(
            getUserListUseCase,
            getEstimatedDaysUseCase,
            saveDailyWageUseCase,
            loadDailyWageUseCase
        )
    })

    private val editDailyWageViewModel: EditDailyWageViewModel by activityViewModels()
    private var refreshTimeoutDisposable: Disposable? = null

    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(layoutInflater, this)
    }

    private val goalToGet10K = Goal(10000)

    private val userListObserver = Observer<List<User>> { list ->
        stopRefreshingSwipe()
        //check if at least first user has valid information
        if (viewModel.mayListBeShown(list)) {
            val sorted = viewModel.sortByDaysASC(list)
            val usersWithEstimatedDays = viewModel.estimatedDaysForUsers(sorted, goalToGet10K)
            usersAdapter.setUsersWithDays(usersWithEstimatedDays)
        }
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
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUserList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
        }
        binding.srRefreshLayout.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        subscribeOnLiveData()
        viewModel.fetchUserList()
    }

    override fun onPause() {
        super.onPause()
        unsubscribeFromLiveData()
        stopRefreshingSwipe()
    }

    override fun onUserEditInitiated(user: User) {
        editDailyWageViewModel.cleanUpDailyWageValue()
        val action = UserListFragmentDirections.openEditDialog(user)
        findNavController().navigate(action)
    }

    override fun onRefresh() {
        refreshTimeoutDisposable = Observable.timer(REFRESH_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    stopRefreshingSwipe()
                }, {
                    stopRefreshingSwipe()
                })
        viewModel.fetchUserList()
    }

    private fun subscribeOnLiveData() {
        with(viewLifecycleOwner) {
            viewModel.usersList.observe(this, userListObserver)
            editDailyWageViewModel.userWithNewDailyWage.observe(this, changedDailyWageObserver)
        }
    }

    private fun unsubscribeFromLiveData() {
        viewModel.usersList.removeObserver(userListObserver)
        editDailyWageViewModel.userWithNewDailyWage.removeObserver(changedDailyWageObserver)
    }

    private fun stopRefreshingSwipe() {
        if (refreshTimeoutDisposable != null && refreshTimeoutDisposable?.isDisposed == false) {
            refreshTimeoutDisposable?.dispose()
        }
        binding.srRefreshLayout.isRefreshing = false
    }

    companion object {
        const val REFRESH_TIMEOUT_SECONDS = 4L
    }
}