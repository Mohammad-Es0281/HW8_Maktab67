package ir.es.mohammad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.children
import ir.es.mohammad.databinding.ActivityMainBinding
import ir.es.mohammad.databinding.ActivityMainBinding.inflate

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        doWithAllImgPlaces { imgPlace -> imgPlace.setOnClickListener { setImgPlaceOnClick(imgPlace) } }

        binding.btnReset.setOnClickListener { reset() }
    }

    private val places = Array(9) { PlaceStatus.Empty }

    private var isOTurn = true

    private fun setImgPlaceOnClick(imgPlace: ImageView) {
        if (isOTurn) {
            imgPlace.setImageResource(R.drawable.ic_o_letter)
            val placeNumber = (imgPlace.tag as String).toInt()
            places[placeNumber] = PlaceStatus.O
        } else {
            imgPlace.setImageResource(R.drawable.ic_x_letter)
            val placeNumber = (imgPlace.tag as String).toInt()
            places[placeNumber] = PlaceStatus.X
        }
        imgPlace.isClickable = false
        imgPlace.isFocusable = false
        isOTurn = !isOTurn

        winningChecker()
    }

    private var nonEmptyPlaces = 0

    private fun winningChecker() {
        val winner =
            when {
                rowCheck().isWin -> rowCheck().winner
                columnCheck().isWin -> columnCheck().winner
                diagonalCheck().isWin -> diagonalCheck().winner
                else -> PlaceStatus.Empty
            }
        if (winner != PlaceStatus.Empty) {
            Toast.makeText(this, "${winner.name} win!", Toast.LENGTH_LONG).show()
            doWithAllImgPlaces {
                it.isClickable = false
                it.isFocusable = false
            }
        }
        else {
            nonEmptyPlaces++
            if (nonEmptyPlaces == 9)
                Toast.makeText(this, "Draw!", Toast.LENGTH_LONG).show()
        }
    }

    private fun rowCheck(): Winning {
        for (i in 0..6 step 3)
            if (places[i] == places[i + 1] && places[i] == places[i + 2])
                return Winning(true, places[i])

        return Winning(false)
    }

    private fun columnCheck(): Winning {
        for (i in 0..2)
            if (places[i] == places[i + 3] && places[i] == places[i + 6])
                return Winning(true, places[i])

        return Winning(false)
    }

    private fun diagonalCheck(): Winning {
        if (places[0] == places[4] && places[0] == places[8])
            return Winning(true, places[0])
        if (places[2] == places[4] && places[2] == places[6])
            return Winning(true, places[2])

        return Winning(false)
    }

    private fun reset() {
        doWithAllImgPlaces {
            it.setImageResource(0)
            it.isClickable = true
            it.isFocusable = true
        }
        (0 .. places.lastIndex).forEach { places[it] = PlaceStatus.Empty }
        nonEmptyPlaces = 0
    }

    private fun doWithAllImgPlaces(action: (imgPlace: ImageView) -> Unit) {
        for (child in binding.ConstraintLayout.children)
            if (child is ImageView && child.tag != null)
                action(child)
    }
}

private data class Winning(val isWin: Boolean, val winner: PlaceStatus = PlaceStatus.Empty)

private enum class PlaceStatus {
    Empty, O, X
}