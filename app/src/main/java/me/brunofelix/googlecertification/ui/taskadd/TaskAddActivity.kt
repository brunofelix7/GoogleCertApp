package me.brunofelix.googlecertification.ui.taskadd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.brunofelix.googlecertification.R
import me.brunofelix.googlecertification.data.AppDatabase
import me.brunofelix.googlecertification.data.Task
import me.brunofelix.googlecertification.data.TaskRepositoryImpl
import me.brunofelix.googlecertification.databinding.ActivityTaskAddBinding
import me.brunofelix.googlecertification.extension.hideKeyboard
import me.brunofelix.googlecertification.extension.myCustomMask
import me.brunofelix.googlecertification.extension.snackbar
import me.brunofelix.googlecertification.extension.toast
import me.brunofelix.googlecertification.util.AppConstants
import me.brunofelix.googlecertification.util.AppProvider
import me.brunofelix.googlecertification.util.convertToTimestamp
import me.brunofelix.googlecertification.util.getDuration
import java.util.*

class TaskAddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityTaskAddBinding
    private lateinit var viewModel: TaskAddViewModel

    private var date: String? = null
    private var time: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initObjects()
        observeData()
    }

    private fun initUI() {
        setTheme(R.style.ThemeGoogleCertApp)

        binding = ActivityTaskAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.inflateMenu(R.menu.add_menu)
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)

        binding.inputDate.myCustomMask(AppConstants.DATE_MASK)
        binding.inputTime.myCustomMask(AppConstants.TIME_MASK)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            submitForm(it)
        }

        binding.inputDate.setOnClickListener {
            hideKeyboard()
            showDatePicker()
        }

        binding.inputTime.setOnClickListener {
            hideKeyboard()
            showTimePicker()
        }
    }

    private fun initObjects() {
        val db = AppDatabase.getInstance(this)
        val dao = db.taskDao()
        val repository = TaskRepositoryImpl(dao)
        val provider = AppProvider(this)
        val factory = TaskAddViewModelFactory(repository, Dispatchers.IO, provider)

        viewModel = ViewModelProvider(this, factory)[TaskAddViewModel::class.java]
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.liveData.observe(this@TaskAddActivity) { uiState ->
                when (uiState) {
                    is TaskAddUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is TaskAddUiState.Success -> {
                        binding.progressBar.isVisible = false
                        toast(uiState.message)
                        onBackPressed()
                    }
                    is TaskAddUiState.Error -> {
                        binding.progressBar.isVisible = false
                        toast(uiState.message)
                    }
                }
            }
        }
    }

    private fun submitForm(view: View) {
        hideKeyboard()
        clearFormErrors()

        val taskName = binding.inputName.text.toString()
        val taskDate = binding.inputDate.text.toString()
        val taskTime = binding.inputTime.text.toString()

        if (taskName.isEmpty() && taskDate.isEmpty() && taskTime.isEmpty()) {
            binding.inputLayoutName.error = AppConstants.FIELD_NAME_ERROR
            binding.inputLayoutDate.error = AppConstants.FIELD_DATE_ERROR
            binding.inputLayoutTime.error = AppConstants.FIELD_TIME_ERROR
            view.snackbar(AppConstants.FORM_SUBMIT_ERROR)
            return
        }
        if (taskDate.isEmpty() && taskTime.isEmpty()) {
            binding.inputLayoutDate.error = AppConstants.FIELD_DATE_ERROR
            binding.inputLayoutTime.error = AppConstants.FIELD_TIME_ERROR
            return
        }
        if (taskName.isEmpty()) {
            binding.inputLayoutName.error = AppConstants.FIELD_NAME_ERROR
            return
        }
        if (taskDate.isEmpty()) {
            binding.inputLayoutDate.error = AppConstants.FIELD_DATE_ERROR
            return
        }
        if (taskTime.isEmpty()) {
            binding.inputLayoutTime.error = AppConstants.FIELD_TIME_ERROR
            return
        }

        val date = convertToTimestamp("$taskDate $taskTime")
        val duration = getDuration(date)

        viewModel.addTask(Task(name = taskName, date = date, duration = duration))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val pickedDay = if (dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth.toString()
        val pickedMonth = if (month < 10) "0${month.plus(1)}" else month.plus(1).toString()
        val pickedYear = year.toString()

        date = "${pickedMonth}-${pickedDay}-${pickedYear}"

        binding.inputLayoutDate.error = null
        binding.inputDate.setText(date)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val pickedHour = if (hourOfDay < 10) "0${hourOfDay}" else hourOfDay.toString()
        val pickedMinute = if (minute < 10) "0${minute}" else minute.toString()

        time = "${pickedHour}:${pickedMinute}"

        binding.inputLayoutTime.error = null
        binding.inputTime.setText(time)
    }

    private fun showDatePicker() {
        val now = Calendar.getInstance()
        val day = now.get(Calendar.DAY_OF_MONTH)
        val month = now.get(Calendar.MONTH)
        val year = now.get(Calendar.YEAR)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR)
        val minute = now.get(Calendar.MINUTE)

        TimePickerDialog(this, this, hour, minute, false).show()
    }

    private fun clearFormErrors() {
        binding.inputLayoutName.error = null
        binding.inputLayoutDate.error = null
        binding.inputLayoutTime.error = null
    }
}
