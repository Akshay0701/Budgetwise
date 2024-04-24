package com.example.budgetwise

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.DecimalFormat
import kotlin.math.roundToInt

class Homepage : AppCompatActivity() {

    /**
     *  Budget values for each category
     *  currently i set static ratio of budget for each category,
     *  but we can make it dynamic according to user input
     */
    var totalBudget: Int = 10000;
    var totalSpendBudget: Int = 0;

    var foodRatio = 0.05
    var foodSpend = 0

    var shoppingRatio = 0.15
    var shoppingSpend = 0

    var transportationRatio = 0.05
    var transportationSpend = 0

    var educationRatio = 0.2
    var educationSpend = 0

    var groceriesRatio = 0.05
    var groceriesSpend = 0

    var housingRatio = 0.5
    var housingSpend = 0

    val animationDuration = 1000L

    // TextViews
    lateinit var totalBudgetTxt: TextView
    lateinit var totalSpendTxt: TextView
    lateinit var remainingTxt: TextView
    lateinit var foodSpendTxt: TextView
    lateinit var foodOf: TextView
    lateinit var foodLeftTxt: TextView
    lateinit var shopSpendTxt: TextView
    lateinit var shopOf: TextView
    lateinit var shopLeftTxt: TextView
    lateinit var transportSpendTxt: TextView
    lateinit var transportOf: TextView
    lateinit var transportLeftTxt: TextView
    lateinit var educationSpendTxt: TextView
    lateinit var educationOf: TextView
    lateinit var educationLeftTxt: TextView
    lateinit var groceriesSpendTxt: TextView
    lateinit var groceriesOf: TextView
    lateinit var groceriesLeftTxt: TextView
    lateinit var housingSpendTxt: TextView
    lateinit var housingOf: TextView
    lateinit var housingLeftTxt: TextView

    // ProgressBars
    lateinit var progressBarTotalBudget: ProgressBar
    lateinit var foodProgressBar: ProgressBar
    lateinit var shopProgressBar: ProgressBar
    lateinit var transportProgressBar: ProgressBar
    lateinit var educationProgressBar: ProgressBar
    lateinit var groceriesProgressBar: ProgressBar
    lateinit var housingProgressBar: ProgressBar


    // SharedPreferences file name
    private val PREFS_FILENAME = "com.example.budgetwise.prefs"

    /**
     *  Each category's budget value will be saved in PREFS_FILENAME,
     *  later we can retrive the last updated value,
     *  this method can be further improve by using SQLite database instead of shared preference
     */
    private val KEY_TOTAL_SPEND = "total_spend"
    private val KEY_FOOD_SPEND = "food_spend"
    private val KEY_SHOPPING_SPEND = "shopping_spend"
    private val KEY_TRANSPORTATION_SPEND = "transportation_spend"
    private val KEY_EDUCATION_SPEND = "education_spend"
    private val KEY_GROCERIES_SPEND = "groceries_spend"
    private val KEY_HOUSING_SPEND = "housing_spend"

    // SharedPreferences instance
    private lateinit var prefs: SharedPreferences


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        // Initialize TextViews
        totalBudgetTxt = findViewById(R.id.totalBudgetTxt)
        totalSpendTxt = findViewById(R.id.totalSpendTxt)
        remainingTxt = findViewById(R.id.remainingTxt)
        foodSpendTxt = findViewById(R.id.foodSpendTxt)
        foodOf = findViewById(R.id.foodOf)
        foodLeftTxt = findViewById(R.id.foodLeftTxt)
        shopSpendTxt = findViewById(R.id.shopSpendTxt)
        shopOf = findViewById(R.id.shopOf)
        shopLeftTxt = findViewById(R.id.shopLeftTxt)
        transportSpendTxt = findViewById(R.id.transportSpendTxt)
        transportOf = findViewById(R.id.transportOf)
        transportLeftTxt = findViewById(R.id.transportLeftTxt)
        educationSpendTxt = findViewById(R.id.educationSpendTxt)
        educationOf = findViewById(R.id.educationOf)
        educationLeftTxt = findViewById(R.id.educationLeftTxt)
        groceriesSpendTxt = findViewById(R.id.groceriesSpendTxt)
        groceriesOf = findViewById(R.id.groceriesOf)
        groceriesLeftTxt = findViewById(R.id.groceriesLeftTxt)
        housingSpendTxt = findViewById(R.id.housingSpendTxt)
        housingOf = findViewById(R.id.housingOf)
        housingLeftTxt = findViewById(R.id.housingLeftTxt)

