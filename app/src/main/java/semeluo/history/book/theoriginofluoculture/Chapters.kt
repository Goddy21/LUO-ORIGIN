package semeluo.history.book.theoriginofluoculture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import org.json.JSONArray
import java.io.IOException

class Chapters : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var expandableListView: ExpandableListView
    private lateinit var searchEditText: SearchView
    private lateinit var adapter: ChapterExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chapters, container, false)

        dbHelper = DatabaseHelper(requireContext())

        // Check if the database is empty and add chapters if true
        if (dbHelper.isDatabaseEmpty()) {
            addChaptersToDatabase(requireContext())
        }

        expandableListView = view.findViewById<ExpandableListView>(R.id.listView)
        searchEditText = view.findViewById(R.id.search) // Initialize the SearchView

        loadChapters()


        // Add a text change listener for search functionality
        searchEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "") // Filter the adapter based on input
                return true
            }
        })

        return view
    }

    private fun addChaptersToDatabase(context: Context) {
        val jsonString = loadJSONFromAsset(context, "book_content.json")
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val chapterObj = jsonArray.getJSONObject(i)
            val chapterName = chapterObj.getString("chapter")
            val chapterId = dbHelper.insertChapter(chapterName) // Insert chapter into the database

            // Get pages array
            val pagesArray = chapterObj.getJSONArray("pages")
            for (j in 0 until pagesArray.length()) {
                val pageObj = pagesArray.getJSONObject(j)
                dbHelper.insertPage(
                    pageObj.getString("title"),
                    pageObj.getString("content"),
                    chapterId.toInt(), // Use the inserted chapter ID
                    pageObj.getInt("page_number")
                )
            }
        }
    }

    private fun loadChapters() {
        val chapters = dbHelper.getChapters() // This should yield List<Pair<Int, String>>
        val chapterMap = mutableMapOf<String, List<Triple<Int, String, Int>>>()

        for ((id, name) in chapters) {
            val pages = dbHelper.getPagesByChapter(id) // Fetch pages based on chapter ID
            val pageList = pages.map { page ->
                Triple(page.id, page.title, page.pageNumber) // Convert to Triple for easier access
            }
            chapterMap[name] = pageList // Map chapter name to its pages
        }

        val chapterTitles: List<String> = chapterMap.keys.toList() // Get Chapter titles as a List of Strings
        adapter = ChapterExpandableListAdapter(requireContext(), chapterTitles, chapterMap)

        expandableListView.setAdapter(adapter)

        // Handle child clicks to view specific page content
        expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val chapterName = chapterTitles[groupPosition]
            val pages = chapterMap[chapterName] ?: return@setOnChildClickListener false

            if (pages.size > childPosition) {
                val (pageId, pageTitle, pageNumber) = pages[childPosition]

                val intent = Intent(requireContext(), PageViewerActivity::class.java).apply {
                    putExtra("PAGE_ID", pageId)
                    putExtra("PAGE_TITLE", pageTitle)
                    putExtra("CHAPTER_NAME", chapterName)
                    putExtra("PAGE_NUMBER", pageNumber)
                }
                startActivity(intent)
            } else {
                Log.e("ChaptersFragment", "Invalid child position: $childPosition for chapter: $chapterName")
            }

            true
        }
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            throw RuntimeException("Failed to load JSON from asset: $fileName")
        }
    }
}
