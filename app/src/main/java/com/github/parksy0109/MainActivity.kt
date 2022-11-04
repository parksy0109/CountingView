package com.github.parksy0109

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.github.parksy0109.data.toNumberData
import com.github.parksy0109.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val counterViewModel: CounterViewModel by viewModels()
    private lateinit var increaseHandler: Handler
    private lateinit var increaseRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        increaseHandler = Handler(Looper.getMainLooper())
        increaseRunnable = Runnable {
            counterViewModel.increaseCount()
            increaseHandler.postDelayed(increaseRunnable, counterViewModel.increaseSpeed)
        }

        binding.rlInitialize.setOnClickListener {
            hideKeyboard()
            counterViewModel.initializeCount()
        }

        binding.etSpeed.apply {
            setText(counterViewModel.increaseSpeed.toString())
            addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        if(p0.isNotEmpty() && p0.isNotBlank()){
                            counterViewModel.increaseSpeed = it.toString().toLong()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }

        binding.rlCounting.apply {
            setOnLongClickListener {
                hideKeyboard()
                increaseHandler.post(increaseRunnable)
                false
            }
            setOnTouchListener { _, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        counterViewModel.increaseCount()
                    }
                    MotionEvent.ACTION_UP -> {
                        increaseHandler.removeCallbacks(increaseRunnable)
                    }
                    else -> {
                        performClick()
                    }
                }
                false
            }
        }


        counterViewModel.currentCount.observe(this) {
            if(it == 999) {
                Toast.makeText(this, "카운터 최대 값에 도달했습니다.",Toast.LENGTH_SHORT).show()
            }
            val toNumberData = it.toNumberData()
            binding.tvThousands.text = toNumberData.thousands.toString()
            binding.tvHundreds.text = toNumberData.hundreds.toString()
            binding.tvTens.text = toNumberData.tens.toString()
            binding.tvUnits.text = toNumberData.units.toString()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }
}