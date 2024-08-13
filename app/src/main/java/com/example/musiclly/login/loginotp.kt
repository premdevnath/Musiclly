package com.example.musiclly.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.musiclly.AndroidUtil
import com.example.musiclly.MainActivity
import com.example.musiclly.databinding.ActivityLoginotpBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider.getCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


/*
class loginotp : AppCompatActivity() {
    private lateinit var binding: ActivityLoginotpBinding
    private lateinit var phoneNumber: String
    private var timeoutSeconds: Long = 60
    private lateinit var verificationCode: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityLoginotpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.extras?.getString("phone") ?: ""

        sendOtp(phoneNumber, false)

        binding.loginNextBtn.setOnClickListener {
            val enteredOtp = binding.loginOtp.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp)
            signIn(credential)
        }

        binding.resendOtpTextview.setOnClickListener {
            sendOtp(phoneNumber, true)
        }
    }

    private fun sendOtp(phoneNumber: String, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    AndroidUtil.showToast(applicationContext, "OTP verification failed")
                    setInProgress(false)
                }

                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    AndroidUtil.showToast(applicationContext, "OTP sent successfully")
                    setInProgress(false)
                }
            })
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build())
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.loginNextBtn.visibility = View.GONE
        } else {
            binding.loginProgressBar.visibility = View.GONE
            binding.loginNextBtn.visibility = View.VISIBLE
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this@loginotp, MainActivity::class.java).apply {
                    putExtra("phone", phoneNumber)
                }
                startActivity(intent)
            } else {
                AndroidUtil.showToast(applicationContext, "OTP verification failed")
            }
        }
    }

    private fun startResendTimer() {
        binding.resendOtpTextview.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                runOnUiThread {
                    binding.resendOtpTextview.text = "Resend OTP in $timeoutSeconds seconds"
                    if (timeoutSeconds <= 0) {
                        timeoutSeconds = 60
                        timer.cancel()
                        binding.resendOtpTextview.isEnabled = true
                    }
                }
            }
        }, 0, 1000)
    }
}


 */
class loginotp : AppCompatActivity() {

    private lateinit var binding: ActivityLoginotpBinding
    private lateinit var phoneNumber: String
    private var timeoutSeconds: Long = 60
    private lateinit var verificationCode: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private val mAuth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginotpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.extras?.getString("phone") ?: ""

        sendOtp(phoneNumber, false)

        binding.loginNextBtn.setOnClickListener {
            val enteredOtp = binding.loginOtp.text.toString()
            val credential = getCredential(verificationCode, enteredOtp)
            signIn(credential)
        }

        binding.resendOtpTextview.setOnClickListener {
            sendOtp(phoneNumber, true)
        }
    }

    private fun sendOtp(phoneNumber: String, isResend: Boolean) {
        timeoutSeconds = 60  // Reset the timeout
        startResendTimer()
        setInProgress(true)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    AndroidUtil.showToast(applicationContext, "OTP verification failed: ${e.message}")
                    setInProgress(false)
                }

                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    AndroidUtil.showToast(applicationContext, "OTP sent successfully")
                    setInProgress(false)
                }
            })
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendingToken).build())
        } else {
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.loginProgressBar.visibility = View.VISIBLE
            binding.loginNextBtn.visibility = View.GONE
        } else {
            binding.loginProgressBar.visibility = View.GONE
            binding.loginNextBtn.visibility = View.VISIBLE
        }
    }

    private fun signIn(phoneAuthCredential: AuthCredential) {
        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this@loginotp, MainActivity::class.java).apply {
                    putExtra("phone", phoneNumber)
                }
                startActivity(intent)
                finish()
            } else {
                AndroidUtil.showToast(applicationContext, "OTP verification failed")
            }
        }
    }

    private fun startResendTimer() {
        binding.resendOtpTextview.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                runOnUiThread {
                    binding.resendOtpTextview.text = "Resend OTP in $timeoutSeconds seconds"
                    if (timeoutSeconds <= 0) {
                        timeoutSeconds = 60
                        timer.cancel()
                        binding.resendOtpTextview.isEnabled = true
                    }
                }
            }
        }, 0, 1000)
    }
}