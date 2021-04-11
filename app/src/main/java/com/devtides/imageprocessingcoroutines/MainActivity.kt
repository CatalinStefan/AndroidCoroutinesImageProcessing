package com.devtides.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.devtides.imageprocessingcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val IMAGE_URL = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                getOriginalBitmap()
            }

            val originalBitmap: Bitmap = originalDeferred.await()

            val filteredDeferred = coroutineScope.async(Dispatchers.Default) {
                applyFilter(originalBitmap)
            }

            val filterBitmap: Bitmap = filteredDeferred.await()

            loadImage(filterBitmap)
        }
    }

    private fun getOriginalBitmap() = URL(IMAGE_URL).openStream().use {
        BitmapFactory.decodeStream(it)
    }

    private fun applyFilter(originalBitmap: Bitmap) = Filter.apply(originalBitmap)

    private fun loadImage(bitmap: Bitmap) {
        binding.progressBar.visibility = View.GONE
        binding.imageView.setImageBitmap(bitmap)
        binding.imageView.visibility = View.VISIBLE
    }

}
