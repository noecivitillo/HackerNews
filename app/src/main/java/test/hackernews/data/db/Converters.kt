package test.hackernews.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import test.hackernews.model.HighlightResult

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromHighlightResult(data: String?): HighlightResult {
            val type = object : TypeToken<HighlightResult>() {}.type
            return Gson().fromJson<HighlightResult>(data, type)
        }

        @TypeConverter
        @JvmStatic
        fun toHighlightResult(highlightResult: HighlightResult): String {
            val type = object : TypeToken<HighlightResult>() {}.type
            return Gson().toJson(highlightResult, type)
        }
    }
}