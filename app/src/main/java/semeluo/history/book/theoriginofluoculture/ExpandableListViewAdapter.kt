package semeluo.history.book.theoriginofluoculture

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView

class ExpandableListViewAdapter(
    private val context: Context,
    private val titles: List<String>,
    private val contents: List<List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return titles.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return contents[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return titles[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return contents[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_group, parent, false)

        val textView = view.findViewById<TextView>(R.id.listTitle)
        val imageView = view.findViewById<ImageView>(R.id.groupIndicator)

        textView.text = getGroup(groupPosition) as String

        // Ensure the ImageView is always visible
        imageView.visibility = View.VISIBLE

        // Update the arrow direction based on expanded/collapsed state
        if (isExpanded) {
            imageView.setImageResource(android.R.drawable.arrow_up_float) // Up when expanded
        } else {
            imageView.setImageResource(android.R.drawable.arrow_down_float) // Down when collapsed
        }

        return view
    }


    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.expandedListItem)
        textView.text = getChild(groupPosition, childPosition) as String
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
