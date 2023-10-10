package data

import DBConnection
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Returning

class YardSaleRepositoryImpl : YardSaleRepository {

    override suspend fun getAllYardSales(): List<YardSaleDTO> {
        return DBConnection.client.postgrest["yardsale"].select().decodeList<YardSaleDTO>()
    }

    override suspend fun deleteYardSale(yardSaleID: Long) {
        DBConnection.client.postgrest["yardsale"].delete {
            eq("id", yardSaleID)
        }
    }

    override suspend fun addYardSale(yardSaleInsertDTO: YardSaleInsertDTO) {
        DBConnection.client.postgrest["yardsale"].insert(yardSaleInsertDTO, returning = Returning.MINIMAL)
    }
}