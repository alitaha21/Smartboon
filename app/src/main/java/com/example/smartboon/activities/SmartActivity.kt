package com.example.smartboon.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.smartboon.R
import com.example.smartboon.databinding.ActivitySmartBinding

class SmartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivitySmartBinding>(this,
                R.layout.activity_smart
            )
    }
}
