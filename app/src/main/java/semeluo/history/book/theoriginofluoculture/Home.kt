package semeluo.history.book.theoriginofluoculture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment() {
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
        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find WebView inside the inflated layout
        val webView: WebView = view.findViewById(R.id.webView)

        val htmlContent = """
    <html>
    <head>
        <style>
            body {
                font-family: 'Georgia', serif;
                font-size: 18px;
                line-height: 1.6;
                color: #333333;
                padding: 16px;
                background-color: #fdfdfd;
            }
            h2 {
                text-align: center;
                color: #2c3e50;
                margin-bottom: 12px;
            }
            h3 {
                color: #34495e;
                margin-top: 24px;
                border-bottom: 1px solid #ccc;
                padding-bottom: 4px;
            }
            p {
                margin-bottom: 16px;
            }
            ul {
                padding-left: 20px;
            }
            ul li {
                margin-bottom: 10px;
                list-style-type: '📍';
                padding-left: 6px;
            }
            img {
                width: 100%;
                height: auto;
                margin: 16px 0;
                border-radius: 12px;
                box-shadow: 0px 4px 8px rgba(0,0,0,0.1);
            }
            .img-caption {
                text-align: center;
                font-style: italic;
                font-size: 14px;
                color: #666;
                margin-top: -12px;
                margin-bottom: 24px;
            }
        </style>
    </head>
    <body>
        <h2>Luo Culture and Tradition</h2>
        <p>This book explores the origin of the culture of the Luo-speaking people, who are spread across six countries in Eastern Africa. Culture plays a crucial role in defining people’s identity, their belief systems, and patterns of behavior.</p>
        
        <h3>The Luo Nation</h3>
        <p>The Luo Nation is a monolithic and distinct community that traces its roots back to Noah’s lineage. In subsequent generations, it transformed into various sub-tribes while still retaining similarities in its culture, traditions, language, and other aspects of their lives.</p>
        
        <img src="file:///android_asset/map.png" alt="Luo Migration Map" />
        <div class="img-caption">Migration map showing the journey of the Luo people</div>

        <h3>The Long Trek Route by Proximate Years:</h3>
        <ul>
            <li>Garden of Eden / Sumeria (6000BC to Millions of years ago)</li>
            <li>Memphis city (5000BC)</li>
            <li>Thebes city (3200BC)</li>
            <li>Kerma city (2500BC – 1500 BC)</li>
            <li>Napata kingdom (750BC – 270BC)</li>
            <li>Meroe City (400AD)</li>
            <li>Nobatia kingdom (400AD – 652AD)</li>
            <li>Makuria / Dongola kingdom (590AD – 1100AD)</li>
            <li>Alodia / Alwa / Anwa Kingdom (9th – 12th Centuries)</li>
            <li>South Sudan - Final disintegration (1220AD – 1498AD)</li>
            <li>Uganda, Kenya, Tanzania (15TH Century)</li>
        </ul>
    </body>
    </html>
""".trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
