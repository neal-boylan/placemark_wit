package org.wit.placemark.views.placemark

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber.i

class PlacemarkView : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    private lateinit var presenter: PlacemarkPresenter
    var placemark = PlacemarkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = PlacemarkPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.placemarkDescription.text.toString())
            presenter.doSelectImage()
        }

        binding.placemarkLocation.setOnClickListener {
            presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.placemarkDescription.text.toString())
            presenter.doSetLocation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.delete_button)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_button -> {
                if (binding.placemarkTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.please_enter_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    // presenter.cachePlacemark(binding.placemarkTitle.text.toString(), binding.description.text.toString())
                    presenter.doAddOrSave(binding.placemarkTitle.text.toString(), binding.placemarkDescription.text.toString())
                }
            }
            R.id.delete_button -> {
                presenter.doDelete()
            }
            R.id.cancel_button -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showPlacemark(placemark: PlacemarkModel) {
        binding.placemarkTitle.setText(placemark.title)
        binding.placemarkDescription.setText(placemark.description)
        // binding.btnAdd.setText(R.string.button_savePlacemark)
        Picasso.get()
            .load(placemark.image)
            .into(binding.placemarkImage)
        if (placemark.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_placemark_image)
        }

    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.placemarkImage)
        binding.chooseImage.setText(R.string.change_placemark_image)
    }

}