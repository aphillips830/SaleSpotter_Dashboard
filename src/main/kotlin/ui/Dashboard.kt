package ui

import DBDELAY
import UserController
import YardSaleController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Dashboard(
    applicationScope: ApplicationScope,
    userController: UserController = koinInject(),
    yardSaleController: YardSaleController = koinInject()
) {
    var hasStarted by remember { mutableStateOf(true) }
    var tabState by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Users", "Yard Sales")
    Column(
        modifier = Modifier.padding(8.dp, 0.dp)
    ) {
        MenuBar(
            exitApplication = { applicationScope.exitApplication() }
        )
        Column {
            TabRow(selectedTabIndex = tabState) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabState == index,
                        onClick = { tabState = index },
                        text = { Text(text = title, fontSize = 18.sp) }
                    )
                }
            }
            LaunchedEffect("App start") {
                delay(DBDELAY.milliseconds)
                hasStarted = false
            }
            if (!hasStarted) {
                when (tabState) {
                    0 -> UserTab(
                        joinedLast30 = userController.usersJoinedLast30,
                        joinedLast90 = userController.usersJoinedLast90,
                        joinedLast365 = userController.usersJoinedLast365,
                        totalUsers = userController.totalUsers,
                        userTableSearchText = userController.userTableSearchText,
                        userByStateData = userController.userByStateData,
                        filteredUsersList = userController.filteredUsersList,
                        getFilteredUsersList = { userController.getFilteredUsersList() },
                        addUserName = userController.addUserName,
                        addUserAddress = userController.addUserAddress,
                        addUserCity = userController.addUserCity,
                        addUserState = userController.addUserState,
                        addUserPostcode = userController.addUserPostcode,
                        addUserEmail = userController.addUserEmail,
                        deleteUser = { user -> userController.deleteUser(user) },
                        updateUser = { userController.updateUser() },
                        addUser = { userController.addUser() },
                        editUserID = userController.editUserID,
                        editUserName = userController.editUserName,
                        editUserAddress = userController.editUserAddress,
                        editUserCity = userController.editUserCity,
                        editUserState = userController.editUserState,
                        editUserPostcode = userController.editUserPostcode,
                        editUserEmail = userController.editUserEmail
                    )
                    1 -> YardSaleTab(
                        yardSaleTableSearchText = yardSaleController.yardSaleTableSearchText,
                        filteredYardSaleList = yardSaleController.filteredYardSaleList,
                        getFilteredYardSaleList = { yardSaleController.getFilteredYardSaleList() },
                        totalYardSales = yardSaleController.totalYardSales,
                        yardSalesNext30 = yardSaleController.yardSalesNext30,
                        yardSalesNext90 = yardSaleController.yardSalesNext90,
                        yardSalesNext365 = yardSaleController.yardSalesNext365,
                        categoryData = yardSaleController.yardSalesByCategoryData,
                        deleteYardSale = { yardSale -> yardSaleController.deleteYardSale(yardSale) },
                        addStartDate = yardSaleController.addStartDate,
                        addStartTime = yardSaleController.addStartTime,
                        addEndDate = yardSaleController.addEndDate,
                        addInformation = yardSaleController.addInformation,
                        addAddress = yardSaleController.addAddress,
                        addCity = yardSaleController.addCity,
                        addState = yardSaleController.addState,
                        addPostcode = yardSaleController.addPostcode,
                        addYardSale = { yardSaleController.addYardSale() }
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuBar(
    exitApplication: () -> Unit
) {
    var fileMenuExpanded by remember { mutableStateOf(false) }
    Row {
        TextButton(onClick = { fileMenuExpanded = true } ) {
            Text(text = "File", color = Color.Black)
        }
        DropdownMenu(expanded = fileMenuExpanded, onDismissRequest = { fileMenuExpanded = !fileMenuExpanded }) {
            DropdownMenuItem(
                text = { Text(text = "Exit") },
                onClick = exitApplication
            )
        }
    }
}