package com.me.livedataflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.me.livedataflow.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

const val TAG = "LIVEDATA_FLOW_TAG"

class MainActivity : AppCompatActivity() {
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate ===============")

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.liveDataBtn.setOnClickListener {
            viewModel.populateLiveData()
        }

        binding.stateFlowBtn.setOnClickListener {
            viewModel.populateStateFlow()
        }

//        observeTvLiveData(binding)
        collectColdFlow(binding)
//        collectStateFlow(binding)
//        collectSharedFlow(binding)
    }

    private fun observeTvLiveData(binding: ActivityMainBinding) {
        viewModel.tvLiveData.observe(this){
            binding.tvLiveData.text = it
            Log.d(TAG, "live data consumed: $it")
        }
    }

    private fun collectColdFlow(binding: ActivityMainBinding) {
        this.lifecycleScope.launch {
           // .launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coldFlow.collect {
                    binding.tvFlow.text = it
                    Log.d(TAG, "cold flow consumed: $it")
                }
            }
        }
    }

    private fun collectStateFlow(binding: ActivityMainBinding) {
        this.lifecycleScope.launch {
                viewModel.stateFlow.collect {
                    binding.tvStateFlow.text = it
                    Log.d(TAG, "state flow consumed: $it")
                }
        }
    }

    private fun collectSharedFlow(binding: ActivityMainBinding) {
        this.lifecycleScope.launch {
            viewModel.sharedFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect{
                binding.tvSharedFlow.text = it
                Log.d(TAG, "shared flow consumed: $it")
            }
        }
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart ===============")
        super.onRestart()
    }

    override fun onStart() {
        Log.d(TAG, "onStart ===============")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume ===============")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause ===============")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop ===============")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy ===============")
        super.onDestroy()
    }


}


//Activity:
// onCreate -> onStart -> onResume -> ACTIVITY RUNNING -> onPause -> onStop -> onDestroy
// Activity can be paused if apps with high priority are working: onPause -> onResume-> ACTIVITY RUNNING
// Phone rotation: onPause -> onStop -> onDestroy -> onCreate -> onStart -> onResume -> ACTIVITY RUNNING
// Home btn clicked: onStop -> onRestart -> onStart -> onResume -> ACTIVITY RUNNING (app goes to background)

//LiveData
// - lifecycle aware (data observed(consumed) when UI is visible)
// - continue to  emit values even if no one observes the live data



//Flow:
// - stream of data
// - not lifecycle aware
//   but could be made lifecycle aware by using:
//   1. asLiveData
//   2. in fragment consumer set:
//   viewlifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) - for cold
//   flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED) - for hot
//     (complete stop collecting onStop, collect data when activity or fragment started: not collecting between onStop and onStart)


// - cold stream (Flow) :
//      stop emission if consumer is not active (no one collect the flow)
//      when lifecycleScope.launchWhenStarted (suspend flow - stops onStop and continues to emit(consume) onStart)
//      when lifecycleScope.launch (flow stops completely onStop)

// - hot stream (StateFlow, SharedFlow)
//      emitting even there is not consumer (hot)
//      accept more than 1 consumer
//      remains in memory as long as collecting or has GC reference
//      StateFlow - does not show repeated value, show latest value
//      SharedFlow - shows repeated value, show more than latest value by using replay

//      https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda