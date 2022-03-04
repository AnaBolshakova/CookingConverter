package com.example.cookingconverter20


import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.cookingconverter20.databinding.ActivityMainBinding
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val productList = listOf(
        Product("Water", 1000.0),
        Product("Milk", 1030.0),
        Product("Olive oil", 940.0),
        Product("Flour", 700.0),
        Product("Sugar", 900.0),
        Product("Salt", 690.0),
        Product("Vodka", 940.0),
        Product("Peas", 920.0),
        Product("Cocoa powder", 650.0),
        Product("Kefir", 1030.0),
        Product("Coffee cereal", 430.0),
        Product("Corn", 720.0),
        Product("Mayonnaise", 940.0),
        Product("Sunflower oil", 960.0),
        Product("Melted butter", 910.0),
        Product("Honey", 1500.0),
        Product("Almond", 640.0),
        Product("Condensed milk", 1300.0),
        Product("Beer", 1030.0),
        Product("Sugar powder", 760.0),
        Product("Sour creme", 1050.0),
        Product("Tomato paste", 1080.0),
        Product("Essence", 1000.0),
        Product("Beans", 840.0),
        Product("Jam", 1330.0),
        Product("Cherry", 800.0),
        Product("Rice", 780.0),
        Product("Strawberry", 600.0),
        Product("Coconut sprinkle", 350.0),
        Product("Raisin", 760.0),
        Product("Buckwheat", 710.0),
        Product("Semolina", 800.0),
        Product("Pearl barley", 910.0),
        Product("Millet", 840.0),
        Product("Wheat", 280.0),
        Product("Sunflower seed", 360.0),
        Product("Soda", 690.0),
        Product("Blueberry", 720.0),
        Product("Chips", 90.0)
    )

    // add the name of the unit in result
    private val unitList = listOf(
        Unit("Liter", 1.0, "vol", "l"),
        Unit("Milliliter", 0.001, "vol", "ml"),
        Unit("Kilogram", 1000.0, "wei", "kg"),
        Unit("200 ml glass", 0.2, "vol", "gl"),
        Unit("Tablespoon", 0.018, "vol", "tablespoons"),
        Unit("Teaspoon", 0.005, "vol", "teaspoons"),
        Unit("Pound", 453.592, "wei", "lb"),
        Unit("Ounce", 28.3495, "wei", "ounce"),
        Unit("Gram", 1.0, "wei", "gr")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertButton.setOnClickListener { calculateResult() }

        val productAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            productList.map { product -> product.name }
        )
        binding.productName.setAdapter(productAdapter)

        val conversionUnitAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            unitList.map { unit -> unit.name }
        )

        binding.conversionUnit.setAdapter(conversionUnitAdapter)

        val measurementUnitAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            unitList.map { unit -> unit.name }
        )
        binding.measurementUnit.setAdapter(measurementUnitAdapter)

        binding.measurementValueOptions.setOnCheckedChangeListener { _, _ ->
            when (binding.measurementValueOptions.checkedRadioButtonId) {
                R.id.weight_option -> {
                    val weightUnits = unitList.filter { it.unitType == "wei" }
                    val measurementUnitAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        weightUnits.map { unit -> unit.name }
                    )
                    binding.measurementUnit.setAdapter(measurementUnitAdapter)
                }
                R.id.volume_option -> {
                    val volumeUnits = unitList.filter { it.unitType == "vol" }
                    val measurementUnitAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        volumeUnits.map { unit -> unit.name }
                    )
                    binding.measurementUnit.setAdapter(measurementUnitAdapter)
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun calculateResult() {
        val stringChosenProduct = binding.productName.text.toString()
        val stringUnitFrom = binding.measurementUnit.text.toString()
        val stringUnitTo = binding.conversionUnit.text.toString()
        val stringQuantity = binding.quantity.text.toString()

        val unitInResult = unitList.find { it.name == stringUnitTo }
        val unitFrom = unitList.find { it.name == stringUnitFrom }
        val unitTo = unitList.find { it.name == stringUnitTo }
        val chosenProduct = productList.find { it.name == stringChosenProduct }
        val quantity = stringQuantity.toDoubleOrNull()
        var result: Double

        when {
            quantity == null -> {
                binding.resultText.text = getString(R.string.input_quantity)
                return
            }
            unitFrom == null || unitTo == null || chosenProduct == null -> {
                binding.resultText.text = getString(R.string.input_units)
                return
            }
            unitFrom.unitType == unitTo.unitType -> {
                result = (unitFrom.factor / unitTo.factor) * quantity
            }
            unitFrom.unitType == "wei" && unitTo.unitType == "vol" -> {
                result = ((unitFrom.factor / chosenProduct.density) * quantity) / unitTo.factor
            }
            unitFrom.unitType == "vol" && unitTo.unitType == "wei" -> {
                result = ((chosenProduct.density * unitFrom.factor) * quantity) / unitTo.factor
            }
            else -> {
                binding.resultText.text = getString(R.string.enter_input)
                return
            }
        }


        if (binding.roundUp.isChecked) {
            result = round(result * 10.0) / 10.0
        }

        binding.resultText.text =
            getString(
                R.string.amount_in_unit,
                result.toString(),
                unitInResult?.unitInResult.toString()
            )
    }

}


