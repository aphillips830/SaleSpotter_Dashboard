package ui

import TABLECOLORONE
import TABLECOLORTWO
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.User

@Composable
fun UserTab(
    joinedLast30: MutableIntState,
    joinedLast90: MutableIntState,
    joinedLast365: MutableIntState,
    totalUsers: MutableIntState,
    userTableSearchText: MutableState<String>,
    userByStateData: SnapshotStateMap<String, Int>,
    filteredUsersList: SnapshotStateList<User>,
    getFilteredUsersList: () -> Unit,
    addUserName: MutableState<String>,
    addUserAddress: MutableState<String>,
    addUserCity: MutableState<String>,
    addUserState: MutableState<String>,
    addUserPostcode: MutableState<String>,
    addUserEmail: MutableState<String>,
    editUserID: MutableLongState,
    editUserName: MutableState<String>,
    editUserAddress: MutableState<String>,
    editUserCity: MutableState<String>,
    editUserState: MutableState<String>,
    editUserPostcode: MutableState<String>,
    editUserEmail: MutableState<String>,
    deleteUser: (user: User) -> Unit,
    updateUser: () -> Unit,
    addUser: () -> Unit
) {
    val showAddUserDialog = remember { mutableStateOf(false) }
    val userValidated = remember { mutableStateOf(false) }
    val editUser = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        UISpacer()
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            TotalUserCard(totalUsers)
            JoinedCard(joinedLast30, joinedLast90, joinedLast365)
            StateCard(userByStateData)
        }
        UISpacer()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = userTableSearchText.value,
                onValueChange = {
                    userTableSearchText.value = it
                    filteredUsersList.clear()
                    getFilteredUsersList() },
                label = { Text(text = "Search") },
                singleLine = true,
                leadingIcon = { Image(painterResource("search.svg"), contentDescription = null) },
                shape = CutCornerShape(10.dp),
                modifier = Modifier.padding(0.dp, 8.dp)
            )
            FloatingActionButton(
                onClick = { showAddUserDialog.value = true }
            ) {
                Icon(painter = painterResource("add.svg"), contentDescription = "Add user.")
            }
        }
        UserTable(
            filteredUsersList = filteredUsersList,
            deleteUser = deleteUser,
            editUser = editUser,
            editUserID = editUserID,
            editUserName = editUserName,
            editUserAddress = editUserAddress,
            editUserCity = editUserCity,
            editUserState = editUserState,
            editUserPostcode = editUserPostcode,
            editUserEmail = editUserEmail
        )
        when {
            editUser.value -> {
                AlertDialog(
                    title = { Text(text = "Edit User") },
                    icon = { Icon(painterResource("edit.svg"), contentDescription = null) },
                    text = { AddEditUser(editUserName, editUserAddress, editUserCity, editUserState, editUserPostcode,
                        editUserEmail, userValidated) },
                    onDismissRequest = { /* Do nothing */ },
                    confirmButton = {
                        TextButton(onClick = {
                            updateUser()
                            editUser.value = false },
                            enabled = userValidated.value
                        ) {
                            Text(text = "Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { editUser.value = false }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
            showAddUserDialog.value -> {
                AlertDialog(
                    title = { Text(text = "Add User") },
                    icon = { Icon(painterResource("add.svg"), contentDescription = null) },
                    text = { AddEditUser(addUserName, addUserAddress, addUserCity, addUserState, addUserPostcode,
                            addUserEmail, userValidated) },
                    onDismissRequest = { /* Do nothing */ },
                    confirmButton = {
                        TextButton(onClick = {
                            addUser()
                            showAddUserDialog.value = false },
                            enabled = userValidated.value
                        ) {
                            Text(text = "Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddUserDialog.value = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun UserTable(
    filteredUsersList: SnapshotStateList<User>,
    deleteUser: (user: User) -> Unit,
    editUser: MutableState<Boolean>,
    editUserID: MutableLongState,
    editUserName: MutableState<String>,
    editUserAddress: MutableState<String>,
    editUserCity: MutableState<String>,
    editUserState: MutableState<String>,
    editUserPostcode: MutableState<String>,
    editUserEmail: MutableState<String>
) {
    val userIDWidth = 75.dp
    val nameWidth = 250.dp
    val addressWidth = 250.dp
    val cityWidth = 200.dp
    val stateWidth = 150.dp
    val postcodeWidth = 150.dp
    val emailWidth = 300.dp
    val joinedDateWidth = 200.dp
    Column {
        Divider(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp)) {
            TableHeaderText(text = "User ID", width = userIDWidth)
            TableHeaderText(text = "Name", width = nameWidth)
            TableHeaderText(text = "Street Address", width = addressWidth)
            TableHeaderText(text = "City", width = cityWidth)
            TableHeaderText(text = "State", width = stateWidth)
            TableHeaderText(text = "Postcode", width = postcodeWidth)
            TableHeaderText(text = "Email", width = emailWidth)
            TableHeaderText(text = "Date Joined", width = joinedDateWidth)
        }
        Divider(modifier = Modifier.height(4.dp))
    }
    LazyColumn {
        items(filteredUsersList.size) {
            if (it % 2 == 0) {
                UserTableRow(
                    id = filteredUsersList[it].userID.toString(), idWidth = userIDWidth,
                    name = filteredUsersList[it].name, nameWidth = nameWidth,
                    street = filteredUsersList[it].address, streetWidth = addressWidth,
                    city = filteredUsersList[it].city, cityWidth = cityWidth,
                    state = filteredUsersList[it].state, stateWidth = stateWidth,
                    postcode = filteredUsersList[it].postcode, postcodeWidth = postcodeWidth,
                    email = filteredUsersList[it].email, emailWidth = emailWidth,
                    dateJoined = filteredUsersList[it].joinedDate, dateJoinedWidth = joinedDateWidth,
                    deleteUser = { deleteUser(filteredUsersList[it]) },
                    editUser = editUser,
                    editUserID = editUserID,
                    editUserName = editUserName,
                    editUserAddress = editUserAddress,
                    editUserCity = editUserCity,
                    editUserState = editUserState,
                    editUserPostcode = editUserPostcode,
                    editUserEmail = editUserEmail,
                    rowColor = TABLECOLORONE
                )
            }
            else {
                UserTableRow(
                    id = filteredUsersList[it].userID.toString(), idWidth = userIDWidth,
                    name = filteredUsersList[it].name, nameWidth = nameWidth,
                    street = filteredUsersList[it].address, streetWidth = addressWidth,
                    city = filteredUsersList[it].city, cityWidth = cityWidth,
                    state = filteredUsersList[it].state, stateWidth = stateWidth,
                    postcode = filteredUsersList[it].postcode, postcodeWidth = postcodeWidth,
                    email = filteredUsersList[it].email, emailWidth = emailWidth,
                    dateJoined = filteredUsersList[it].joinedDate, dateJoinedWidth = joinedDateWidth,
                    deleteUser = { deleteUser(filteredUsersList[it]) },
                    editUser = editUser,
                    editUserID = editUserID,
                    editUserName = editUserName,
                    editUserAddress = editUserAddress,
                    editUserCity = editUserCity,
                    editUserState = editUserState,
                    editUserPostcode = editUserPostcode,
                    editUserEmail = editUserEmail,
                    rowColor = TABLECOLORTWO
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UserTableRow(
    id: String, idWidth: Dp,
    name: String, nameWidth: Dp,
    street: String, streetWidth: Dp,
    city: String, cityWidth: Dp,
    state: String, stateWidth: Dp,
    postcode: String, postcodeWidth: Dp,
    email: String, emailWidth: Dp,
    dateJoined: String, dateJoinedWidth: Dp,
    deleteUser: () -> Unit,
    editUser: MutableState<Boolean>,
    editUserID: MutableLongState,
    editUserName: MutableState<String>,
    editUserAddress: MutableState<String>,
    editUserCity: MutableState<String>,
    editUserState: MutableState<String>,
    editUserPostcode: MutableState<String>,
    editUserEmail: MutableState<String>,
    rowColor: Color
) {
    val confirmDeleteAlert = remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = rowColor, contentColor = Color.White),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            TableRowText(text = id, width = idWidth)
            TableRowText(text = name, width = nameWidth)
            TableRowText(text = street, width = streetWidth)
            TableRowText(text = city, width = cityWidth)
            TableRowText(text = state, width = stateWidth)
            TableRowText(text = postcode, width = postcodeWidth)
            TableRowText(text = email, width = emailWidth)
            TableRowText(text = dateJoined, width = dateJoinedWidth)
            val sendUser = {
                editUser.value = true
                editUserID.longValue = id.toLong()
                editUserName.value = name
                editUserAddress.value = street
                editUserCity.value = city
                editUserState.value = state
                editUserPostcode.value = postcode
                editUserEmail.value = email
            }
            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp).clickable { sendUser() },
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Edit: $name",
                            color = Color.Black,
                            modifier = Modifier.padding(20.dp, 4.dp, 8.dp, 4.dp)
                        )
                    }
                },
                delayMillis = 500,
                tooltipPlacement = TooltipPlacement.CursorPoint(alignment = Alignment.BottomEnd, offset = DpOffset.Zero)
            ) {
                Icon(
                    painter = painterResource("edit.svg"),
                    contentDescription = "Edit user.",
                    modifier = Modifier.padding(0.dp, 0.dp, 24.dp, 0.dp).clickable { sendUser() }
                )
            }
            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp).clickable { confirmDeleteAlert.value = true },
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Delete: $name",
                            color = Color.Red,
                            modifier = Modifier.padding(20.dp, 4.dp, 8.dp, 4.dp)
                        )
                    }
                },
                delayMillis = 500,
                tooltipPlacement = TooltipPlacement.CursorPoint(alignment = Alignment.BottomEnd, offset = DpOffset.Zero)
            ) {
                Icon(
                    painter = painterResource("delete.svg"),
                    contentDescription = "Delete user.",
                    modifier = Modifier.clickable { confirmDeleteAlert.value = true }
                )
            }
            when {
                confirmDeleteAlert.value -> {
                    AlertDialog(
                        onDismissRequest = { /* Do nothing */ },
                        confirmButton = {
                            TextButton(onClick = {
                                deleteUser()
                                confirmDeleteAlert.value = false
                            }) {
                                Text(text = "Delete")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { confirmDeleteAlert.value = false }) {
                                Text(text = "Cancel")
                            }
                        },
                        icon = { Icon(painterResource("delete.svg"), contentDescription = null) },
                        title = { Text(text = "Delete User") }
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalUserCard(totalUsers: MutableIntState) {
    ElevatedCard(
        shape = CutCornerShape(10.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Total Users",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            Text(
                text = totalUsers.intValue.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StateCard(stateData: SnapshotStateMap<String, Int>) {
    val stateDataSorted = stateData.entries.sortedByDescending { it.value }.associate { it.toPair() }
    ElevatedCard(
        shape = CutCornerShape(10.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Users by State",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            FlowRow {
                stateDataSorted.forEach {
                    Row(modifier = Modifier.padding(0.dp, 0.dp, 50.dp, 0.dp)) {
                        Text(
                            text = it.key + ":  ",
                            fontSize = 18.sp
                        )
                        Text(
                            text = it.value.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Yellow
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JoinedCard(
    joinedLast30: MutableIntState,
    joinedLast90: MutableIntState,
    joinedLast365: MutableIntState
) {
    ElevatedCard(
        shape = CutCornerShape(10.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(24.dp)) {
            Text(
                text = "New Users",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Last 30 days:  ",
                    fontSize = 18.sp
                )
                Text(
                    text = joinedLast30.intValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    text = "Last 90 days:  ",
                    fontSize = 18.sp
                )
                Text(
                    text = joinedLast90.intValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    text = "Last 365 days:  ",
                    fontSize = 18.sp
                )
                Text(
                    text = joinedLast365.intValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
            }
        }
    }
}