        // Initialize ProgressBars
        progressBarTotalBudget = findViewById(R.id.progressBarTotalBudget)
        foodProgressBar = findViewById(R.id.foodProgressBar)
        shopProgressBar = findViewById(R.id.shopProgressBar)
        transportProgressBar = findViewById(R.id.transportProgressBar)
        educationProgressBar = findViewById(R.id.educationProgressBar)
        groceriesProgressBar = findViewById(R.id.groceriesProgressBar)
        housingProgressBar = findViewById(R.id.housingProgressBar)

        // get saved values
        totalSpendBudget = prefs.getInt(KEY_TOTAL_SPEND, 0)
        foodSpend = prefs.getInt(KEY_FOOD_SPEND, 0)
        shoppingSpend = prefs.getInt(KEY_SHOPPING_SPEND, 0)
        transportationSpend = prefs.getInt(KEY_TRANSPORTATION_SPEND, 0)
        educationSpend = prefs.getInt(KEY_EDUCATION_SPEND, 0)
        groceriesSpend = prefs.getInt(KEY_GROCERIES_SPEND, 0)
        housingSpend = prefs.getInt(KEY_HOUSING_SPEND, 0)

        // Set text and progress
        updateTotalSpend(0)
        updateFoodCategory(0)
        updateShoppingCategory(0)
        updateTransportationCategory(0)
        updateEducationCategory(0)
        updateGroceriesCategory(0)
        updateHousingCategory(0)


