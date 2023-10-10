package data

import DBConnection
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning
import models.User

class UserRepositoryImpl : UserRepository {
    override suspend fun getAllUsers(): List<UserDTO> {
        return DBConnection.client.postgrest["user"].select().decodeList<UserDTO>()
    }

    override suspend fun addUser(userInsertDTO: UserInsertDTO) {
        DBConnection.client.postgrest["user"].insert(userInsertDTO, returning = Returning.MINIMAL)
    }

    override suspend fun updateUser(user: User) {
        DBConnection.client.postgrest["user"].update( {
            UserInsertDTO::name setTo user.name
            UserInsertDTO::address setTo user.address
            UserInsertDTO::city setTo user.city
            UserInsertDTO::state setTo user.state
            UserInsertDTO::postcode setTo user.postcode
            UserInsertDTO::email setTo user.email
        }) {
            eq("id", user.userID)
        }
    }

    override suspend fun deleteUser(userID: Long) {
        DBConnection.client.postgrest["user"].delete {
            eq("id", userID)
        }
    }
}