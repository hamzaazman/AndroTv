package com.hamzaazman.androtv.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hamzaazman.androtv.common.viewBinding
import com.hamzaazman.androtv.databinding.ActivityMainBinding
import com.hamzaazman.androtv.ui.detail.DetailActivity
import com.hamzaazman.androtv.ui.feed.adapter.ChannelAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val vm: MainViewModel by viewModels()
    private val channelAdapter by lazy { ChannelAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.channelRv.adapter = channelAdapter
        channelAdapter.onChannelClickItem = {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("url", it.url)
            startActivity(intent)
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.getAllChannel() }
                launch {
                    vm.channelUiState.collectLatest { state ->
                        if (state.loading) {
                            binding.loadingBar.visibility = View.VISIBLE
                        } else {
                            binding.loadingBar.visibility = View.GONE
                        }

                        if (state.errorMessage.isNotEmpty()) {
                            binding.errorMessage.apply {
                                text = state.errorMessage
                                visibility = View.VISIBLE
                            }
                        } else {
                            binding.errorMessage.apply {
                                text = ""
                                visibility = View.GONE
                            }
                        }

                        if (state.channelList.isNotEmpty()) {
                            channelAdapter.submitList(state.channelList)
                        }


                    }
                }
            }
        }

    }
}