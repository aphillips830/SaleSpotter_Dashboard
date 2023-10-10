package data

import models.User

interface UserRepository {
    suspend fun getAllUsers(): List<UserDTO>
    suspend fun addUser(userInsertDTO: UserInsertDTO)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userID: Long)
}