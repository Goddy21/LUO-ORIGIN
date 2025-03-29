package semeluo.history.book.theoriginofluoculture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import semeluo.history.book.theoriginofluoculture.databinding.ActivityPageViewerBinding

class PageViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPageViewerBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val pageId = intent.getIntExtra("PAGE_ID", -1)

        if (pageId != -1) {
            val pageData = dbHelper.getPageContent(pageId)
            if (pageData != null) {
                displayPageContent(pageData)
            } else {
                // Handle the case where page content is null
            }
        }
    }

    private fun displayPageContent(pageData: PageData) {
        binding.txtPageTitle.text = pageData.title
        binding.txtContent.text = pageData.content
        binding.txtPageNumber.text = "Page ${pageData.pageNumber}"
    }
}
