package ru.gb.androidone.donspb.cinematron.model

import android.os.Handler
import android.util.Log
import com.google.gson.Gson
import ru.gb.androidone.donspb.cinematron.Consts
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieLoader(private val listener: MovieLoaderListener, private val id: Int = 0 ) {

    interface MovieLoaderListener {
        fun onLoaded(oneMovie: OneMovie)
        fun onFailed(throwable: Throwable)
    }

    fun loadMovieData() {
        try {
            val uri = URL("https://api.themoviedb.org/3/movie/$id?api_key=${Consts.API_KEY}")
            val handler = Handler()
            Thread(
                Runnable {
                    lateinit var urlConnection: HttpsURLConnection
                    try {
                        urlConnection = uri.openConnection() as HttpsURLConnection
                        urlConnection.requestMethod = "GET"
                        urlConnection.readTimeout = 10000
                        val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))

                        val oneMovie: OneMovie = Gson().fromJson(getLines(bufferedReader), OneMovie::class.java)
                        handler.post { listener.onLoaded(oneMovie) }
                    } catch (e: Exception) {
                        Log.e("", "Connection Failed", e)
                        e.printStackTrace()
                    } finally {
                        urlConnection.disconnect()
                    }
                }
            ).start()
        } catch (e: MalformedURLException) {
            Log.e("", "URI Error", e)
            e.printStackTrace()
        }
    }

    private fun getLines(reader: BufferedReader) = reader.lines().collect(Collectors.joining("\n"))
}