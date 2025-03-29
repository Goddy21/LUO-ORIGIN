package semeluo.history.book.theoriginofluoculture

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class ChapterExpandableListAdapter(
    private val context: Context,
    private val originalChapters: List<String>,
    private val originalChapterMap: Map<String, List<Triple<Int, String, Int>>>
) : BaseExpandableListAdapter() {

    private var filteredChapters: MutableList<String> = originalChapters.toMutableList()
    private var filteredChapterMap: MutableMap<String, List<Triple<Int, String, Int>>> = originalChapterMap.toMutableMap()

    override fun getGroupCount(): Int = filteredChapters.size

    override fun getChildrenCount(groupPosition: Int): Int {
        val key = filteredChapters.getOrNull(groupPosition) ?: return 0
        return filteredChapterMap[key]?.size ?: 0
    }


    override fun getGroup(groupPosition: Int): Any = filteredChapters[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): Any? =
        filteredChapterMap[filteredChapters[groupPosition]]?.get(childPosition)

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        val groupTitle = getGroup(groupPosition) as String
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.group_item, parent, false)
        val textView: TextView = listItemView.findViewById(R.id.groupTitle)
        textView.text = groupTitle
        return listItemView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        val childItem = getChild(groupPosition, childPosition) as? Triple<Int, String, Int> ?: Triple(0, "Unknown", 0)
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.child_item, parent, false)
        val titleView: TextView = listItemView.findViewById(R.id.childTitle)
        titleView.text = childItem?.second
        return listItemView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
    override fun areAllItemsEnabled(): Boolean = true

    override fun onGroupExpanded(groupPosition: Int) {}
    override fun onGroupCollapsed(groupPosition: Int) {}

    // Filtering method
    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase()
        filteredChapters.clear()
        filteredChapterMap.clear()

        if (query.isEmpty()) {
            filteredChapters.addAll(originalChapters)
            filteredChapterMap.putAll(originalChapterMap)
        } else {
            for (chapter in originalChapters) {
                val filteredPages = originalChapterMap[chapter]?.filter { page ->
                    page.second.lowercase().contains(lowerCaseQuery)
                } ?: emptyList()

                if (chapter.lowercase().contains(lowerCaseQuery) || filteredPages.isNotEmpty()) {
                    filteredChapters.add(chapter)
                    filteredChapterMap[chapter] = filteredPages
                }
            }
        }

        // Ensure update runs on the UI thread
        (context as? android.app.Activity)?.runOnUiThread {
            notifyDataSetChanged()
        }
    }

}
