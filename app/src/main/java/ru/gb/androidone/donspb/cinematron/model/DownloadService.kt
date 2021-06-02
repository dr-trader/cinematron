package ru.gb.androidone.donspb.cinematron.model

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000


class DownloadService(name: String = "DownloadService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val id = intent.getIntExtra(Consts.ID_NAME_EXTRA, 0)
            if (id == 0) {
                onEmptyData()
            } else {
                loadMovie(id)
            }
        }
    }

    private fun loadMovie(id: Int) {
        try {
            val uri = URL("https://api.themoviedb.org/3/movie/$id?api_key=${Consts.API_KEY}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = REQUEST_GET
                urlConnection.readTimeout = REQUEST_TIMEOUT

                val oneMovie: OneMovie = Gson().fromJson(
                    getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))), OneMovie::class.java
                )
                onResponse(oneMovie)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    private fun getLines(reader: BufferedReader) = reader.lines().collect(Collectors.joining("\n"))

    private fun onResponse(oneMovie: OneMovie) {
        if (oneMovie.title == null) {
            onEmptyResponse()
        } else {
            var genresString = ""
            with (oneMovie) {
                val date = LocalDate.parse(release_date)
                for (genre in genres) {
                    genresString += genre.name
                }
                onSuccessResponse(title, genresString, id, overview, date.year, vote_average)
            }
        }
    }

    private fun onSuccessResponse(
        title: String?, genres: String, id: Int, overview: String, year: Int, votes: Float
    ) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_TITLE_EXTRA, title)
        broadcastIntent.putExtra(DETAILS_GENRES_EXTRA, genres)
        broadcastIntent.putExtra(DETAILS_ID_EXTRA, id)
        broadcastIntent.putExtra(DETAILS_OVERVIEW_EXTRA, overview)
        broadcastIntent.putExtra(DETAILS_YEAR_EXTRA, year)
        broadcastIntent.putExtra(DETAILS_VOTES_EXTRA, votes)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

}
