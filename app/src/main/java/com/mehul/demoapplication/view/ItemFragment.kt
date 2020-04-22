package com.mehul.demoapplication.view

import CommonUtility.CommonMethod
import CommonUtility.HttpManager
import CommonUtility.RequestPackage
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.baoyz.widget.PullRefreshLayout
import com.mehul.demoapplication.R
import com.mehul.demoapplication.databinding.FragmentItemBinding
import com.mehul.demoapplication.model.Item
import org.json.JSONObject
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ItemFragment : Fragment() {

    private var binding: FragmentItemBinding? = null
    internal lateinit var swipeRefreshLayout: PullRefreshLayout
    var isRefresh: Boolean = false
    lateinit var adapter: ItemAdapter
    var listItem = ArrayList<Item>()
    lateinit var llOffline: LinearLayout
    lateinit var llOnLine: LinearLayout
    var title = ""
    lateinit var sp: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sp = PreferenceManager.getDefaultSharedPreferences(activity)
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false)


        val view = binding?.root
        // pull down request
        swipeRefreshLayout = binding?.root?.findViewById(R.id.swipeRefreshLayout)!!
        llOffline = binding?.root?.findViewById(R.id.llOffline)!!
        llOnLine = binding?.root?.findViewById(R.id.llOnLine)!!
        swipeRefreshLayout.setOnRefreshListener {
            isRefresh = true
            getData()
        }
        getData()
        return view
    }


    //region Make call http request and get json data and parse in adapter
    internal inner class Task : AsyncTask<String, Void, ArrayList<Item>>() {

        private var pd: ProgressDialog? = null


        override fun onPreExecute() {
            super.onPreExecute()
            listItem.clear()
            if (!isRefresh) {
                pd = ProgressDialog(activity)
                pd!!.setTitle("Processing...")
                pd!!.setMessage("Please wait.")
                pd!!.setCancelable(false)
                pd!!.isIndeterminate = true
                pd!!.show()
            }


        }

        override fun doInBackground(vararg obj: String): ArrayList<Item> {

            val rp = RequestPackage()
            rp.uri = CommonMethod.Main_URL
            rp.method = "GET"

            val result = HttpManager.getData(rp)

            sp.edit().putString("items", result).apply()
            val jsonObject = JSONObject(result)
            title = jsonObject.optString("title")




            return getListItem(jsonObject)

        }

        override fun onPostExecute(result: ArrayList<Item>) {
            super.onPostExecute(result)
            if (pd != null)
                pd!!.dismiss()
            activity?.title = title



            adapter = ItemAdapter(result, context)
            binding?.itemAdapter = adapter
            if (isRefresh) {
                swipeRefreshLayout.setRefreshing(false)
                isRefresh = !isRefresh
            }

        }
    }
    //endregion


    //region getData From server
    // Set if refreshing data
    private fun getData() {

        if (CommonMethod.isOnline(activity)) {

            llOnLine.visibility = View.VISIBLE

            llOffline.visibility = View.GONE
            Task().execute()
        } else {
            swipeRefreshLayout.setRefreshing(false)
            Toast.makeText(activity, "You are offline", Toast.LENGTH_LONG).show()
            if (sp.getString("items", "") != null) {

                if (sp.getString("items", "").isNotEmpty()) {

                    adapter =
                        ItemAdapter(getListItem(JSONObject(sp.getString("items", ""))), context)
                    activity?.title = title
                    binding?.itemAdapter = adapter
                } else {
                    llOnLine.visibility = View.GONE

                    llOffline.visibility = View.VISIBLE
                    isRefresh = !isRefresh
                }
            } else {
                llOnLine.visibility = View.GONE

                llOffline.visibility = View.VISIBLE
                isRefresh = !isRefresh
            }
        }
    }
    //endregion


    fun getListItem(json: JSONObject): ArrayList<Item> {
        title = if (json.isNull("title")) "" else json.optString("title", "")
        listItem.clear()
        val jsonArray = json.getJSONArray("rows")
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val item = Item()
            item.description =
                if (obj.isNull("description")) "" else obj.optString("description", "")
            item.title = if (obj.isNull("title")) "" else obj.optString("title", "")
            item.imageHref = obj.optString("imageHref")
            listItem.add(item)

        }
        return listItem
    }
}

