package com.example.unittestdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unittestdemo.adapter.BotAdapter
import com.example.unittestdemo.data.TYPE
import com.example.unittestdemo.data.TextModel
import com.example.unittestdemo.databinding.ActivityMainBinding
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object {
        const val tag_1 = "較人性化的回答"
        const val tag_2 = "30個字內"
        const val tag_3 = "小孩子的口吻"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val model = GenerativeModel(
            modelName = "gemini-1.0-pro",
            apiKey = "AIzaSyC4o7pxTWPSta4G6DRTwmWfrtXtW5cigEs"
        )

        val adapter = BotAdapter()
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.adapter = adapter


        binding.ivTop.setOnClickListener {
            binding.rv.smoothScrollToPosition(0)
        }

        binding.btnClick.setOnClickListener {

            binding.clSendBox.alpha = 0.5f
            binding.clSendBox.isClickable = false

            val userTextModel = TextModel(binding.et.text.toString(), TYPE.USER)
            adapter.addItem(userTextModel)
            binding.rv.smoothScrollToPosition(adapter.itemCount - 1)

            binding.llLoading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                val prompt = binding.et.text.toString() + tag_1 + tag_2 + tag_3
                val response = model.generateContent(prompt)

                withContext(Dispatchers.Main) {
                    binding.et.text?.clear()
                    binding.et.clearFocus()

                    binding.clSendBox.alpha = 1.0f
                    binding.clSendBox.isClickable = true
                }

                val botTextModel = TextModel(response.text!!, TYPE.BOT)
                adapter.addItem(botTextModel)

                binding.llLoading.visibility = View.GONE
                binding.rv.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}