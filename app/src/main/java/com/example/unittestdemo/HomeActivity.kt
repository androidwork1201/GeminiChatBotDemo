package com.example.unittestdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unittestdemo.adapter.ChooseItemAdapter
import com.example.unittestdemo.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), ChooseItemAdapter.OnItemClickListener {

    companion object{
        const val type_1 = "1"
        const val type_2 = "2"
        const val type_3 = "3"

        const val first = "first"
        const val second = "second"
        const val third = "third"
        const val fourth = "fourth"
        const val fiveth = "fiveth"
    }

    private var type: String? = null
    private var firstString: String? = null
    private var secondString: String? = null
    private var thirdString: String? = null

    private lateinit var binding: ActivityHomeBinding

    private val list1 = mutableListOf("較人性化的回答", "較溫柔的回答", "較嚴肅的回答", "較冰冷的回答")
    private val list2 = mutableListOf("長話短說", "30個字內", "30個字內", "30個字內")
    private val list3 = mutableListOf("小孩子的口吻", "成年人的口吻", "老人的口吻", "軍人的口吻")

    private var chooseItemAdapter1: ChooseItemAdapter? = null
    private var chooseItemAdapter2: ChooseItemAdapter? = null
    private var chooseItemAdapter3: ChooseItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type = type_1
        chooseItemAdapter1 = ChooseItemAdapter()
        chooseItemAdapter1?.setOnItemClickListener(this)

        chooseItemAdapter2 = ChooseItemAdapter()
        chooseItemAdapter2?.setOnItemClickListener(this)

        chooseItemAdapter3 = ChooseItemAdapter()
        chooseItemAdapter3?.setOnItemClickListener(this)


        gridItemList()
        layoutChange()
    }

    private fun gridItemList() {
        binding.rv1.layoutManager = GridLayoutManager(this, 2)
        binding.rv1.setHasFixedSize(true)
        binding.rv1.adapter = chooseItemAdapter1

        binding.rv2.layoutManager = GridLayoutManager(this, 2)
        binding.rv2.setHasFixedSize(true)
        binding.rv2.adapter = chooseItemAdapter2

        binding.rv3.layoutManager = GridLayoutManager(this, 2)
        binding.rv3.setHasFixedSize(true)
        binding.rv3.adapter = chooseItemAdapter3

        chooseItemAdapter1?.setData(list1)
        chooseItemAdapter2?.setData(list2)
        chooseItemAdapter3?.setData(list3)
    }


    private fun layoutChange() {
        binding.btnCheckTag1.setOnClickListener {
            type = type_2
            binding.clTag1.visibility = View.GONE
            binding.clTag2.visibility = View.VISIBLE
            animEnter(binding.clTag2)
        }

        binding.btnCheckTag2.setOnClickListener {
            type = type_3
            binding.clTag2.visibility = View.GONE
            binding.clTag3.visibility = View.VISIBLE
            animEnter(binding.clTag3)
        }

        binding.btnCheckTag3.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(first, firstString)
            bundle.putString(second, secondString)
            bundle.putString(third, thirdString)
            bundle.putString(fourth, "非條列式")
            bundle.putString(fiveth, "設身處地")

            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
            finish()
        }
    }


    private fun animEnter(view: ConstraintLayout) {
        val screenWidth = view.resources.displayMetrics.widthPixels.toFloat()
        view.translationX = screenWidth

        val anim = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f)
        anim.duration = 500
        anim.start()

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.VISIBLE
            }
        })
    }

    override fun onItemClick(text: String) {
        when(type) {
            type_1 -> {
                firstString = text
                binding.tvChoose1.text = text
            }
            type_2 -> {
                secondString = text
                binding.tvChoose2.text = text
            }
            type_3 -> {
                thirdString = text
                binding.tvChoose3.text = text
            }
        }
    }
}