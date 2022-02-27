package com.example.cookingconverter20

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.cookingconverter20.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val productList = listOf(
        Product("Water", 1000.0),
        Product("Milk", 1030.0),
        Product("Olive oil", 940.0),
        Product("Flour", 700.0),
        Product("Sugar", 900.0),
        Product("Salt", 1200.0)
    )

    private val unitsList = listOf(
        Unit("Liter", 1.0, "vol"),
        Unit("Milliliter", 0.001, "vol"),
        Unit("Kilogram", 1000.0, "wei"),
        Unit("Gram", 1.0, "wei")
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

        val measurementUnitAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            unitsList.map { unit -> unit.name }
        )

        binding.measurementUnit.setAdapter(measurementUnitAdapter)

        val conversionUnitAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            unitsList.map { unit -> unit.name }
        )

        binding.conversionUnit.setAdapter(conversionUnitAdapter)
    }

    private fun calculateResult() {
        val stringChosenProduct = binding.productName.text.toString()
        val stringUnitFrom = binding.measurementUnit.text.toString()
        val stringUnitTo = binding.conversionUnit.text.toString()
        val stringQuantity = binding.quantity.text.toString()

        val unitFrom = unitsList.find { it.name == stringUnitFrom }
        val unitTo = unitsList.find { it.name == stringUnitTo }
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
            result = kotlin.math.ceil(result)
        }

        binding.resultText.text = getString(R.string.amount_in_unit, result.toString())
    }
}