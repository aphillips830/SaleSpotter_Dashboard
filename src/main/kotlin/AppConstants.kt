import androidx.compose.ui.graphics.Color
import java.time.format.DateTimeFormatter

val DATEFORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
val TIMEFORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
val DATETIMEFORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy 'at' h:mm a")
val DBDELAY: Int = 1000
val TABLECOLORONE: Color = Color(0xFF09136e)
val TABLECOLORTWO: Color = Color(0xFF392747)