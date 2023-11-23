package com.example.workwithview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.workwithview.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard()
                    showProgressBar()
                    loadImageFromUrl(v?.text.toString())
                    return true
                }
                return false
            }
        })
    }

    private fun loadImageFromUrl(url: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val bitmap = downloadBitmap(url)
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    binding.imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    Toast.makeText(
                        this@MainActivity,
                        "Ошибка загрузки изображения $url",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun downloadBitmap(url: String): Bitmap {
        val connection = URL(url).openConnection()
            .apply {
                connect()
            }
        val inputStream = connection.inputStream
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editText.windowToken, 0)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.imageView.visibility = View.GONE

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.imageView.visibility = View.VISIBLE

    }
}