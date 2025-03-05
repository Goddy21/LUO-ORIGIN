package semeluo.history.book.theoriginofluoculture

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

class World : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_world)
        // Set the status bar color dynamically
        val window: Window = window
        window.statusBarColor = resources.getColor(R.color.my_status_bar_color)
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an ActionBarDrawerToggle to handle
        // the drawer's open/close state
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        // Add the toggle as a listener to the DrawerLayout
        drawerLayout.addDrawerListener(toggle);
        // Synchronize the toggle's state with the linked DrawerLayout
        toggle.syncState();

        // Set a listener for when an item in the NavigationView is selected
        navigationView.setNavigationItemSelectedListener { item ->
            // Handle the selected item based on its ID
            when (item.itemId) {
                R.id.nav_about -> {
                    // Show a Toast message for the Account item
                    Toast.makeText(this, "About the app", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    // Show a Toast message for the Settings item
                    Toast.makeText(this, "Settings Opened", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_share -> {
                    // Show a Toast message for the Settings item
                    Toast.makeText(this, "Share the App", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_logout -> {
                    // Show a Toast message for the Logout item
                    Toast.makeText(this, "You are Logged Out", Toast.LENGTH_SHORT).show()
                }
            }

            // Close the drawer after selection
            drawerLayout.closeDrawers()

            // Indicate that the item selection has been handled
            true
        }

        // Add a callback to handle the back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            override fun handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Close the drawer if it's open
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Finish the activity if the drawer is closed
                    finish()
                }
            }
        })

        // Ensure 'main' is the correct ID for the root layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Returning insets
        }

        bottomNav = findViewById(R.id.bottomNav) // Direct assignment without casting
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(Home())
                    true // Return true to indicate selection is handled
                }
                R.id.navigation_chapters -> {
                    loadFragment(Chapters())
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(profile())
                    true // Returning true here to handle this case
                }
                else -> false // Default return for unhandled cases
            }
        }

        // Load the initial fragment (set a default or initial fragment here)
        loadFragment(Home())
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
        Log.d("FragmentTest", "Loaded Fragment: ${fragment.javaClass.simpleName}")
    }
}
