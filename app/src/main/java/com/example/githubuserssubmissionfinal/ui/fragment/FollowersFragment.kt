package com.example.githubuserssubmissionfinal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserssubmissionfinal.adapter.ListUserAdapter
import com.example.githubuserssubmission.databinding.FragmentFollowersBinding
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.viewmodel.DetailViewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(context)
        binding.followersRv.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(context, layoutManager.orientation)
        binding.followersRv.addItemDecoration(itemDecor)

        val detailVM = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailVM.followers.observe(viewLifecycleOwner) {
            setUserFollowerData(it)
        }

        detailVM.isLoading.observe(viewLifecycleOwner) {
            loading(it)
        }

        return binding.root
    }

    private fun setUserFollowerData(user: List<Users>) {
        binding.msgFollowers.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.followersRv.apply {
            binding.followersRv.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.followersRv.adapter = listUserAdapter
            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {
                    val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
                    detailViewModel.dataUser = data.login
                }
            })
        }
    }

    private fun loading(isLoading: Boolean) {
        binding.followersProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}