package test.hackernews.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * A helper class to format the "created at" to an understandable time. E.g 10 min ago
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TimeUtils {
    companion object {
        fun getTimeAgo(createdAt: String?): CharSequence {
            return if (createdAt != null) {
                val simpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss'Z'", Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("CEST")
                val time = simpleDateFormat.parse(createdAt).time
                val now = System.currentTimeMillis()
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL)
            } else {
                ""
            }
        }
    }
}