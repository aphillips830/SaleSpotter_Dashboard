import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.ap.SS.BuildConfig
import data.YardSaleDTO
import data.YardSaleInsertDTO
import data.YardSaleRepository
import data.getLatitudeAndLongitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import models.YardSale
import utility.YardSaleCategory
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds

class YardSaleController(private val yardSaleRepository: YardSaleRepository) {

    val yardSaleTableSearchText: MutableState<String> = mutableStateOf("")
    val filteredYardSaleList: SnapshotStateList<YardSale> = mutableStateListOf()
    val totalYardSales: MutableIntState = mutableIntStateOf(0)
    val yardSalesNext30: MutableIntState = mutableIntStateOf(0)
    val yardSalesNext90: MutableIntState = mutableIntStateOf(0)
    val yardSalesNext365: MutableIntState = mutableIntStateOf(0)
    val yardSalesByCategoryData: SnapshotStateMap<String, Int> = mutableStateMapOf()

    private val allYardSales: MutableList<YardSale> = mutableListOf()

    val addStartDate: MutableState<String> = mutableStateOf(LocalDate.now().format(DATEFORMAT))
    val addStartTime: MutableState<String> = mutableStateOf(LocalTime.now().format(TIMEFORMAT))
    val addEndDate: MutableState<String> = mutableStateOf(LocalDate.now().format(DATEFORMAT))
    val addInformation: MutableState<String> = mutableStateOf("")
    val addAddress: MutableState<String> = mutableStateOf("")
    val addCity: MutableState<String> = mutableStateOf("")
    val addState: MutableState<String> = mutableStateOf("")
    val addPostcode: MutableState<String> = mutableStateOf("")

    init {
        getYardSaleData()
    }

    private fun getYardSaleData() {
        resetData()
        val allDBYardSales = MutableStateFlow<List<YardSale>?>(listOf())
        CoroutineScope(Dispatchers.IO).launch {
            val dbYardSales = yardSaleRepository.getAllYardSales()
            allDBYardSales.emit(dbYardSales.map { it.asDomainModel() })
            allDBYardSales.collect { list ->
                list?.forEach { yardSale ->
                    allYardSales.add(yardSale)
                    ++totalYardSales.intValue
                    getYardSalesNext3090365Days(yardSale)
                    getYardSalesByCategory(yardSale)
                    getFilteredYardSaleList()
                }
            }
        }
    }

    private fun resetData() {
        allYardSales.clear()
        totalYardSales.value = 0
        yardSalesNext30.value = 0
        yardSalesNext90.value = 0
        yardSalesNext365.value = 0
        yardSalesByCategoryData.clear()
    }

    private fun getYardSalesByCategory(yardSale: YardSale) {
        if (yardSale.hasWomensClothes)
            yardSalesByCategoryData.merge("Women's Clothes", 1, Int::plus)
        if (yardSale.hasWomensShoes)
            yardSalesByCategoryData.merge("Women's Shoes", 1, Int::plus)
        if (yardSale.hasMensClothes)
            yardSalesByCategoryData.merge("Men's Clothes", 1, Int::plus)
        if (yardSale.hasMensShoes)
            yardSalesByCategoryData.merge("Men's Shoes", 1, Int::plus)
        if (yardSale.hasGirlsClothes)
            yardSalesByCategoryData.merge("Girls Clothes", 1, Int::plus)
        if (yardSale.hasGirlsShoes)
            yardSalesByCategoryData.merge("Girls Shoes", 1, Int::plus)
        if (yardSale.hasBoysClothes)
            yardSalesByCategoryData.merge("Boys Clothes", 1, Int::plus)
        if (yardSale.hasBoysShoes)
            yardSalesByCategoryData.merge("Boys Shoes", 1, Int::plus)
        if (yardSale.hasBabyClothes)
            yardSalesByCategoryData.merge("Baby Clothes", 1, Int::plus)
        if (yardSale.hasJewelry)
            yardSalesByCategoryData.merge("Jewelry", 1, Int::plus)
        if (yardSale.hasCampingGear)
            yardSalesByCategoryData.merge("Camping Gear", 1, Int::plus)
        if (yardSale.hasHouseWare)
            yardSalesByCategoryData.merge("House Ware", 1, Int::plus)
        if (yardSale.hasKitchenWare)
            yardSalesByCategoryData.merge("Kitchen Ware", 1, Int::plus)
        if (yardSale.hasFurniture)
            yardSalesByCategoryData.merge("Furniture", 1, Int::plus)
        if (yardSale.hasAntiquesCollectables)
            yardSalesByCategoryData.merge("Antiques/Collectables", 1, Int::plus)
        if (yardSale.hasAppliances)
            yardSalesByCategoryData.merge("Appliances", 1, Int::plus)
        if (yardSale.hasBooks)
            yardSalesByCategoryData.merge("Books", 1, Int::plus)
        if (yardSale.hasMoviesMusic)
            yardSalesByCategoryData.merge("Movies/Music", 1, Int::plus)
        if (yardSale.hasArtsCrafts)
            yardSalesByCategoryData.merge("Arts/Crafts", 1, Int::plus)
        if (yardSale.hasAutoParts)
            yardSalesByCategoryData.merge("Auto Parts", 1, Int::plus)
        if (yardSale.hasHealthBeauty)
            yardSalesByCategoryData.merge("Health/Beauty", 1, Int::plus)
        if (yardSale.hasHomeDecoration)
            yardSalesByCategoryData.merge("Home Decorations", 1, Int::plus)
        if (yardSale.hasLawnGarden)
            yardSalesByCategoryData.merge("Lawn/Garden", 1, Int::plus)
        if (yardSale.hasTools)
            yardSalesByCategoryData.merge("Tools", 1, Int::plus)
        if (yardSale.hasMusicalInstruments)
            yardSalesByCategoryData.merge("Musical Instruments", 1, Int::plus)
        if (yardSale.hasPetSupplies)
            yardSalesByCategoryData.merge("Pet Supplies", 1, Int::plus)
        if (yardSale.hasSportingGoods)
            yardSalesByCategoryData.merge("Sporting Goods", 1, Int::plus)
        if (yardSale.hasToysGames)
            yardSalesByCategoryData.merge("Toys/Games", 1, Int::plus)
        if (yardSale.hasFishingGear)
            yardSalesByCategoryData.merge("Fishing Gear", 1, Int::plus)
        if (yardSale.hasFirearmsAmmo)
            yardSalesByCategoryData.merge("Firearms/Ammunition", 1, Int::plus)
        if (yardSale.hasElectronics)
            yardSalesByCategoryData.merge("Electronics", 1, Int::plus)
    }

