import android.content.Context

class SessionManager private constructor(private val context: Context) {
    companion object {
        private var instance: SessionManager? = null
        private const val PREFS_NAME = "SessionPrefs"

        @Synchronized
        fun getInstance(context: Context): SessionManager {
            if (instance == null) {
                instance = SessionManager(context)
            }
            return instance!!
        }
    }
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var loggedInUserId: String?
        get() = prefs.getString("UserId", null)
        set(value) {
            prefs.edit().putString("UserId", value).apply()
        }

    var isLoggedIn: Boolean
        get() = prefs.getBoolean("IsLoggedIn", false)
        set(value) {
            prefs.edit().putBoolean("IsLoggedIn", value).apply()
        }

    var isAdmin: Boolean
        get() = prefs.getBoolean("IsAdmin", false)
        set(value) {
            prefs.edit().putBoolean("IsAdmin", value).apply()
        }
    fun logout() {
        prefs.edit().clear().apply()
    }
}

