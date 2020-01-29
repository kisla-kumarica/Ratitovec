package com.example.ratitovec

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.fatboyindustrial.gsonjodatime.*
import java.util.*
import com.google.gson.reflect.TypeToken
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import org.joda.time.DateTime
import com.google.gson.GsonBuilder
import com.fatboyindustrial.gsonjodatime.Converters
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    lateinit var adapter: DatumiListAdapter
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var prefs: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        adapter = DatumiListAdapter(this)

        var pohodiRec = findViewById<RecyclerView>(R.id.DatumiList)
        pohodiRec.setHasFixedSize(true)
        var manager = LinearLayoutManager(this)
        pohodiRec.layoutManager = manager

        if (prefs.getString("Pohodi", "") != "") {
            var pohodi: MutableList<PohodLegacy>
            var gson: Gson = Converters.registerDateTime(GsonBuilder()).create()
            val groupListType = object : TypeToken<MutableList<PohodLegacy>>() {}.type
            Log.d("sdsdsd", prefs.getString("Pohodi", "")!!)

            pohodi = gson.fromJson<MutableList<PohodLegacy>>(
                prefs.getString("Pohodi", ""),
                groupListType
            )
            viewModel.insert(pohodi.toPohodi())
            prefs.edit().putString("Pohodi", null).apply()

        }

        counterView = findViewById<TextView>(R.id.CounterView)

        DatumiList.setItemViewCacheSize(9999)
        viewModel.pohodi.observe(this, androidx.lifecycle.Observer {
            adapter.submitList(it)
            findViewById<TextView>(R.id.CounterView).text = it.size.toString()
        })
        pohodiRec.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            pohodiRec.context,
            manager.orientation
        )
        pohodiRec.addItemDecoration(dividerItemDecoration)

    }


    fun MutableList<PohodLegacy>.toPohodi(): MutableList<Pohod> {
        val pohodi = mutableListOf<Pohod>()
        for (i: PohodLegacy in this) {
            pohodi.add(Pohod(i.Datum.millis))
        }
        return pohodi
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.action_menu, menu)
        menu!!.findItem(R.id.Menu_Yearly).isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.Menu_All -> {
                viewModel.selectAll()
                viewModel.pohodi.observe(
                    this,
                    androidx.lifecycle.Observer {
                        adapter.submitList(it)
                        findViewById<TextView>(R.id.CounterView).text = it.size.toString()
                    })
                item.isChecked = true
            }
            R.id.Menu_Yearly -> {
                viewModel.selectThisYear()
                viewModel.pohodi.observe(
                    this,
                    androidx.lifecycle.Observer {
                        adapter.submitList(it)
                        findViewById<TextView>(R.id.CounterView).text = it.size.toString()
                    })
                item.isChecked = true
                DatumiList.smoothScrollToPosition(0)
            }

        }
        return true
    }


    lateinit var counterView: TextView

    fun onPovecajClick(v: View) {
        var but = findViewById<Button>(R.id.DodajButton)
        but.isEnabled = false

        viewModel.insert(Pohod(Date().time))


        Toast.makeText(this, "Bravo, le tako naprej!", Toast.LENGTH_LONG).show()
    }
}
