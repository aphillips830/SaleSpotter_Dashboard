package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YardSaleDTO(
    @SerialName("id")
    val yardSaleID: Long,
    @SerialName("user_id")
    val userID: Long,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("information")
    val information: String,
    @SerialName("has_womens_clothes")
    val hasWomensClothes: Boolean,
    @SerialName("has_womens_shoes")
    val hasWomensShoes: Boolean,
    @SerialName("has_mens_clothes")
    val hasMensClothes: Boolean,
    @SerialName("has_mens_shoes")
    val hasMensShoes: Boolean,
    @SerialName("has_girls_clothes")
    val hasGirlsClothes: Boolean,
    @SerialName("has_girls_shoes")
    val hasGirlsShoes: Boolean,
    @SerialName("has_boys_clothes")
    val hasBoysClothes: Boolean,
    @SerialName("has_boys_shoes")
    val hasBoysShoes: Boolean,
    @SerialName("has_baby_clothes")
    val hasBabyClothes: Boolean,
    @SerialName("has_jewelry")
    val hasJewelry: Boolean,
    @SerialName("has_camping_gear")
    val hasCampingGear: Boolean,
    @SerialName("has_house_ware")
    val hasHouseWare: Boolean,
    @SerialName("has_kitchen_ware")
    val hasKitchenWare: Boolean,
    @SerialName("has_furniture")
    val hasFurniture: Boolean,
    @SerialName("has_antiques_collectables")
    val hasAntiquesCollectables: Boolean,
    @SerialName("has_appliances")
    val hasAppliances: Boolean,
    @SerialName("has_books")
    val hasBooks: Boolean,
    @SerialName("has_movies_music")
    val hasMoviesMusic: Boolean,
    @SerialName("has_arts_crafts")
    val hasArtsCrafts: Boolean,
    @SerialName("has_auto_parts")
    val hasAutoParts: Boolean,
    @SerialName("has_health_beauty")
    val hasHealthBeauty: Boolean,
    @SerialName("has_home_decoration")
    val hasHomeDecoration: Boolean,
    @SerialName("has_lawn_garden")
    val hasLawnGarden: Boolean,
    @SerialName("has_tools")
    val hasTools: Boolean,
    @SerialName("has_musical_instruments")
    val hasMusicalInstruments: Boolean,
    @SerialName("has_pet_supplies")
    val hasPetSupplies: Boolean,
    @SerialName("has_sporting_goods")
    val hasSportingGoods: Boolean,
    @SerialName("has_toys_games")
    val hasToysGames: Boolean,
    @SerialName("has_fishing_gear")
    val hasFishingGear: Boolean,
    @SerialName("has_firearms_ammo")
    val hasFirearmsAmmo: Boolean,
    @SerialName("has_electronics")
    val hasElectronics: Boolean,
    @SerialName("inserted_at")
    val dateAdded: String?,
    @SerialName("updated_at")
    val dateLastUpdated: String?,
    @SerialName("street_address")
    val streetAddress: String,
    @SerialName("city")
    val city: String,
    @SerialName("state")
    val state: String,
    @SerialName("postcode")
    val postcode: String,
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String
)
