package data

interface YardSaleRepository {
    suspend fun getAllYardSales(): List<YardSaleDTO>
    suspend fun deleteYardSale(yardSaleID: Long)
    suspend fun addYardSale(yardSaleInsertDTO: YardSaleInsertDTO)
}