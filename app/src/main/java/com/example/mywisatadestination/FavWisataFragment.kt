package com.example.mywisatadestination

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywisatadestination.data.local.FavoriteDatabase
import com.example.mywisatadestination.data.model.Lokasi
import kotlinx.android.synthetic.main.fragment_fav_wisata.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

/**
 * A simple [Fragment] subclass.
 */
class FavWisataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_wisata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFavoritePlace()
    }

    private fun getFavoritePlace() {
        recyclerViewFav.layoutManager = LinearLayoutManager(context)
        FavoriteDatabase.getInstance(context!!).use {
            val result = select(Lokasi.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<Lokasi>())
            recyclerViewFav.adapter = MainAdapter(context!!, favorite) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("data", it)
                startActivity(intent)
            }
            if (favorite.isNotEmpty()) lytEmpty.visibility = View.GONE
        }
    }

    override fun onResume() {
        getFavoritePlace()
        super.onResume()
    }
}
