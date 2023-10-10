import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import data.UserDTO
import data.UserInsertDTO
import data.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import models.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds

class UserController(private val userRepository: UserRepository) {

    private val allUsers: MutableList<User> = mutableListOf()

    val usersJoinedLast30: MutableIntState = mutableIntStateOf(0)
    val usersJoinedLast90: MutableIntState = mutableIntStateOf(0)
    val usersJoinedLast365: MutableIntState = mutableIntStateOf(0)
    val totalUsers: MutableIntState = mutableIntStateOf(0)
    val userTableSearchText: MutableState<String> = mutableStateOf("")
    val filteredUsersList: SnapshotStateList<User> = mutableStateListOf()
    val userByStateData: SnapshotStateMap<String, Int> = mutableStateMapOf()

    val addUserName: MutableState<String> = mutableStateOf("")
    val addUserAddress: MutableState<String> = mutableStateOf("")
    val addUserCity: MutableState<String> = mutableStateOf("")
    val addUserState: MutableState<String> = mutableStateOf("")
    val addUserPostcode: MutableState<String> = mutableStateOf("")
    val addUserEmail: MutableState<String> = mutableStateOf("")

    val editUserID: MutableLongState = mutableLongStateOf(0)
    val editUserName: MutableState<String> = mutableStateOf("")
    val editUserAddress: MutableState<String> = mutableStateOf("")
    val editUserCity: MutableState<String> = mutableStateOf("")
    val editUserState: MutableState<String> = mutableStateOf("")
    val editUserPostcode: MutableState<String> = mutableStateOf("")
    val editUserEmail: MutableState<String> = mutableStateOf("")

    init {
        getUserData()
    }

    private fun getUserData() {
        resetData()
        val allDBUsers = MutableStateFlow<List<User>?>(listOf())
        CoroutineScope(Dispatchers.IO).launch {
            val dbUsers = userRepository.getAllUsers()
            allDBUsers.emit(dbUsers.map { it.asDomainModel() })
            allDBUsers.collect  { list ->
                list?.forEach { user ->
                    allUsers.add(user)
                    ++totalUsers.intValue
                    getUsersJoinedLast3090365Days(user)
                    getUsersByStateData(user)
                    getFilteredUsersList()
                }
            }
        }
    }

    fun deleteUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.deleteUser(user.userID)
            delay(DBDELAY.milliseconds)
            getUserData()
        }
    }

    fun updateUser() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.updateUser(User(
                userID = editUserID.longValue,
                name = editUserName.value,
                address = editUserAddress.value,
                city = editUserCity.value,
                state = editUserState.value,
                postcode = editUserPostcode.value,
                email = editUserEmail.value,
                joinedDate = ""
            ))
            delay(DBDELAY.milliseconds)
            getUserData()
        }
    }

    fun addUser() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.addUser(UserInsertDTO(
                name = addUserName.value.trim(),
                address = addUserAddress.value.trim(),
                city = addUserCity.value.trim(),
                state = addUserState.value.trim(),
                postcode = addUserPostcode.value.trim(),
                email = addUserEmail.value.trim()
            ))
            delay(DBDELAY.milliseconds)
            getUserData()
        }
    }

    private fun resetData() {
        allUsers.clear()
        totalUsers.intValue = 0
        usersJoinedLast30.intValue = 0
        usersJoinedLast90.intValue = 0
        usersJoinedLast365.intValue = 0
        userByStateData.clear()
    }

    private fun UserDTO.asDomainModel(): User {
        return User(
            userID = this.userID,
            name = this.name,
            address = this.address,
            city = this.city,
            state = this.state,
            postcode = this.postcode,
            email = this.email,
            joinedDate = OffsetDateTime.parse(this.joinedDate).atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime().format(DATETIMEFORMAT)
        )
    }

    fun getFilteredUsersList() {
        filteredUsersList.clear()
        val search = userTableSearchText.value.lowercase()
        for (user in allUsers) {
            if (search in user.name.lowercase() ||
                search in user.address.lowercase() ||
                search in user.city.lowercase() ||
                search in user.state.lowercase() ||
                search in user.postcode.lowercase() ||
                search in user.email.lowercase() ||
                search in user.joinedDate.lowercase()
            ) {
                filteredUsersList.add(user)
            }
        }
    }

    private fun getUsersJoinedLast3090365Days(user: User) {
        val today: LocalDate = LocalDate.now()
        val localJoinedDate: LocalDate = LocalDateTime.parse(user.joinedDate, DATETIMEFORMAT).toLocalDate()
        val daysOld = ChronoUnit.DAYS.between(today, localJoinedDate)
        if (daysOld <= 30)
            ++usersJoinedLast30.intValue
        if (daysOld <= 90)
            ++usersJoinedLast90.intValue
        if (daysOld <= 365)
            ++usersJoinedLast365.intValue
    }

    private fun getUsersByStateData(user: User) {
        userByStateData.merge(user.state, 1, Int::plus)
    }
}