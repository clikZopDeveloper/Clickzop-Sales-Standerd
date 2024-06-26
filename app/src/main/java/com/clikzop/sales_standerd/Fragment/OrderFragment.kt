package com.clikzop.sales_standerd.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.clikzop.sales_standerd.Activity.CreateOrderActivity
import com.clikzop.sales_standerd.Activity.OrderDetailActivity
import com.clikzop.sales_standerd.Adapter.OrderListAdapter
import com.clikzop.sales_standerd.ApiHelper.ApiController
import com.clikzop.sales_standerd.ApiHelper.ApiResponseListner
import com.clikzop.sales_standerd.Model.OrderListBean
import com.clikzop.sales_standerd.R
import com.clikzop.sales_standerd.Utills.RvStatusComplClickListner
import com.clikzop.sales_standerd.Utills.SalesApp
import com.clikzop.sales_standerd.Utills.Utility
import com.clikzop.sales_standerd.databinding.FragmentOrderBinding
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class OrderFragment : Fragment() , ApiResponseListner {

    private var _binding: FragmentOrderBinding? = null
    private lateinit var apiClient: ApiController

    private val binding get() = _binding!!
var statusType="pending"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.appbarLayout.ivMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.appbarLayout.tvTitle.text = "Orders"
        binding.fbAddMeting.setOnClickListener {
            startActivity(Intent(requireContext(), CreateOrderActivity::class.java).putExtra("way", "CreateOrder"))
        }
        apiClient = ApiController(requireContext(), this)
      //  val KeyStatus=arguments?.getString("KeyStatus")
        val bundle = arguments
        if (bundle != null) {
            Log.d("wewe", "receivedData")
            val receivedData = arguments?.getString("KeyStatus")
            if (receivedData != null) {
                Log.d("wewe", receivedData)

                if (receivedData.equals("pending")) {
                    statusType = "pending"
                    binding.rbPending.setChecked(true)
                    binding.rbPending.setTextColor(getResources().getColor(R.color.black));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));

               //     Toast.makeText(requireContext(), statusType, Toast.LENGTH_SHORT).show()
                    apiGetOrder(statusType)
                }

                //  "delivered" ->statusType="delivered"
                else if (receivedData.equals("delivered")) {
                    statusType = "delivered"
                    binding.rbDelivered.setChecked(true)
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.black));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));

                 //   Toast.makeText(requireContext(), statusType, Toast.LENGTH_SHORT).show()
                    apiGetOrder(statusType)
                } else if (receivedData.equals("approved")) {
                    statusType = "approved"
                    binding.rbApproved.setChecked(true)
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.black));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));

                  //  Toast.makeText(requireContext(), statusType, Toast.LENGTH_SHORT).show()
                    apiGetOrder(statusType)
                } else if (receivedData.equals("rejected")) {
                    statusType = "rejected"
                    binding.rbRejected.setChecked(true)
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.black));

                 //   Toast.makeText(requireContext(), statusType, Toast.LENGTH_SHORT).show()
                    apiGetOrder(statusType)
                } else if (receivedData.equals("dispatched")) {
                    statusType = "dispatched"
                    binding.rbDispatched.setChecked(true)
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.black));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));

               //     Toast.makeText(requireContext(), statusType, Toast.LENGTH_SHORT).show()
                    apiGetOrder(statusType)

                } else {
                 //   Toast.makeText(requireContext(), statusType, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            apiGetOrder(statusType)
        }
        binding.refreshLayout.setOnRefreshListener {
            apiGetOrder(statusType)
            binding.refreshLayout.isRefreshing = false
        }
         /*   when (receivedData) {
              //  "pending" ->

                else -> println("Value is neither 1 nor 2")
            }*/

        typeMode()


        return root
    }

    fun typeMode() {
        binding.RadioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.rbPending) {
                //    Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT).show()
                    binding.rbPending.setTextColor(getResources().getColor(R.color.black));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));
                    statusType = "pending"
                    apiGetOrder(statusType)
                } else if (checkedId == R.id.rbApproved) {
                  //  Toast.makeText(requireContext(), "2", Toast.LENGTH_SHORT).show()

                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.black));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));
                    statusType = "approved"
                    apiGetOrder(statusType)
                } else if (checkedId == R.id.rbDispatched) {
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.black));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));
                    statusType = "dispatched"
                    apiGetOrder(statusType)
                }else if (checkedId == R.id.rbDelivered) {
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.black));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.white));
                    statusType = "delivered"
                    apiGetOrder(statusType)
                }else if (checkedId == R.id.rbRejected) {
                    binding.rbPending.setTextColor(getResources().getColor(R.color.white));
                    binding.rbApproved.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDispatched.setTextColor(getResources().getColor(R.color.white));
                    binding.rbDelivered.setTextColor(getResources().getColor(R.color.white));
                    binding.rbRejected.setTextColor(getResources().getColor(R.color.black));
                    statusType = "rejected"
                    apiGetOrder(statusType)
                }
            }
        })
    }

    fun apiGetOrder(statusType: String) {
        SalesApp.isAddAccessToken = true

        val params = Utility.getParmMap()
          apiClient.progressView.showLoader()
        params["status"]=statusType
        apiClient.getApiPostCall(ApiContants.GetOrders,params)

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.GetOrders) {
                val orderBean = apiClient.getConvertIntoModel<OrderListBean>(
                    jsonElement.toString(),
                    OrderListBean::class.java
                )
                if (orderBean.error == false) {
                    Toast.makeText(requireContext(),orderBean.msg,Toast.LENGTH_SHORT).show()
                    handleOrderList(orderBean.data)
                }
            }
        }catch (e:Exception){

        }
    }

    override fun failure(tag: String?, errorMessage: String?) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage!!)
    }

    fun handleOrderList(data: List<OrderListBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(requireContext())
        var mAdapter = OrderListAdapter(requireActivity(), data, object :
            RvStatusComplClickListner {
            override fun clickPos(status: String,workstatus: String,amt: String, id: Int) {
                     startActivity(
                         Intent(
                             requireContext(),
                             OrderDetailActivity::class.java
                         ).putExtra("order_id", id.toString())
                     )
            }
        })
        binding.rcTeamContactList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}