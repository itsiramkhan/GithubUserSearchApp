package com.iram.githubusersearchapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iram.githubusersearchapp.R
import com.iram.githubusersearchapp.adapter.MainAdapter
import com.iram.githubusersearchapp.model.GithubUsers
import com.iram.githubusersearchapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var adapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupAPICall()
        observeSearchList()
        searchUser()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupAPICall() {
        mainViewModel.userListLiveData.observe(this, Observer {
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            renderList(it)
        })
    }

    private fun renderList(users: List<GithubUsers>) {
        adapter.apply {
            addData(users)
            notifyDataSetChanged()
        }
    }

    private fun searchUser(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    mainViewModel.getUserListByName(query.trim())
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    mainViewModel.getUserListByName(newText.trim())
                }
                return true
            }
        })
    }

    private fun observeSearchList() {
        mainViewModel.searchUserListLiveData.observe(this, Observer {
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            renderList(it)
        })
    }
}