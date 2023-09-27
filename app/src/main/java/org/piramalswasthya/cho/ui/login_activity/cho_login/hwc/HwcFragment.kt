package org.piramalswasthya.cho.ui.login_activity.cho_login.hwc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.cho.R
import org.piramalswasthya.cho.database.shared_preferences.PreferenceDao
import org.piramalswasthya.cho.databinding.FragmentChoLoginBinding
import org.piramalswasthya.cho.databinding.FragmentHwcBinding
import org.piramalswasthya.cho.ui.login_activity.asha_login.AshaLoginFragmentDirections
import org.piramalswasthya.cho.ui.login_activity.cho_login.ChoLoginFragmentDirections
import org.piramalswasthya.cho.ui.login_activity.cho_login.outreach.OutreachViewModel
import org.piramalswasthya.cho.ui.login_activity.username.UsernameFragmentDirections
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
@AndroidEntryPoint
class HwcFragment constructor(
    private val userName: String,
    private val rememberUsername: Boolean,
    ): Fragment() {

    @Inject
    lateinit var prefDao: PreferenceDao

    private var _binding: FragmentHwcBinding? = null
    private val binding: FragmentHwcBinding
        get() = _binding!!

    private lateinit var viewModel: HwcViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HwcViewModel::class.java)

        _binding = FragmentHwcBinding.inflate(layoutInflater, container, false)
        if(!viewModel.fetchRememberedPassword().isNullOrBlank()) {
            viewModel.fetchRememberedPassword()?.let {
                binding.etPasswordHwc.setText(it)
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pattern = "yyyy-MM-dd'T'HH:mm:ssZ"
        val timeZone = TimeZone.getTimeZone("GMT+0530")
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        formatter.timeZone = timeZone
        val timestamp = formatter.format(Date())

        binding.btnHwcLogin.setOnClickListener {
            viewModel.authUser(
                userName,
                binding.etPasswordHwc.text.toString(),
                "HWC",
                null,
                timestamp,
                null,
                null,
                null,
                null
            )
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state!!) {
                OutreachViewModel.State.SUCCESS -> {
                    if (rememberUsername)
                        viewModel.rememberUser(userName,binding.etPasswordHwc.text.toString())
                    else {
                        viewModel.forgetUser()
                    }
                    findNavController().navigate(
                        ChoLoginFragmentDirections.actionSignInToHomeFromCho()
                    )
                    viewModel.resetState()
                    activity?.finish()
                }

                OutreachViewModel.State.ERROR_SERVER,
                OutreachViewModel.State.ERROR_NETWORK -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_while_logging_in),
                        Toast.LENGTH_LONG
                    ).show()
//                        viewModel.forgetUser()
                    viewModel.resetState()
                }

                else -> {}
            }

        }

        }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
////        binding.btnHwcLogin.setOnClickListener {
////            findNavController().navigate(
////                ChoLoginFragmentDirections.actionSignInToHomeFromCho()
////            )
////        }
//        // TODO: Use the ViewModel
//    }

}