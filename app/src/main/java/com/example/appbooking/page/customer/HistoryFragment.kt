package com.example.appbooking.page.customer

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.appbooking.Database.MySQLite
import com.example.appbooking.Model.Don
import com.example.appbooking.page.customer.Adapter.DonAdapter
import com.example.appbooking.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale
import java.util.logging.Handler

class HistoryFragment : Fragment() {
    val db = MySQLite()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val tvError = view.findViewById<TextView>(R.id.tvError)
        val lvDon = view.findViewById<ListView>(R.id.lvDon)



        val sharedPreferences = requireContext().getSharedPreferences("UserInfo", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val username = sharedPreferences.getString("username", "Guest")
        val role = sharedPreferences.getInt("role", -1)

        loadData(userId, lvDon, tvError)

        return view
    }


    private fun loadData(userId: Int, lvDon: ListView, tvError: TextView) {
        // Disable interactions during data loading
        lvDon.isClickable = false
        lvDon.isEnabled = false

        // Set the error TextView to show "Đang tải..." (loading message)
        tvError.text = getString(R.string.loading)
        tvError.visibility = View.VISIBLE

        // Simulate a delay for loading data
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            // Simulate fetching data from the database
            val listDon = db.layDuLieuDonCuaUser(userId)
            // Check if data is found
            if (listDon.isEmpty()) {
                tvError.text = getString(R.string.no_orders_found)
            } else {
                tvError.visibility = View.GONE  // Hide error message if data is found
                val donAdapter = DonAdapter(requireContext(), R.layout.user_item_history, listDon)
                lvDon.adapter = donAdapter
            }

            lvDon.isClickable = true
            lvDon.isEnabled = true
        }, 350)  // Adjust the delay time as needed
    }


}
