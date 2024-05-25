package com.example.offline_translator

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerTranslateFrom: Spinner
    private lateinit var spinnerTranslateTo: Spinner
    private lateinit var buttonTranslate: Button
    private lateinit var textViewTranslationResult: TextView

    private val languages = listOf(
        "English", "French", "Spanish", "German", "Italian", "Arabic",
        "Indonesian", "Vietnamese", "Urdu", "Hindi", "Russian", "Chinese", "Japanese",
        "Korean", "Portuguese", "Turkish", "Dutch", "Swedish"
    )

    private val languageCodes = listOf(
        FirebaseTranslateLanguage.EN,
        FirebaseTranslateLanguage.FR,
        FirebaseTranslateLanguage.ES,
        FirebaseTranslateLanguage.DE,
        FirebaseTranslateLanguage.IT,
        FirebaseTranslateLanguage.AR,
        FirebaseTranslateLanguage.ID,
        FirebaseTranslateLanguage.VI,
        FirebaseTranslateLanguage.UR,
        FirebaseTranslateLanguage.HI,
        FirebaseTranslateLanguage.RU,
        FirebaseTranslateLanguage.ZH,
        FirebaseTranslateLanguage.JA,
        FirebaseTranslateLanguage.KO,
        FirebaseTranslateLanguage.PT,
        FirebaseTranslateLanguage.TR,
        FirebaseTranslateLanguage.NL,
        FirebaseTranslateLanguage.SV
    )

    private lateinit var translator: FirebaseTranslator
    private var selectedFromLanguage: Int = FirebaseTranslateLanguage.EN
    private var selectedToLanguage: Int = FirebaseTranslateLanguage.EN

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        spinnerTranslateFrom = findViewById(R.id.spinnerTranslateFrom)
        spinnerTranslateTo = findViewById(R.id.spinnerTranslateTo)
        buttonTranslate = findViewById(R.id.buttonTranslate)
        textViewTranslationResult = findViewById(R.id.textViewTranslationResult)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTranslateFrom.adapter = adapter
        spinnerTranslateTo.adapter = adapter

        spinnerTranslateFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFromLanguage = languageCodes[position]
                initializeTranslator(selectedFromLanguage, selectedToLanguage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTranslateTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedToLanguage = languageCodes[position]
                initializeTranslator(selectedFromLanguage, selectedToLanguage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        buttonTranslate.setOnClickListener {
            val inputText = "Text to be translated"
            val translationResult = translate(inputText)

            textViewTranslationResult.text = translationResult.toString()
        }

        fetchRemoteConfigValues()
    }

    private fun initializeTranslator(sourceLanguage: Int, targetLanguage: Int) {
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        buttonTranslate.isEnabled = true
    }

    private fun translate(inputText: String): Task<String> {
        val translatedText = translator.translate(inputText)

        return translatedText
    }

    private fun fetchRemoteConfigValues() {
        val defaults = mapOf(
            "key1" to "default_value1",
            "key2" to "default_value2"
        )
        firebaseRemoteConfig.setDefaultsAsync(defaults)
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val value1 = firebaseRemoteConfig.getString("key1")
                    val value2 = firebaseRemoteConfig.getString("key2")
                    // Use the fetched values in your app
                } else {
                    // Fetch failed, use default values or handle the error
                }
            }
    }
}