package com.example.smartboon.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.smartboon.R
import com.example.smartboon.common.Common
import com.example.smartboon.databinding.FragmentReserveBinding
import com.example.smartboon.model.APIResponse
import com.example.smartboon.remote.Api
import com.example.smartboon.session.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class ReserveFragment : Fragment() {

    private lateinit var session: Session
    private lateinit var apiService: Api

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentReserveBinding>(
            inflater,
            R.layout.fragment_reserve, container, false
        )

        session = Session(activity?.applicationContext!!)
        session.checkLogin()

//        val user: HashMap<String, String> = session.getUserDetails()
//        val userName = user[Session.NAME]
//        val userEmail = user[Session.EMAIL]
//
//        name.text = userName
//        email.text = userEmail
//
//   //   For some reason this does not work in a fragment but works in an activity
//        val user: HashMap<String, String> = session.getUserDetails()
//        val email: String = user[Session.EMAIL]!!
//        logout_email.text = email

        apiService = Common.api

        binding.reserveButton.setOnClickListener {
            orderCreation()
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun orderCreation() {
        makeAnOrder((1..6).random())
    }

    private fun makeAnOrder(open: Int) {
        apiService.order(open)
            .enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    // Server is not available
                    Toast.makeText(activity, "Failed to reach the server, try again later! ${t.message}", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                    if (response.body()!!.error) {

                        // failed to make the order because of some application technical restrictions like it's not a suitable time to order
                        Toast.makeText(activity, "Order creation failure! ${response.body()!!.error_msg}", Toast.LENGTH_LONG).show()

                    } else {
                        // Order creation is successful
                        Toast.makeText(activity, "Ordered created successfully!", Toast.LENGTH_LONG).show()
                        findNavController().navigate(ReserveFragmentDirections.actionReserveFragmentToCancelFragment())
                    }

                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logout -> {
                session.logoutUser()
                true
            }
            else -> NavigationUI.onNavDestinationSelected(
                item,
                view!!.findNavController()
            ) || super.onOptionsItemSelected(item)
        }
    }
}
