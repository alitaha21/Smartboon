package com.example.smartboon.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.smartboon.R
import com.example.smartboon.databinding.FragmentCancelBinding
import com.example.smartboon.session.Session

/**
 * A simple [Fragment] subclass.
 */
class CancelFragment : Fragment() {

    private lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCancelBinding>(
            inflater,
            R.layout.fragment_cancel, container, false
        )
        binding.cancelButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(CancelFragmentDirections.actionCancelFragmentToReserveFragment())
        }
        setHasOptionsMenu(true)

        session = Session(activity?.applicationContext!!)
        session.checkLogin()

        return binding.root
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
