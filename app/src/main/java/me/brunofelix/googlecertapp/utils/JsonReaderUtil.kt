package me.brunofelix.googlecertapp.utils

import android.content.Context
import android.util.Log
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.Task
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object JsonReaderUtil {

    fun getDataFromJson(context: Context): List<Task> {
        val tasksJson = loadJsonArray(context)
        val tasksList = arrayListOf<Task>()

        for (i in 0 until tasksJson.length()) {
            val task = tasksJson.getJSONObject(i)

            val id = task.getLong("id")
            val name = task.getString("name")
            val state = task.getString("state")
            val date = task.getLong("date")

            tasksList.add(Task(id, name, state, date))
        }
        return tasksList
    }

    private fun readJson(context: Context): String {
        val builder = StringBuilder()
        val inputStream = context.resources.openRawResource(R.raw.task_datasource)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?

        try {
            do {
                line = reader.readLine()
                builder.append(line)

                if (line == null) {
                    break
                }
            } while (true)
        } catch (e: IOException) {
            Timber.e(e.message ?: "An error occurred")
        }
        return builder.toString()
    }

    private fun loadJsonArray(context: Context): JSONArray {
        val json = readJson(context)
        val jsonObject = JSONObject(json)
        return jsonObject.getJSONArray("tasks")
    }
}