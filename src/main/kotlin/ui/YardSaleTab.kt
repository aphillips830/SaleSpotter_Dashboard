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
import models.YardSale

@Composable
fun YardSaleTab(
    yardSaleTableSearchText: MutableState<String>,
    filteredYardSaleList: SnapshotStateList<YardSale>,
    getFilteredYardSaleList: () -> Unit,
    totalYardSales: MutableIntState,
    yardSalesNext30: MutableIntState,
    yardSalesNext90: MutableIntState,
    yardSalesNext365: MutableIntState,
    categoryData: SnapshotStateMap<String, Int>,
    deleteYardSale: (yardSale: YardSale) -> Unit,
    addStartDate: MutableState<String>,
    addStartTime: MutableState<String>,
    addEndDate: MutableState<String>,
    addInformation: MutableState<String>,
    addAddress: MutableState<String>,
    addCity: MutableState<String>,
    addState: MutableState<String>,
    addPostcode: MutableState<String>,
    addYardSale: () -> Unit
) {
    val showAddYardSaleDialog = remember { mutableStateOf(false) }
    val yardSaleValidated = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        UISpacer()
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            TotalYardSalesCard(totalYardSales)
            NextYardSalesCard(yardSalesNext30, yardSalesNext90, yardSalesNext365)
            CategoryCard(categoryData)
        }
        UISpacer()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = yardSaleTableSearchText.value,
                onValueChange = {
                    yardSaleTableSearchText.value = it
                    getFilteredYardSaleList() },
                label = { Text(text = "Search") },
                singleLine = true,
                leadingIcon = { Image(painterResource("search.svg"), contentDescription = null) },
                shape = CutCornerShape(10.dp),
                modifier = Modifier.padding(0.dp, 8.dp)
            )
            FloatingActionButton(
                onClick = { showAddYardSaleDialog.value = true }
            ) {
                Icon(painter = painterResource("add.svg"), contentDescription = "Add yard sale.")
            }
        }
        YardSaleTable(filteredYardSaleList = filteredYardSaleList, deleteYardSale = deleteYardSale)
        when {
            showAddYardSaleDialog.value -> {
                AlertDialog(
                    title = { Text(text = "Add Yard Sale") },
                    icon = { Icon(painterResource("add.svg"), contentDescription = null) },
                    text = {
                        AddYardSale(addStartDate, addStartTime, addEndDate, addInformation, addAddress, addCity,
                            addState, addPostcode, yardSaleValidated)
                    },
                    onDismissRequest = { /* Do nothing */ },
                    confirmButton = {
                        TextButton(onClick = {
                            addYardSale()
                            showAddYardSaleDialog.value = false
                        },
                            enabled = yardSaleValidated.value
                        ) {
                            Text(text = "Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddYardSaleDialog.value = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryCard(categoryData: SnapshotStateMap<String, Int>) {
    val categoryDataSorted = categoryData.entries.sortedByDescending { it.value }.associate { it.toPair() }
    ElevatedCard(
        shape = CutCornerShape(10.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Yard Sales by Category",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            FlowRow {
                categoryDataSorted.forEach {
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
private fun NextYardSalesCard(
    yardSalesNext30: MutableIntState,
    yardSalesNext90: MutableIntState,
    yardSalesNext365: MutableIntState
) {
    ElevatedCard(
        shape = CutCornerShape(10.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Yard Sales",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Next 30 days:  ",
                    fontSize = 18.sp
                )
                Text(
                    text = yardSalesNext30.intValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    text = "Next 90 days:  ",
                    fontSize = 18.sp
                )
                Text(
                    text = yardSalesNext90.intValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    text = "Next 365 days:  ",
                    fontSize = 18.sp
                )
                Text(
                    text = yardSalesNext365.intValue.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
            }
        }
    }
}

@Composable
private fun TotalYardSalesCard(totalYardSales: MutableIntState) {
    ElevatedCard(
        shape = CutCornerShape(10.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Total Yard Sales",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            Text(
                text = totalYardSales.intValue.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun YardSaleTable(
    filteredYardSaleList: SnapshotStateList<YardSale>,
    deleteYardSale: (yardSale: YardSale) -> Unit
) {
    val userIDWidth = 75.dp
    val addressWidth = 250.dp
    val cityWidth = 200.dp
    val stateWidth = 150.dp
    val postcodeWidth = 125.dp
    val startDateWidth = 125.dp
    val startTimeWidth = 100.dp
    val informationWidth = 125.dp
    val categoriesWidth = 100.dp
    val addedDateTimeWidth = 200.dp
    val updatedDateTimeWidth = 200.dp
    Column {
        Divider(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp)) {
            TableHeaderText(text = "User ID", width = userIDWidth)
            TableHeaderText(text = "Street Address", width = addressWidth)
            TableHeaderText(text = "City", width = cityWidth)
            TableHeaderText(text = "State", width = stateWidth)
            TableHeaderText(text = "Postcode", width = postcodeWidth)
            TableHeaderText(text = "Start Date", width = startDateWidth)
            TableHeaderText(text = "Start Time", width = startTimeWidth)
            TableHeaderText(text = "Information", width = informationWidth)
            TableHeaderText(text = "Categories", width = categoriesWidth)
            TableHeaderText(text = "Date Added", width = addedDateTimeWidth)
            TableHeaderText(text = "Last Updated", width = updatedDateTimeWidth)
        }
        Divider(modifier = Modifier.height(4.dp))
    }
    LazyColumn {
        items(filteredYardSaleList.size) {
            val categoryList = getCategoryList(filteredYardSaleList[it])
            if (it % 2 == 0) {
                YardSaleTableRow(
                    userID = filteredYardSaleList[it].userID.toString(), userIDWidth = userIDWidth,
                    address = filteredYardSaleList[it].streetAddress, addressWidth = addressWidth,
                    city = filteredYardSaleList[it].city, cityWidth = cityWidth,
                    state = filteredYardSaleList[it].state, stateWidth = stateWidth,
                    postcode = filteredYardSaleList[it].postcode, postcodeWidth = postcodeWidth,
                    startDate = filteredYardSaleList[it].startDate, startDateWidth = startDateWidth,
                    startTime = filteredYardSaleList[it].startTime, startTimeWidth = startTimeWidth,
                    information = filteredYardSaleList[it].information, informationWidth = informationWidth,
                    categories = categoryList, categoriesWidth = categoriesWidth,
                    addedDate = filteredYardSaleList[it].dateAdded, addedDateWidth = addedDateTimeWidth,
                    lastUpdated = filteredYardSaleList[it].dateLastUpdated, lastUpdatedWidth = updatedDateTimeWidth,
                    deleteYardSale = { deleteYardSale(filteredYardSaleList[it]) },
                    rowColor = TABLECOLORONE
                )
            }
            else {
                YardSaleTableRow(
                    userID = filteredYardSaleList[it].userID.toString(), userIDWidth = userIDWidth,
                    address = filteredYardSaleList[it].streetAddress, addressWidth = addressWidth,
                    city = filteredYardSaleList[it].city, cityWidth = cityWidth,
                    state = filteredYardSaleList[it].state, stateWidth = stateWidth,
                    postcode = filteredYardSaleList[it].postcode, postcodeWidth = postcodeWidth,
                    startDate = filteredYardSaleList[it].startDate, startDateWidth = startDateWidth,
                    startTime = filteredYardSaleList[it].startTime, startTimeWidth = startTimeWidth,
                    information = filteredYardSaleList[it].information, informationWidth = informationWidth,
                    categories = categoryList, categoriesWidth = categoriesWidth,
                    addedDate = filteredYardSaleList[it].dateAdded, addedDateWidth = addedDateTimeWidth,
                    lastUpdated = filteredYardSaleList[it].dateLastUpdated, lastUpdatedWidth = updatedDateTimeWidth,
                    deleteYardSale = { deleteYardSale(filteredYardSaleList[it]) },
                    rowColor = TABLECOLORTWO
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun YardSaleTableRow(
    userID: String, userIDWidth: Dp,
    address: String, addressWidth: Dp,
    city: String, cityWidth: Dp,
    state: String, stateWidth: Dp,
    postcode: String, postcodeWidth: Dp,
    startDate: String, startDateWidth: Dp,
    startTime: String, startTimeWidth: Dp,
    information: String, informationWidth: Dp,
    categories: List<String>, categoriesWidth: Dp,
    addedDate: String, addedDateWidth: Dp,
    lastUpdated: String, lastUpdatedWidth: Dp,
    deleteYardSale: () -> Unit,
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
            TableRowText(text = userID, width = userIDWidth)
            TableRowText(text = address, width = addressWidth)
            TableRowText(text = city, width = cityWidth)
            TableRowText(text = state, width = stateWidth)
            TableRowText(text = postcode, width = postcodeWidth)
            TableRowText(text = startDate, width = startDateWidth)
            TableRowText(text = startTime, width = startTimeWidth)
            InformationToolTipArea(information = information, informationWidth = informationWidth)
            CategoryListToolTipArea(categoryList = categories, categoriesWidth = categoriesWidth)
            TableRowText(text = addedDate, width = addedDateWidth)
            TableRowText(text = lastUpdated, width = lastUpdatedWidth)
            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp).clickable { confirmDeleteAlert.value = true },
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Delete yard sale",
                            color = Color.Red,
                            modifier = Modifier.padding(20.dp, 4.dp, 8.dp, 4.dp)
                        )
                    }
                },
                delayMillis = 500,
                tooltipPlacement = TooltipPlacement.CursorPoint(
                    alignment = Alignment.BottomEnd,
                    offset = DpOffset.Zero
                )
            ) {
                Icon(
                    painter = painterResource("delete.svg"),
                    contentDescription = "Delete yard sale.",
                    modifier = Modifier.clickable { confirmDeleteAlert.value = true }
                )
            }
            when {
                confirmDeleteAlert.value -> {
                    AlertDialog(
                        onDismissRequest = { /* Do nothing */ },
                        confirmButton = {
                            TextButton(onClick = {
                                deleteYardSale()
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
                        title = { Text(text = "Delete Yard Sale") }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoryListToolTipArea(categoryList: List<String>, categoriesWidth: Dp) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(4.dp),
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Categories:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
                    )
                    categoryList.forEach {
                        Text(
                            text = it,
                            color = Color.Black
                        )
                    }
                }
            }
        },
        modifier = Modifier.width(categoriesWidth),
        delayMillis = 500,
        tooltipPlacement = TooltipPlacement.CursorPoint(
            alignment = Alignment.BottomEnd,
            offset = DpOffset.Zero
        )
    ) {
        Icon(painterResource("info.svg"), contentDescription = "Categories in sale.")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InformationToolTipArea(information: String, informationWidth: Dp) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(4.dp),
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Additional Details:",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
                    )
                    Text(
                        text = information,
                        color = Color.Black
                    )
                }
            }
        },
        modifier = Modifier.width(informationWidth),
        delayMillis = 500,
        tooltipPlacement = TooltipPlacement.CursorPoint(
            alignment = Alignment.BottomEnd,
            offset = DpOffset.Zero
        )
    ) {
        Icon(painterResource("info.svg"), contentDescription = "Additional details.")
    }
}

private fun getCategoryList(yardSale: YardSale): List<String> {
    val categoriesList: MutableList<String> = mutableListOf()
    if (yardSale.hasWomensClothes)
        categoriesList.add("Women's Clothes")
    if (yardSale.hasWomensShoes)
        categoriesList.add("Women's Shoes")
    if (yardSale.hasMensClothes)
        categoriesList.add("Men's Clothes")
    if (yardSale.hasMensShoes)
        categoriesList.add("Men's Shoes")
    if (yardSale.hasGirlsClothes)
        categoriesList.add("Girls Clothes")
    if (yardSale.hasGirlsShoes)
        categoriesList.add("Girls Shoes")
    if (yardSale.hasBoysClothes)
        categoriesList.add("Boys Clothes")
    if (yardSale.hasBoysShoes)
        categoriesList.add("Boys Shoes")
    if (yardSale.hasBabyClothes)
        categoriesList.add("Baby Clothes")
    if (yardSale.hasJewelry)
        categoriesList.add("Jewelry")
    if (yardSale.hasCampingGear)
        categoriesList.add("Camping Gear")
    if (yardSale.hasHouseWare)
        categoriesList.add("House Ware")
    if (yardSale.hasKitchenWare)
        categoriesList.add("Kitchen Ware")
    if (yardSale.hasFurniture)
        categoriesList.add("Furniture")
    if (yardSale.hasAntiquesCollectables)
        categoriesList.add("Antiques/Collectables")
    if (yardSale.hasAppliances)
        categoriesList.add("Appliances")
    if (yardSale.hasBooks)
        categoriesList.add("Books")
    if (yardSale.hasMoviesMusic)
        categoriesList.add("Movies/Music")
    if (yardSale.hasArtsCrafts)
        categoriesList.add("Arts/Crafts")
    if (yardSale.hasAutoParts)
        categoriesList.add("Auto Parts")
    if (yardSale.hasHealthBeauty)
        categoriesList.add("Health/Beauty")
    if (yardSale.hasHomeDecoration)
        categoriesList.add("Home Decorations")
    if (yardSale.hasLawnGarden)
        categoriesList.add("Lawn/Garden")
    if (yardSale.hasTools)
        categoriesList.add("Tools")
    if (yardSale.hasMusicalInstruments)
        categoriesList.add("Musical Instruments")
    if (yardSale.hasPetSupplies)
        categoriesList.add("Pet Supplies")
    if (yardSale.hasSportingGoods)
        categoriesList.add("Sporting Goods")
    if (yardSale.hasToysGames)
        categoriesList.add("Toys/Games")
    if (yardSale.hasFishingGear)
        categoriesList.add("Fishing Gear")
    if (yardSale.hasFirearmsAmmo)
        categoriesList.add("Firearms/Ammunition")
    if (yardSale.hasElectronics)
        categoriesList.add("Electronics")
    return categoriesList
}