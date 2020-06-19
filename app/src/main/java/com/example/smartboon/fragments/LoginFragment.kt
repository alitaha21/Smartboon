package com.example.smartboon.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.smartboon.R
import com.example.smartboon.activities.SmartActivity
import com.example.smartboon.common.Common
import com.example.smartboon.databinding.FragmentLoginBinding
import com.example.smartboon.model.APIResponse
import com.example.smartboon.remote.Api
import com.example.smartboon.session.Session
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var apiService: Api
    private lateinit var session: Session
    private var userId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )
        setHasOptionsMenu(true)

        session = Session(activity?.applicationContext!!)

        // Upon creation we check whether the user is logged in in a session so that
        // he won't have to write his credentials once again
        if (session.isLoggedIn()) {
            val intent = Intent(context, SmartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        apiService = Common.api

        binding.loginButton.setOnClickListener { onUserLogin() }

        return binding.root
    }

    private fun onUserLogin() {
        authenticateUser(email.text.toString(), password.text.toString())
    }

    private fun authenticateUser(email: String, password: String) {
        apiService.login(email, password)
            .enqueue(object : Callback<APIResponse> {

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    // Here the server is not available right now
                    Toast.makeText(activity, "Failed to login. ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    if (response.body()!!.error) {
                        // This means we passed the server but maybe we have wrong credentials
                        Toast.makeText(
                            activity,
                            "Failed to login. ${response.body()!!.error_msg}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // Now we are in hence we create a login session
                        session.createLoginSession(email, password, response.body()!!.user!!)
                        // this is how to retrieve data from the database
                        userId = response.body()!!.user!!.id!!
                        session.userId(userId)
                        // Directing the user to the reserve and cancel fragments
                        val intent = Intent(context, SmartActivity::class.java)
                        startActivity(intent)
                    }
                }
            })
    }


    // AnAn old login function for trial
//    private fun login(binding: FragmentLoginBinding, view: View) {
//
//        val username: EditText = binding.email
//        val password: EditText = binding.password
//
//        if (username.text.toString() == "alitaha21" && password.text.toString() == "12347890") {
//            view.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToReserveFragment())
//        } else {
//            Toast.makeText(activity, "Wrong credentials", Toast.LENGTH_LONG).show()
//        }
//    }


    // Menu stuff
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}
