package com.sifat.mydiary

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sifat.mydiary.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {
        lateinit var binding: FragmentCreateBinding

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = FragmentCreateBinding.inflate(inflater, container, false)

            binding.loginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_createFragment_to_loginFragment)
            }
            binding.createAccount.setOnClickListener {
                val email = binding.Username.text.toString().trim()
                val password = binding.password.text.toString().trim()

                if (isEmailValid(email) && isPasswordValid(password)) {
                    signInUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }

            return binding.root
        }

        private fun signInUser(email: String, password: String) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Create account succesfull", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_createFragment_to_loginFragment)
                } else {
                    Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        private fun isPasswordValid(password: String): Boolean {

            //Use easy password
            val passRegex = Regex("^(?=.*[A-Za-z])(?=.*[0-9]).{4,12}$")
            return password.matches(passRegex)
        }
    }