    private fun getYardSalesNext3090365Days(yardSale: YardSale) {
        val today: LocalDate = LocalDate.now()
        val localSaleDate: LocalDate = LocalDate.parse(yardSale.startDate, DATEFORMAT)
        val daysUntil = ChronoUnit.DAYS.between(today, localSaleDate)
        if (daysUntil <= 30)
            ++yardSalesNext30.intValue
        if (daysUntil <= 90)
            ++yardSalesNext90.intValue
        if (daysUntil <= 365)
            ++yardSalesNext365.intValue
    }

    fun deleteYardSale(yardSale: YardSale) {
        CoroutineScope(Dispatchers.IO).launch {
            yardSaleRepository.deleteYardSale(yardSale.yardSaleID)
            delay(DBDELAY.milliseconds)
            getYardSaleData()
        }
    }

    fun addYardSale() {
        val address = StringBuilder()
        address.append(addAddress.value)
            .append(" ")
            .append(addCity.value)
            .append(" ")
            .append(addState.value)
            .append(" ")
            .append(addPostcode.value)
        val apiKey = BuildConfig.GOOGLE_MAPS_KEY
        CoroutineScope(Dispatchers.IO).launch {
            val (latitude, longitude) = getLatitudeAndLongitude(address = address.toString(), apiKey)
            val yardSaleToInsert = YardSaleInsertDTO(
                userID = 1,
                startDate = addStartDate.value,
                startTime = addStartTime.value,
                endDate = addEndDate.value,
                information = addInformation.value,
                hasWomensClothes = YardSaleCategory.WOMENS_CLOTHES.inSale.value,
                hasWomensShoes = YardSaleCategory.WOMENS_SHOES.inSale.value,
                hasMensClothes = YardSaleCategory.MENS_CLOTHES.inSale.value,
                hasMensShoes = YardSaleCategory.MENS_SHOES.inSale.value,
                hasGirlsClothes = YardSaleCategory.GIRLS_CLOTHES.inSale.value,
                hasGirlsShoes = YardSaleCategory.GIRLS_SHOES.inSale.value,
                hasBoysClothes = YardSaleCategory.BOYS_CLOTHES.inSale.value,
                hasBoysShoes = YardSaleCategory.BOYS_SHOES.inSale.value,
                hasBabyClothes = YardSaleCategory.BABY_CLOTHES.inSale.value,
                hasJewelry = YardSaleCategory.JEWELRY.inSale.value,
                hasCampingGear = YardSaleCategory.CAMPING_GEAR.inSale.value,
                hasHouseWare = YardSaleCategory.HOUSE_WARE.inSale.value,
                hasKitchenWare = YardSaleCategory.KITCHEN_WARE.inSale.value,
                hasFurniture = YardSaleCategory.FURNITURE.inSale.value,
                hasAntiquesCollectables = YardSaleCategory.ANTIQUES_COLLECTABLES.inSale.value,
                hasAppliances = YardSaleCategory.APPLIANCES.inSale.value,
                hasBooks = YardSaleCategory.BOOKS.inSale.value,
                hasMoviesMusic = YardSaleCategory.MOVIES_MUSIC.inSale.value,
                hasArtsCrafts = YardSaleCategory.ARTS_CRAFTS.inSale.value,
                hasAutoParts = YardSaleCategory.AUTO_PARTS.inSale.value,
                hasHealthBeauty = YardSaleCategory.HEALTH_BEAUTY.inSale.value,
                hasHomeDecoration = YardSaleCategory.HOME_DECORATIONS.inSale.value,
                hasLawnGarden = YardSaleCategory.LAWN_GARDEN.inSale.value,
                hasTools = YardSaleCategory.TOOLS.inSale.value,
                hasMusicalInstruments = YardSaleCategory.MUSICAL_INSTRUMENTS.inSale.value,
                hasPetSupplies = YardSaleCategory.PET_SUPPLIES.inSale.value,
                hasSportingGoods = YardSaleCategory.SPORTING_GOODS.inSale.value,
                hasToysGames = YardSaleCategory.TOYS_GAMES.inSale.value,
                hasFishingGear = YardSaleCategory.FISHING_GEAR.inSale.value,
                hasFirearmsAmmo = YardSaleCategory.FIREARMS_AMMO.inSale.value,
                hasElectronics = YardSaleCategory.ELECTRONICS.inSale.value,
                streetAddress = addAddress.value.trim(),
                city = addCity.value.trim(),
                state = addState.value.trim(),
                postcode = addPostcode.value.trim(),
                latitude = latitude,
                longitude = longitude
            )
            yardSaleRepository.addYardSale(yardSaleToInsert)
            resetAddYardSaleFields()
            delay(DBDELAY.milliseconds)
            getYardSaleData()
        }
    }

