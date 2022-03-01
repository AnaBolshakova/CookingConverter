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
    private val unitsList = listOf(
        Unit("Liter", 1.0, "vol"),
        Unit("Milliliter", 0.001, "vol"),
        Unit("Kilogram", 1000.0, "wei"),
        Unit("200 ml glass", 0.2, "vol"),
        Unit("Tablespoon", 0.018, "vol"),
        Unit("Teaspoon", 0.005, "vol"),
        Unit("Pound", 453.592, "wei"),
        Unit("Ounce", 28.3495, "wei")
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

