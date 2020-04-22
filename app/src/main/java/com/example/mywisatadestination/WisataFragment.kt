package com.example.mywisatadestination


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywisatadestination.data.model.Lokasi
import com.example.mywisatadestination.data.remote.ApiFactory
import kotlinx.android.synthetic.main.fragment_wisata.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class WisataFragment : Fragment() {
    private val placeService = ApiFactory.placeApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wisata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        swipe_refresh.isRefreshing = true
        getData()
        swipe_refresh.setOnRefreshListener {
            getData()
        }
        bt_retry.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            val newsTechRequest = placeService.getLokasi()
            try {
                val response = newsTechRequest.await()
                if (response.isSuccessful) {
                    showFailedLayout(false)
                    val newsResponse = response.body()
                    val placeData = newsResponse?.data?.lokasi
                    println(placeData)
                    swipeProgress(false)
                    if (!placeData.isNullOrEmpty()) {
//                        dataArticle = topTechNews

                        setAdapterData(placeData)
                    }
                } else {
                    showFailedLayout(true)
                    swipeProgress(false)
                    println("Respon gagal")
                    Log.e("ResonponseFailed", response.errorBody().toString())
                }
            } catch (e: Exception) {
                swipeProgress(false)
                showFailedLayout(true)
                Log.e("ResponseException", e.localizedMessage)
            }
        }
    }

    private fun swipeProgress(show: Boolean) {
        if (!show) {
            swipe_refresh.isRefreshing = show
            return
        }
        swipe_refresh.post { swipe_refresh.isRefreshing = show }
    }

    private fun setAdapterData(listLokasi: List<Lokasi>) {
        println("masuk")
        recyclerView.adapter = MainAdapter(context!!, listLokasi) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("data", it)
            startActivity(intent)
        }
    }

    private fun showFailedLayout(show: Boolean) {
        if (show) {
            layoutFailed.visibility = View.VISIBLE
            swipe_refresh.visibility = View.GONE
        } else {
            layoutFailed.visibility = View.GONE
            swipe_refresh.visibility = View.VISIBLE
        }
    }
}
