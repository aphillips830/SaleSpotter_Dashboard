import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ui.Dashboard
import ui.theme.AppTheme

val appModule = module {
    single { UserController(get()) }
    single { YardSaleController(get()) }
    single<UserRepository> { UserRepositoryImpl() }
    single<YardSaleRepository> { YardSaleRepositoryImpl() }
}

fun main() = application {

    startKoin {
        modules(appModule)
    }

    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(1920.dp, 1080.dp)
    )

    Window(
        title = "SaleSpotter Dashboard",
        icon = painterResource("salespotter_fox.png"),
        onCloseRequest = ::exitApplication,
        state = windowState
    ) {
        AppTheme {
            Dashboard(this@application)
        }
    }
}