        findViewById<ImageView>(R.id.fab_button).setOnClickListener {
            showDialogBox()
        }
    }



    private fun showDialogBox() {
        // Setting up view for dialog box
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)
        val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)

        buttonOk.setOnClickListener {
            addTransaction(dialogView)
            // Dismiss the dialog
            alertDialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun addTransaction(dialogView: View) {
        val radioGroupCategories = dialogView.findViewById<RadioGroup>(R.id.radioGroupCategories)
        val checkedRadioButtonId = radioGroupCategories.checkedRadioButtonId

        if (checkedRadioButtonId != -1) {
            // At least one RadioButton is checked
            val selectedRadioButton = dialogView.findViewById<RadioButton>(checkedRadioButtonId)
            val selectedCategory = selectedRadioButton.text.toString()
            val amount = Integer.parseInt(dialogView.findViewById<EditText>(R.id.editTextAmount).text.toString())
            Toast.makeText(this, "${selectedCategory} + $amount", Toast.LENGTH_SHORT).show()
            // Handle the selected category and amount
            when (selectedCategory) {
                "Housing" -> {
                    updateHousingCategory(amount)
                    updateTotalSpend(amount)
                }

                "Food" -> {
                    updateFoodCategory(amount)
                    updateTotalSpend(amount)
                }

                "Transportation" -> {
                    updateTransportationCategory(amount)
                    updateTotalSpend(amount)
                }

                "Shopping" -> {
                    updateShoppingCategory(amount)
                    updateTotalSpend(amount)
                }

                "Groceries" -> {
                    updateGroceriesCategory(amount)
                    updateTotalSpend(amount)
                }

                "Education" -> {
                    updateEducationCategory(amount)
                    updateTotalSpend(amount)
                }
                else -> {
                    Toast.makeText(this, "Unknown category selected", Toast.LENGTH_SHORT).show()
                }
            }
            // Save updated spend amounts to SharedPreferences
            with(prefs.edit()) {
                putInt(KEY_TOTAL_SPEND, totalSpendBudget)
                putInt(KEY_FOOD_SPEND, foodSpend)
                putInt(KEY_SHOPPING_SPEND, shoppingSpend)
                putInt(KEY_TRANSPORTATION_SPEND, transportationSpend)
                putInt(KEY_EDUCATION_SPEND, educationSpend)
                putInt(KEY_GROCERIES_SPEND, groceriesSpend)
                putInt(KEY_HOUSING_SPEND, housingSpend)
                apply()
            }
        } else {
            // No RadioButton is checked, handle this case
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
        }
    }

    fun addCommasToNumber(numberString: String): String {
        val number = numberString.substring(1).toInt() // Remove the dollar sign and convert to int
        val formatter = DecimalFormat("#,###")
        return "$${formatter.format(number)}"
    }

    private fun updateTotalSpend(spend: Int) {
        val oldProgress = totalSpendBudget

        totalSpendBudget += spend
        totalBudgetTxt.text = addCommasToNumber("$$totalBudget")
        totalSpendTxt.text = addCommasToNumber("$$totalSpendBudget")
        remainingTxt.text = addCommasToNumber("$${totalBudget - totalSpendBudget}")

        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(progressBarTotalBudget, "progress", oldProgress, totalSpendBudget)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            progressBarTotalBudget.progress = progress
        }

        // Start the animation
        animator.start()
    }

    // Function to update the groceries category
    private fun updateGroceriesCategory(spend: Int) {
        val oldProgress = groceriesSpend

        groceriesSpend += spend
        groceriesSpendTxt.text = "$$groceriesSpend"
        groceriesOf.text = "of ${(totalBudget * groceriesRatio).roundToInt()}"
        groceriesLeftTxt.text = "$${(totalBudget * groceriesRatio - groceriesSpend).roundToInt()} left"
        groceriesProgressBar.max = (totalBudget * groceriesRatio).roundToInt()

        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(groceriesProgressBar, "progress", oldProgress, groceriesSpend)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            groceriesProgressBar.progress = progress
        }

        // Start the animation
        animator.start()
    }

    // Function to update the food category
    private fun updateFoodCategory(spend: Int) {
        val oldProgress = foodSpend

        foodSpend += spend
        foodSpendTxt.text = "$$foodSpend"
        foodOf.text = "of ${(totalBudget * foodRatio).roundToInt()}"
        foodLeftTxt.text = "$${(totalBudget * foodRatio - foodSpend).roundToInt()} left"
        foodProgressBar.max = (totalBudget * foodRatio).roundToInt()


        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(foodProgressBar, "progress", oldProgress, foodSpend)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            foodProgressBar.progress = progress
        }

        // Start the animation
        animator.start()
    }

    // Function to update the shopping category
    private fun updateShoppingCategory(spend: Int) {
        val oldProgress = shoppingSpend

        shoppingSpend += spend
        shopSpendTxt.text = "$$shoppingSpend"
        shopOf.text = "of ${(totalBudget * shoppingRatio).roundToInt()}"
        shopLeftTxt.text = "$${(totalBudget * shoppingRatio - shoppingSpend).roundToInt()} left"
        shopProgressBar.max = (totalBudget * shoppingRatio).roundToInt()

        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(shopProgressBar, "progress", oldProgress, shoppingSpend)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            shopProgressBar.progress = progress
        }

        // Start the animation
        animator.start()
    }

    // Function to update the transportation category
    private fun updateTransportationCategory(spend: Int) {
        val oldProgress = transportationSpend

        transportationSpend += spend
        transportSpendTxt.text = "$$transportationSpend"
        transportOf.text = "of ${(totalBudget * transportationRatio).roundToInt()}"
        transportLeftTxt.text = "$${(totalBudget * transportationRatio - transportationSpend).roundToInt()} left"
        transportProgressBar.max = (totalBudget * transportationRatio).roundToInt()


        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(transportProgressBar, "progress", oldProgress, transportationSpend)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            transportProgressBar.progress = progress
        }

        // Start the animation
        animator.start()
    }

    // Function to update the education category
    private fun updateEducationCategory(spend: Int) {
        val oldProgress = educationSpend

        educationSpend += spend
        educationSpendTxt.text = "$$educationSpend"
        educationOf.text = "of ${(totalBudget * educationRatio).roundToInt()}"
        educationLeftTxt.text = "$${(totalBudget * educationRatio - educationSpend).roundToInt()} left"
        educationProgressBar.max = (totalBudget * educationRatio).roundToInt()

        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(educationProgressBar, "progress", oldProgress, educationSpend)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            educationProgressBar.progress = progress
        }

        // Start the animation
        animator.start()
    }

    // Function to update the housing category
    private fun updateHousingCategory(spend: Int) {
        val oldProgress = housingSpend

        housingSpend += spend
        housingSpendTxt.text = "$$housingSpend"
        housingOf.text = "of ${(totalBudget * housingRatio).roundToInt()}"
        housingLeftTxt.text = "$${(totalBudget * housingRatio - housingSpend).roundToInt()} left"
        housingProgressBar.max = (totalBudget * housingRatio).roundToInt()


        // Create an ObjectAnimator to animate the progress
        val animator = ObjectAnimator.ofInt(housingProgressBar, "progress", oldProgress, housingSpend)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            housingProgressBar.progress = progress
        }

        // Start the animation
        animator.start()
    }
}