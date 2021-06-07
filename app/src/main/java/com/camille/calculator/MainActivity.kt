package com.camille.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    // лист с R.id для всех кнопок
    val buttonsIds = listOf(
        R.id.button0,
        R.id.button1,
        R.id.button2,
        R.id.button3,
        R.id.button4,
        R.id.button5,
        R.id.button6,
        R.id.button7,
        R.id.button8,
        R.id.button9,
    )

    // Объявление переменных, которые позволят работать с визуальными объектами (View)
    lateinit var text: TextView
    lateinit var buttons: List<Button>
    lateinit var buttonC: Button
    lateinit var buttonEqual: Button
    lateinit var buttonAddition: Button
    lateinit var buttonSubtraction: Button
    lateinit var buttonMultiplication: Button
    lateinit var buttonDivision: Button

    // Инициализация переменных для работа калькулятора
    // В выражении a + b
    var first: Long = 0 // это "a"
    var second: Long? = null // это "b"
    var action: MathAction? = null // это "+"

    // Метод, который для переданной аргументом кнопки
    // устанавливает цвет, что ее нажали
    private fun setColorPressed(button: Button) {
        button.setBackgroundColor(getColor(R.color.teal_200))
    }
    // Метод, который для переданной аргументов кнопки
    // устанавливает цвет, что был изначально
    private fun setColorUnpressed(button: Button?) {
        button?.setBackgroundColor(getColor(R.color.purple_500))
    }
    // Метод, который возвращает все параметры калькулятора к начальным
    private fun backToInitials() {
        first = 0
        second = null
        setColorUnpressed(when (action) {
            MathAction.PLUS -> buttonAddition
            MathAction.MINUS -> buttonSubtraction
            MathAction.DIVIDE -> buttonDivision
            MathAction.MULTIPLY -> buttonMultiplication
            else -> null
        })
        action = null
    }

    /**
     * Метод, который запускается при нажатии кнопки на циферблате
     * num — цифра нажатой кнопки
     */
    private fun onNumButtonClick(num: Int) {
        if (action == null) {
            first = first*10 + num
            text.text = first.toString()
        } else {
            second = (second ?: 0) *10 + num
            text.text = second.toString()
        }
    }

    /**
     * Метод, который запускается при нажатии кнопки +, -, / или *
     */
    private fun onActionButtonClick(action: MathAction) {
        if (second != null) { // случай, когда неявно
            // подразумевалось нажать кнопку "равно",
            // а потом продолжить действия
            onEqualButtonClick()
        }
        when (action) {
            MathAction.PLUS -> onAdditionButtonClick()
            MathAction.MINUS -> onSubtractionButtonClick()
            MathAction.MULTIPLY -> onMultiplicationButtonClick()
            MathAction.DIVIDE -> onDivisionButtonClick()
        }
    }
    private fun onAdditionButtonClick() {
        action = MathAction.PLUS
        setColorPressed(buttonAddition)
    }
    private fun onSubtractionButtonClick() {
        action = MathAction.MINUS
        setColorPressed(buttonSubtraction)
    }
    private fun onMultiplicationButtonClick() {
        action = MathAction.MULTIPLY
        setColorPressed(buttonMultiplication)
    }
    private fun onDivisionButtonClick() {
        action = MathAction.DIVIDE
        setColorPressed(buttonDivision)
    }
    private fun onEqualButtonClick() {
        if (action == null || second == null)
            // Я не могу что-то посчитать,
            // если нет действия либо второй переменной
            return
        text.text = when (action) {
            MathAction.PLUS -> (first+second!!) // складываем числа, если действие — сложение
            MathAction.MINUS -> (first-second!!) // вычитаем, если вычитание
            MathAction.MULTIPLY -> (first*second!!) // умножение
            MathAction.DIVIDE -> (
                if (second!! != 0L) first/second!! // проверка, если происходит деление на ноль
                else getString(R.string.divisionOnZeroError) // текст о том, что делить на ноль нельзя
            )
            else -> 0
        }.toString()
        backToInitials() // сбрасываем все цвета и переменные
        first = try { // пробуем привести посчитанный на экране текст к числу
            text.text.toString().toLong()
        } catch (e: NumberFormatException) {
            0 // если не получилось привести к числу
              // (когда мы выводим текст с ошибкой),
              // оставляем ноль как значение по умолчанию
        }
    }
    private fun onCButtonClick() {
        backToInitials()
        text.text = first.toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Находим объекты на activity_main.xml с помощью findViewById
         */
        text = findViewById(R.id.textView)
        buttonC = findViewById(R.id.buttonC)
        buttonEqual = findViewById(R.id.buttonEqual)
        buttonAddition = findViewById(R.id.buttonAddition)
        buttonSubtraction = findViewById(R.id.buttonSubtraction)
        buttonMultiplication = findViewById(R.id.buttonMultiplication)
        buttonDivision = findViewById(R.id.buttonDivision)
        /**
         *   кнопки с цифрами находятся в листе,
         *   а id достанутся из отдельного листа с их R.id,
         *   объявленного ранее
         */
        buttons = List(10, { findViewById(buttonsIds[it]) })

        // навешиваем действия по нажатию кнопок с цифрами
        for (i in 0..9) { buttons[i].setOnClickListener { onNumButtonClick(i) } }

        // действия по нажатию кнопок с математическими действиями
        buttonAddition.setOnClickListener { onActionButtonClick(MathAction.PLUS) }
        buttonSubtraction.setOnClickListener { onActionButtonClick(MathAction.MINUS) }
        buttonMultiplication.setOnClickListener { onActionButtonClick(MathAction.MULTIPLY) }
        buttonDivision.setOnClickListener { onActionButtonClick(MathAction.DIVIDE) }

        // действие по нажатию кнопки "равно"
        buttonEqual.setOnClickListener { onEqualButtonClick() }

        // действие по нажатию кнопки "С"
        buttonC.setOnClickListener { onCButtonClick() }

    }
}


