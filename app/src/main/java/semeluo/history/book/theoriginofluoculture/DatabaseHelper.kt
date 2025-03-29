package semeluo.history.book.theoriginofluoculture

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "origin_of_luo_culture.db"
        private const val DATABASE_VERSION = 1
    }

    private val TABLE_CHAPTERS = "chapters"
    private val TABLE_PAGES = "pages"

    private val COL_CHAPTER_ID = "id"
    private val COL_CHAPTER_NAME = "name"

    private val COL_PAGE_ID = "id"
    private val COL_PAGE_TITLE = "title"
    private val COL_PAGE_CONTENT = "content"
    private val COL_PAGE_CHAPTER_ID = "chapter_id"
    private val COL_PAGE_NUMBER = "page_number"

    override fun onCreate(db: SQLiteDatabase) {
        val createChaptersTable = """
            CREATE TABLE $TABLE_CHAPTERS (
                $COL_CHAPTER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CHAPTER_NAME TEXT UNIQUE NOT NULL
            )
        """.trimIndent()

        val createPagesTable = """
            CREATE TABLE $TABLE_PAGES (
                $COL_PAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PAGE_TITLE TEXT NOT NULL,
                $COL_PAGE_CONTENT TEXT NOT NULL,
                $COL_PAGE_CHAPTER_ID INTEGER NOT NULL,
                $COL_PAGE_NUMBER INTEGER NOT NULL,
                FOREIGN KEY($COL_PAGE_CHAPTER_ID) REFERENCES $TABLE_CHAPTERS($COL_CHAPTER_ID)
            )
        """.trimIndent()

        db.execSQL(createChaptersTable)
        db.execSQL(createPagesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAGES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHAPTERS")
        onCreate(db)
    }

    fun insertChapter(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply { put(COL_CHAPTER_NAME, name) }
        val result = db.insert(TABLE_CHAPTERS, null, values)

        if (result == -1L) {
            Log.e("DatabaseHelper", "Failed to insert chapter: $name")
        }
        return result
    }

    fun insertPage(title: String, content: String, chapterId: Int, pageNumber: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PAGE_TITLE, title)
            put(COL_PAGE_CONTENT, content)
            put(COL_PAGE_CHAPTER_ID, chapterId)
            put(COL_PAGE_NUMBER, pageNumber)
        }
        val result = db.insert(TABLE_PAGES, null, values)

        if (result == -1L) {
            Log.e("DatabaseHelper", "Failed to insert page: $title (Chapter ID: $chapterId)")
        }
        return result
    }


    fun getChapters(): List<Pair<Int, String>> {
        val db = readableDatabase
        val chapters = mutableListOf<Pair<Int, String>>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CHAPTERS", null)

        cursor.use {
            while (it.moveToNext()) {
                val idIndex = it.getColumnIndex(COL_CHAPTER_ID)
                val id = if (idIndex != -1) it.getInt(idIndex) else -1
                val name = it.getString(it.getColumnIndexOrThrow(COL_CHAPTER_NAME))
                chapters.add(Pair(id, name))
            }
        }
        return chapters
    }

    fun getPagesByChapter(chapterId: Int): List<PageData> {
        val db = readableDatabase
        val pages = mutableListOf<PageData>()
        val cursor = db.rawQuery(
            "SELECT $COL_PAGE_ID, $COL_PAGE_TITLE, $COL_PAGE_CONTENT, $COL_PAGE_CHAPTER_ID, $COL_PAGE_NUMBER FROM $TABLE_PAGES WHERE $COL_PAGE_CHAPTER_ID = ? ORDER BY $COL_PAGE_NUMBER",
            arrayOf(chapterId.toString())
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COL_PAGE_ID))
                val title = it.getString(it.getColumnIndexOrThrow(COL_PAGE_TITLE))
                val content = it.getString(it.getColumnIndexOrThrow(COL_PAGE_CONTENT))
                val chapterId = it.getInt(it.getColumnIndexOrThrow(COL_PAGE_CHAPTER_ID))
                val pageNumber = it.getInt(it.getColumnIndexOrThrow(COL_PAGE_NUMBER))
                pages.add(PageData(id, title, content, chapterId, pageNumber))
            }
        }
        return pages
    }

    fun getPageContent(pageId: Int): PageData? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_PAGE_ID, $COL_PAGE_TITLE, $COL_PAGE_CONTENT, $COL_PAGE_CHAPTER_ID, $COL_PAGE_NUMBER FROM $TABLE_PAGES WHERE $COL_PAGE_ID = ?",
            arrayOf(pageId.toString())
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAGE_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAGE_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAGE_CONTENT))
            val chapterId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAGE_CHAPTER_ID))
            val pageNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAGE_NUMBER))
            cursor.close()
            PageData(id, title, content, chapterId, pageNumber)
        } else {
            cursor.close()
            null
        }
    }

    fun isDatabaseEmpty(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CHAPTERS", null)
        var isEmpty = true

        cursor.use {
            if (it.moveToFirst()) {
                isEmpty = it.getInt(0) == 0
            }
        }
        return isEmpty
    }
}
