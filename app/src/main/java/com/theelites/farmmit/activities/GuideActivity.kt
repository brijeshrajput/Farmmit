package com.theelites.farmmit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.theelites.farmmit.R
import com.theelites.farmmit.adapters.GetStartedAdapter
import com.theelites.farmmit.models.GuideItems
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class GuideActivity : AppCompatActivity() {
    private lateinit var screenPager: ViewPager
    private lateinit var getStartedAdapter: GetStartedAdapter
    private lateinit var tabIndicator: TabLayout
    private lateinit var btnNext: Button
    private var position = 0
    private lateinit var btnGetStarted: Button
    private lateinit var btnAnim: Animation
    private lateinit var tvSkip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make the activity on full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // when this activity is about to be launch we need to check if its opened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setContentView(R.layout.activity_guide)

        // ini views
        btnNext = findViewById<Button>(R.id.btn_next)
        btnGetStarted = findViewById<Button>(R.id.btn_get_started)
        tabIndicator = findViewById<TabLayout>(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        tvSkip = findViewById<TextView>(R.id.btn_skip)
        // fill list screen
        val guide_list: MutableList<GuideItems> = ArrayList<GuideItems>()
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val wizards = obj.getJSONArray("wizards")
            for (i in 0 until wizards.length()) {
                val wizardsObject = wizards.getJSONObject(i)
                val title = wizardsObject.getString("title")
                val description = wizardsObject.getString("description")
                val thumbnail = wizardsObject.getString("thumbnail")
                guide_list.add(
                    GuideItems(
                        title, description, resources.getIdentifier(
                            thumbnail, "drawable",
                            packageName
                        )
                    )
                )
            }

            // TODO: ADD Guide Activity
            /*
                try {
                    guide_list.add(get_end_of_list(guide_list), new GuideItems(" ", " ", getResources().getIdentifier("", "drawable", getPackageName())));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            */
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // setup viewpager
        screenPager = findViewById(R.id.guide_viewpager)
        getStartedAdapter = GetStartedAdapter(this, guide_list)
        screenPager.setAdapter(getStartedAdapter)

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager)

        // next button click listener
        btnNext.setOnClickListener(View.OnClickListener { v: View? ->
            position = screenPager.getCurrentItem()
            if (position < guide_list.size) {
                position++
                screenPager.setCurrentItem(position)
            }
            if (position == guide_list.size - 1) { // when we reach to the last screen
                loadLastScreen()
            }
        })

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(object : BaseOnTabSelectedListener<TabLayout.Tab?> {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == guide_list.size - 1) {
                    loadLastScreen()
                } else {
                    reloadLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        // Get Started button click listener
        btnGetStarted.setOnClickListener(View.OnClickListener { v: View? ->
            savePrefsData()
            val successConnect = Intent(this@GuideActivity, LoginActivity::class.java)
            startActivity(successConnect)
            finish()
        })

        // skip button click listener
        tvSkip.setOnClickListener(View.OnClickListener { v: View? ->
            screenPager.setCurrentItem(
                guide_list.size
            )
        })
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("Shared Preferences", MODE_PRIVATE)
        return pref.getBoolean("isGuideOpened", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("Shared Preferences", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isGuideOpened", true)
        editor.apply()
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loadLastScreen() {
        btnNext.visibility = View.INVISIBLE
        btnGetStarted.visibility = View.VISIBLE
        tvSkip.visibility = View.INVISIBLE
        tabIndicator.visibility = View.INVISIBLE
        // setup animation
        btnGetStarted.animation = btnAnim
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun reloadLastScreen() {
        btnNext.visibility = View.VISIBLE
        btnGetStarted.visibility = View.INVISIBLE
        tvSkip.visibility = View.VISIBLE
        tabIndicator.visibility = View.VISIBLE
    }

    private fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val istream = applicationContext.assets.open("adapter/wizards.json")
            val size = istream.available()
            val buffer = ByteArray(size)
            istream.read(buffer)
            istream.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun getEndOfList(list: List<GuideItems>?): Int {
        return list?.size ?: -1
    }
}