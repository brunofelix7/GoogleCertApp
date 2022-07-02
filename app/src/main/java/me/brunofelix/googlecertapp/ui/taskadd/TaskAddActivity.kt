package me.brunofelix.googlecertapp.ui.taskadd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.databinding.ActivityTaskAddBinding

class TaskAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}