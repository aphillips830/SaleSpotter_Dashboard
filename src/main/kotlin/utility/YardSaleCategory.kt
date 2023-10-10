package utility

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

enum class YardSaleCategory(val text: String, var inSale: MutableState<Boolean> = mutableStateOf(false)) {
    WOMENS_CLOTHES("Women's Clothes"),
    WOMENS_SHOES("Women's Shoes"),
    MENS_CLOTHES("Men's Clothes"),
    MENS_SHOES("Men's Shoes"),
    GIRLS_CLOTHES("Girl's Clothes"),
    GIRLS_SHOES("Girl's Shoes"),
    BOYS_CLOTHES("Boy's Clothes"),
    BOYS_SHOES("Boy's Shoes"),
    BABY_CLOTHES("Baby Clothes"),
    JEWELRY("Jewelry"),
    CAMPING_GEAR("Camping Gear"),
    HOUSE_WARE("House Ware"),
    KITCHEN_WARE("Kitchen Ware"),
    FURNITURE("Furniture"),
    ANTIQUES_COLLECTABLES("Antiques or Collectables"),
    APPLIANCES("Appliances"),
    BOOKS("Books"),
    MOVIES_MUSIC("Movies or Music"),
    ARTS_CRAFTS("Arts or Crafts"),
    AUTO_PARTS("Auto Parts"),
    HEALTH_BEAUTY("Health or Beauty"),
    HOME_DECORATIONS("Home Decorations"),
    LAWN_GARDEN("Lawn or Garden"),
    TOOLS("Tools"),
    MUSICAL_INSTRUMENTS("Musical Instruments"),
    PET_SUPPLIES("Pet Supplies"),
    SPORTING_GOODS("Sporting Goods"),
    TOYS_GAMES("Toys or Games"),
    FISHING_GEAR("Fishing Gear"),
    FIREARMS_AMMO("Firearms or Ammunition"),
    ELECTRONICS("Electronics")
}