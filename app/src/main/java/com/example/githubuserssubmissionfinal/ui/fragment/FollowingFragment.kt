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
import com.example.githubuserssubmission.databinding.FragmentFollowingBinding
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.viewmodel.DetailViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(context)
        binding.followingsRv.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(context, layoutManager.orientation)
        binding.followingsRv.addItemDecoration(itemDecor)

        val detailVM = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailVM.followings.observe(viewLifecycleOwner) {
            setDataFollowings(it)
        }

        detailVM.isLoading.observe(viewLifecycleOwner) {
            loading(it)
        }
        return binding.root
    }

    private fun setDataFollowings(user: List<Users>) {
        binding.msgFollowings.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.followingsRv.apply {
            binding.followingsRv.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.followingsRv.adapter = listUserAdapter
            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {
                    val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
                    detailViewModel.dataUser = data.login
                }
            })
        }
    }

    private fun loading(isLoading: Boolean) {
        binding.followingsProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}