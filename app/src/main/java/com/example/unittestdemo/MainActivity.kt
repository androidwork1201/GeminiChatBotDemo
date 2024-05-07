package com.example.unittestdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unittestdemo.adapter.BotAdapter
import com.example.unittestdemo.data.TYPE
import com.example.unittestdemo.data.TextModel
import com.example.unittestdemo.databinding.ActivityMainBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Candidate
import com.google.ai.client.generativeai.type.FinishReason
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), BotAdapter.OnHelpChooseListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BotAdapter
    private lateinit var model: GenerativeModel
    private var tag1: String? = ""
    private var tag2: String? = ""
    private var tag3: String? = ""
    private var tag4: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getBundle = intent?.getBundleExtra("bundle")
        tag1 = getBundle?.getString(HomeActivity.first)
        tag2 = getBundle?.getString(HomeActivity.second)
        tag3 = getBundle?.getString(HomeActivity.third)
        tag4 = getBundle?.getString(HomeActivity.fourth)

        val config = generationConfig {
            temperature = 0.8f
            topK = 3
            topP = 0.95f
            maxOutputTokens = 300
        }
        model = GenerativeModel(
            modelName = "gemini-1.0-pro",
            apiKey = "AIzaSyC4o7pxTWPSta4G6DRTwmWfrtXtW5cigEs",
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE)
            ),
            generationConfig = config,
        )

        adapter = BotAdapter()
        adapter.setOnHelpChooseListener(this)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.adapter = adapter


        binding.ivTop.setOnClickListener {
            binding.rv.smoothScrollToPosition(0)
        }

        binding.btnClick.setOnClickListener {

            binding.clSendBox.alpha = 0.5f
            binding.clSendBox.isClickable = false

            val userTextModel = TextModel(binding.et.text.toString(), TYPE.USER, false)
            adapter.addItem(textModel = userTextModel)
            binding.rv.smoothScrollToPosition(adapter.itemCount - 1)

            val botLoadingTextModel = TextModel("● ● ●", TYPE.BOT, false)
            adapter.addItem(textModel = botLoadingTextModel)

            CoroutineScope(Dispatchers.Main).launch {

                try {
                    val prompt = binding.et.text.toString() + " " + tag1 + tag2 + tag3 + tag4
                    val response = model.generateContent(prompt)

                    adapter.deleteItem(adapter.itemCount-1)
                    delay(100)
                    val botTextModel = TextModel(response.text, TYPE.BOT, false)
                    adapter.addItem(botTextModel)


                } catch (e: Exception) {
                    adapter.deleteItem(adapter.itemCount-1)
                    delay(100)
                    val botTextModel = TextModel("請重新輸入", TYPE.BOT, true)
                    adapter.addItem(botTextModel)
                }


                binding.et.text?.clear()
                binding.et.clearFocus()

                binding.clSendBox.alpha = 1.0f
                binding.clSendBox.isClickable = true

                binding.llLoading.visibility = View.GONE
                binding.rv.scrollToPosition(adapter.itemCount - 1)

            }
        }
    }

    override fun onClickHelpItem(text: String) {
        val userTextModel = TextModel(text, TYPE.USER, false)
        adapter.addItem(userTextModel)
        binding.rv.smoothScrollToPosition(adapter.itemCount - 1)

        val botLoadingTextModel = TextModel("● ● ●", TYPE.BOT, false)
        adapter.addItem(textModel = botLoadingTextModel)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val prompt = binding.et.text.toString() + " " + tag1 + tag2 + tag3 + tag4
                val response = model.generateContent(prompt)

                adapter.deleteItem(adapter.itemCount - 1)
                delay(100)
                val botTextModel = TextModel(response.text, TYPE.BOT, false)
                adapter.addItem(botTextModel)


            } catch (e: Exception) {
                adapter.deleteItem(adapter.itemCount-1)
                delay(100)
                val botTextModel = TextModel("請重新輸入", TYPE.BOT, true)
                adapter.addItem(botTextModel)
            }

            binding.et.text?.clear()
            binding.et.clearFocus()

            binding.clSendBox.alpha = 1.0f
            binding.clSendBox.isClickable = true

            binding.llLoading.visibility = View.GONE
            binding.rv.scrollToPosition(adapter.itemCount - 1)

        }
    }
}