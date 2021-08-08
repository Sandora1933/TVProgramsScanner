package com.example.tvprogramsscanner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvprogramsscanner.*
import com.example.tvprogramsscanner.model.ProgramItem
import com.example.tvprogramsscanner.other.MyRunnable
import com.example.tvprogramsscanner.other.ProgramAdapter
import com.example.tvprogramsscanner.service.ILoadMore
import com.example.tvprogramsscanner.service.RequestService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity(), ProgramAdapter.OnItemClickListener {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var retrofit: Retrofit

    lateinit var list: MutableList<ProgramItem?>
    lateinit var adapter: ProgramAdapter

    lateinit var serialNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize retrofit and get result from url
        retrofit = initRetrofit()

        // Get Android_ID unique identifier
        serialNumber = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        list = mutableListOf<ProgramItem?>()

        // Init recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ProgramAdapter(recyclerView, this, list, this)
        recyclerView.adapter = adapter

        // Initial request
        val requestService: RequestService = RequestService(this, retrofit, list, adapter)
        val myRunnable: MyRunnable =
            MyRunnable(
                requestService,
                serialNumber,
                0,
                0
            )

        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        executorService.execute(myRunnable)

        adapter.setILoadMore(loadMore = ILoadMore {
            if (list.size <= 1000) {
                list.add(null)
                adapter.notifyItemInserted(list.size - 1)

                val r = Runnable {
                    list.removeAt(list.size - 1)
                    adapter.notifyItemRemoved(list.size)

                    generate40Data()
                    adapter.notifyDataSetChanged()

                    adapter.setLoaded()

                }
                Handler(Looper.getMainLooper()).postDelayed(r, 1000)

            } else {
                Toast.makeText(this, "All data", Toast.LENGTH_SHORT).show()
            }
        })

        // Set up toggle
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.ollTvHeader -> Toast.makeText(this, "ollTv clicked", Toast.LENGTH_SHORT).show()
                R.id.channels -> Toast.makeText(this, "Channels clicked", Toast.LENGTH_SHORT).show()
                R.id.moviesSeries -> Toast.makeText(
                    this,
                    "Movies Series clicked",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.programs -> Toast.makeText(this, "Programs clicked", Toast.LENGTH_SHORT).show()
                R.id.forChildren -> Toast.makeText(this, "For children clicked", Toast.LENGTH_SHORT)
                    .show()
                R.id.football -> Toast.makeText(this, "Football clicked", Toast.LENGTH_SHORT).show()
                R.id.profile -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    private fun generate40Data(){
        val borderId: Int = list[list.size-1]!!.id

        val requestService: RequestService = RequestService(this, retrofit, list, adapter)
        val myRunnable: MyRunnable =
            MyRunnable(
                requestService,
                serialNumber,
                borderId,
                0
            )

        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        executorService.execute(myRunnable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRetrofit(): Retrofit{
        return Retrofit.Builder()
                .baseUrl("http://oll.tv/api/demo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    override fun onItemClick(pos: Int) {
        val program = list.get(pos)
        launchActivityWithProgramInfo(program!!)
    }

    fun launchActivityWithProgramInfo(program: ProgramItem){
        val intent: Intent = Intent(this, ProgramInfoActivity::class.java)
        intent.putExtra("description", program.description)
        startActivity(intent)
    }

}