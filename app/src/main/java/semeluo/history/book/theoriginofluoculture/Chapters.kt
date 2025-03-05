package semeluo.history.book.theoriginofluoculture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Chapters.newInstance] factory method to
 * create an instance of this fragment.
 */
class Chapters : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chapters, container, false)
        val search = view.findViewById<SearchView>(R.id.searchView) as SearchView

        search.post {
            val searchText = search.findViewById<android.widget.EditText>(androidx.appcompat.R.id.search_src_text)
            searchText?.let {
                it.setTextColor(resources.getColor(android.R.color.black))
                it.setHintTextColor(resources.getColor(android.R.color.darker_gray))
                it.textSize = 16f
            }
        }


        val searchIcon = search.findViewById<View>(androidx.appcompat.R.id.search_mag_icon)
        (searchIcon as? ImageView)?.setColorFilter(resources.getColor(R.color.my_light_on_secondary))

        val closeIcon = search.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
        (closeIcon as? ImageView)?.setColorFilter(resources.getColor(R.color.my_light_on_secondary))

        val list = view.findViewById<ListView>(R.id.listView) as ListView
        val chapters = arrayOf("Prologue","Introduction", "In the garden of Eden",
            "Complete account of all nations from Noah",
            "Japhethites(Europeans and the Persians)",
            "Shemites(Arabs, Persians, Hebrews, Israelites)",
            "Hamites(Africans and some Arabs)",
            "Summary of the Biblical account of Nations",
            "The origin of some common names",
            "The Nubians",
            "The Nubian Dynasty in Egypt",
            "Dynasties 1 to 6-Early dynasty period (C.3100-2686 BCE)",
            "Nubians after the 25th dynasty",
            "The rise and fall of the Kush kingdom",
            "The Meroetic alphabet and language",
            "Social and administrative structure in the kingdom of Kush",
            "The downfall of the Kush kingdom",
            "The last three Nubian kingdoms",
            "Kingdom of Nobatia (350-590 AD",
            "Kingdom of Makuria (590-1314 AD",
            "Kingdom of Alodia (Nubia), 570-1480 AD")
        val chapterAdap: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,R.id.textViewItem, chapters)
        list.adapter = chapterAdap

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                chapterAdap.filter.filter(newText)
                return false
            }
        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Menu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Chapters().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}