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
import com.sifat.mydiary.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createFragment)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (isEmailValid(email) && isPasswordValid(password)) {
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(requireContext(), "Login successful: ${user?.email}", Toast.LENGTH_SHORT).show()

                // Navigate to home fragment
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                // Clear input fields after successful login
                binding.username.text.clear()
                binding.password.text.clear()
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
