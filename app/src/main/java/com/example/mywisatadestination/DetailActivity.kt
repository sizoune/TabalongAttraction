package com.example.mywisatadestination

import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mywisatadestination.data.local.FavoriteDatabase
import com.example.mywisatadestination.data.model.Lokasi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.selects.select
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailActivity : AppCompatActivity() {
    private lateinit var tempat: Lokasi
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var stateFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tempat = intent.getParcelableExtra("data")
        Picasso.get().load(tempat.foto).placeholder(R.drawable.image_12).fit().into(detailImage)
        txtTitle.text = tempat.nama
        txtDesc.text = tempat.deskripsi.toSpanned()
        isFavorite = checkFavorite(tempat.id)
        println("fafv ? " + isFavorite)
    }

    fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite){
                    removeFavorite(tempat.id)
                } else {
                    addToFavorite()
                }

                isFavorite = !isFavorite
                setFavorite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite() {
        if (!stateFav)
            addFavoritetoDb(tempat)
    }

    fun checkFavorite(idPlace: Int): Boolean {
        lateinit var favoriteMatch: List<Lokasi>
        FavoriteDatabase.getInstance(this@DetailActivity).use {
            val result = select(Lokasi.TABLE_FAVORITE)
                .whereArgs(
                    "(ID_ = {id})",
                    "id" to idPlace
                )
            favoriteMatch = result.parseList(classParser())
        }
        if (favoriteMatch.isNotEmpty())
            return true
        return false
    }

    fun addFavoritetoDb(data: Lokasi) {
        if (!checkFavorite(data.id)) {
            try {
                FavoriteDatabase.getInstance(this@DetailActivity).use {
                    insert(
                        Lokasi.TABLE_FAVORITE,
                        Lokasi.ID to data.id,
                        Lokasi.DESKRIPSI to data.deskripsi,
                        Lokasi.NAMA to data.nama,
                        Lokasi.LONGITUDE to data.longitude,
                        Lokasi.LATITUDE to data.latitude,
                        Lokasi.FOTO to data.foto
                    )
                }
                Toast.makeText(
                    applicationContext,
                    "Berhasil Di tambahkan ke Favorite",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: SQLiteConstraintException) {
                Log.e("SQLExc", e.localizedMessage)
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun removeFavorite(idPlace: Int) {
        println("remove")
        try {
            FavoriteDatabase.getInstance(this@DetailActivity).use {
                delete(
                    Lokasi.TABLE_FAVORITE,
                    "(ID_ = {id})",
                    "id" to idPlace
                )
            }
            Toast.makeText(
                applicationContext,
                "Berhasil Di hapus dari Favorite",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: SQLiteConstraintException) {
            Log.e("SQLExc", e.localizedMessage)
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}
