# LiveDataFlow

## Screen Demo

![Screenshot_20220513_161401 1](https://user-images.githubusercontent.com/45378000/168383094-c8829d0a-9c85-455c-a3d6-6956d5c1a602.png)

## Live Data
  Consumer: <img width="611" alt="image" src="https://user-images.githubusercontent.com/45378000/168383684-51dd189e-2db9-4e1f-810e-529d6e562109.png">

  No consumer: <img width="609" alt="image" src="https://user-images.githubusercontent.com/45378000/168383944-f0657d91-c894-454b-a53c-57f003cdeb40.png">

## Cold Flow (no consuming - no emitting)

- No LifecycleAware 
Consumer: <img width="610" alt="image" src="https://user-images.githubusercontent.com/45378000/168384650-8540a682-0f8f-4163-854e-f0a6d9844e4a.png">

- LifecycleAware

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
    
Consumer: <img width="609" alt="image" src="https://user-images.githubusercontent.com/45378000/168385773-dc3a1014-2333-478b-8f7b-d243226a295f.png">