    private fun resetAddYardSaleFields() {
        addStartDate.value = LocalDate.now().format(DATEFORMAT)
        addStartTime.value = LocalTime.now().format(TIMEFORMAT)
        addEndDate.value = LocalDate.now().format(DATEFORMAT)
        addInformation.value = ""
        addAddress.value = ""
        addCity.value = ""
        addState.value = ""
        addPostcode.value = ""
        for (category in YardSaleCategory.entries) {
            category.inSale.value = false
        }
    }

    fun getFilteredYardSaleList() {
        filteredYardSaleList.clear()
        val search = yardSaleTableSearchText.value.lowercase()
        for (yardSale in allYardSales) {
            if (search in yardSale.userID.toString() ||
                search in yardSale.streetAddress.lowercase() ||
                search in yardSale.city.lowercase() ||
                search in yardSale.state.lowercase() ||
                search in yardSale.postcode.lowercase() ||
                search in yardSale.dateAdded.lowercase() ||
                search in yardSale.dateLastUpdated.lowercase() ||
                search in yardSale.information.lowercase() ||
                search in yardSale.startDate.lowercase()) {
                filteredYardSaleList.add(yardSale)
            }
        }
    }

    private fun YardSaleDTO.asDomainModel(): YardSale {
        return YardSale(
            yardSaleID = this.yardSaleID,
            userID = this.userID,
            startDate = LocalDate.parse(this.startDate).format(DATEFORMAT),
            startTime = this.startTime,
            endDate = this.endDate,
            information = this.information,
            hasWomensClothes = this.hasWomensClothes,
            hasWomensShoes = this.hasWomensShoes,
            hasMensClothes = this.hasMensClothes,
            hasMensShoes = this.hasMensShoes,
            hasGirlsClothes = this.hasGirlsClothes,
            hasGirlsShoes = this.hasGirlsShoes,
            hasBoysClothes = this.hasBoysClothes,
            hasBoysShoes = this.hasBoysShoes,
            hasBabyClothes = this.hasBabyClothes,
            hasJewelry = this.hasJewelry,
            hasCampingGear = this.hasCampingGear,
            hasHouseWare = this.hasHouseWare,
            hasKitchenWare = this.hasKitchenWare,
            hasFurniture = this.hasFurniture,
            hasAntiquesCollectables = this.hasAntiquesCollectables,
            hasAppliances = this.hasAppliances,
            hasBooks = this.hasBooks,
            hasMoviesMusic = this.hasMoviesMusic,
            hasArtsCrafts = this.hasArtsCrafts,
            hasAutoParts = this.hasAutoParts,
            hasHealthBeauty = this.hasHealthBeauty,
            hasHomeDecoration = this.hasHomeDecoration,
            hasLawnGarden = this.hasLawnGarden,
            hasTools = this.hasTools,
            hasMusicalInstruments = this.hasMusicalInstruments,
            hasPetSupplies = this.hasPetSupplies,
            hasSportingGoods = this.hasSportingGoods,
            hasToysGames = this.hasToysGames,
            hasFishingGear = this.hasFishingGear,
            hasFirearmsAmmo = this.hasFirearmsAmmo,
            hasElectronics = this.hasElectronics,
            dateAdded = OffsetDateTime.parse(this.dateAdded).atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime().format(DATETIMEFORMAT),
            dateLastUpdated = OffsetDateTime.parse(this.dateLastUpdated).atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime().format(DATETIMEFORMAT),
            streetAddress = this.streetAddress,
            city = this.city,
            state = this.state,
            postcode = this.postcode,
            latitude = this.latitude,
            longitude = this.longitude
        )
    }

}