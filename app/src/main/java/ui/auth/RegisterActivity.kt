package com.example.investai_mobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.investai_mobile.MainActivity
import com.example.investai_mobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    // ViewBinding для доступа к элементам layout
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Кнопка "Продолжить" (регистрация или вход)
        binding.btnRegister.setOnClickListener { registerOrLoginUser() }
    }

    /**
     * Простейшая логика:
     * - если пользователь еще не регистрировался -> регистрируем и сохраняем данные
     * - если уже регистрировался -> проверяем логин/пароль/PIN и даём войти
     *
     * Это НE боевой код, а учебный пример без шифрования.
     */
    private fun registerOrLoginUser() {
        val login = binding.etLogin.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val pinCode = binding.etPin.text.toString()

        // Простые проверки заполнения
        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        if (pinCode.length != 4) {
            Toast.makeText(this, "PIN‑код должен содержать 4 цифры", Toast.LENGTH_SHORT).show()
            return
        }

        // Читаем, зарегистрирован ли уже пользователь
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isRegistered = prefs.getBoolean("is_registered", false)

        if (!isRegistered) {
            // Первая регистрация пользователя
            prefs.edit()
                .putString("login", login)
                .putString("password", password)
                .putString("pin", pinCode)
                .putBoolean("is_registered", true)
                .apply()

            Toast.makeText(this, "Регистрация выполнена", Toast.LENGTH_SHORT).show()
        } else {
            // Простейшая "проверка входа"
            val savedLogin = prefs.getString("login", null)
            val savedPassword = prefs.getString("password", null)
            val savedPin = prefs.getString("pin", null)

            val isLoginOk = savedLogin == login
            val isPasswordOk = savedPassword == password
            val isPinOk = savedPin == pinCode

            if (!isLoginOk || !isPasswordOk || !isPinOk) {
                Toast.makeText(this, "Неверные данные для входа", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Переход на главное окно приложения
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}