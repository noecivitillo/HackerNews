package test.hackernews.data

import androidx.lifecycle.LiveData

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 * check https://github.com/android/architecture-components-samples/tree/master/PagingWithNetworkSample
 */
data class Listing<T>(
    // the LiveData of the lists for the UI to observe
    val list: LiveData<List<T>>,
    // represents the status to show to the user
    val loadingState: LiveData<LoadingState>,
    // represents the refresh status to show to the user. Separate from networkState, this
    // value is importantly only when refresh is requested.
    val refreshState: LiveData<LoadingState>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit
)