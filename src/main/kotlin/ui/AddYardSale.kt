package ui

import DATEFORMAT
import TIMEFORMAT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utility.US_STATES
import utility.YardSaleCategory
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddYardSale(
    startDate: MutableState<String>,
    startTime: MutableState<String>,
    endDate: MutableState<String>,
    information: MutableState<String>,
    address: MutableState<String>,
    city: MutableState<String>,
    state: MutableState<String>,
    postcode: MutableState<String>,
    yardSaleValidated: MutableState<Boolean>
) {
    val statesExpanded: MutableState<Boolean> = mutableStateOf(false)
    var addressError by remember { mutableStateOf(false) }
    var cityError by remember { mutableStateOf(false) }
    var stateError by remember { mutableStateOf(false) }
    var postcodeError by remember { mutableStateOf(false) }
    var startDateError by remember { mutableStateOf(false) }
    var endDateError by remember { mutableStateOf(false) }
    val startLocalDate: LocalDate = LocalDate.parse(startDate.value, DATEFORMAT)
    val endLocalDate: LocalDate = LocalDate.parse(endDate.value, DATEFORMAT)
    startDateError = startLocalDate.isBefore(LocalDate.now())
    endDateError = !(endLocalDate.isEqual(startLocalDate) || endLocalDate.isAfter(startLocalDate))
    addressError = address.value == ""
    cityError = city.value == ""
    stateError = state.value == ""
    postcodeError = postcode.value == ""
    yardSaleValidated.value = (!addressError && !cityError && !stateError && !postcodeError && !startDateError && !endDateError)
    Column {
        OutlinedTextField(
            value = address.value,
            onValueChange = { address.value = it },
            label = { Text(text = "Address") },
            isError = addressError,
            supportingText = { if (addressError) { Text(text = "Address Required") } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = city.value,
            onValueChange = { city.value = it },
            label = { Text(text = "City") },
            isError = cityError,
            supportingText = { if (cityError) { Text(text = "City Required") } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        StateSelector(state, statesExpanded, stateError)
        OutlinedTextField(
            value = postcode.value,
            onValueChange = { postcode.value = it },
            label = { Text(text = "Postcode") },
            isError = postcodeError,
            supportingText = { if (postcodeError) { Text(text = "Postcode Required") } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = information.value,
            onValueChange = { information.value = it },
            label = { Text(text = "Additional Details") },
            minLines = 5,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 4.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                if (startDateError)
                    Text(
                        text = "Start Date Required",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp, 4.dp)
                    )
                else
                    Text(text = "Start Date", modifier = Modifier.padding(16.dp, 4.dp))
                YardSaleDatePicker(date = startDate)
            }
            Column {
                Text(text = "Start Time", modifier = Modifier.padding(16.dp, 4.dp))
                YardSaleTimePicker(time = startTime)
            }
            Column {
                if (endDateError)
                    Text(
                        text = "End Date Required",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp, 4.dp)
                    )
                else
                    Text(text = "End Date", modifier = Modifier.padding(16.dp, 4.dp))
                YardSaleDatePicker(date = endDate)
            }
        }
        YardSaleCategoryPicker()
        Card(modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        maxItemsInEachRow = 3,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        for (category in YardSaleCategory.entries) {
                            if (category.inSale.value) {
                                ElevatedFilterChip(
                                    selected = category.inSale.value,
                                    onClick = { category.inSale.value = false },
                                    label = { Text(text = category.text, color = Color.White) },
                                    leadingIcon = {
                                        if (category.inSale.value) {
                                            Icon(painter = painterResource("check.svg"), contentDescription = null)
                                        }
                                    },
                                    modifier = Modifier.padding(4.dp, 0.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YardSaleDatePicker(date: MutableState<String>) {
    val openDatePicker = remember { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        text = { Text(text = date.value) },
        icon = { Icon(painter = painterResource("calendar.svg"), contentDescription = null) },
        onClick = { openDatePicker.value = !openDatePicker.value }
    )
    when {
        openDatePicker.value -> {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = LocalDate.parse(date.value, DATEFORMAT)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),

            )
            DatePickerDialog(
                onDismissRequest = { /* Do nothing */},
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDatePicker.value = false
                            if (datePickerState.selectedDateMillis != null) {
                                date.value = ZonedDateTime.ofInstant(
                                    Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                                    ZoneId.of("UTC")
                                ).format(DATEFORMAT)
                            }
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { openDatePicker.value = false }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YardSaleTimePicker(time: MutableState<String>) {
    val openTimePicker = remember { mutableStateOf(false) }
    ExtendedFloatingActionButton(
        text = { Text(text = time.value) },
        icon = { Icon(painter = painterResource("clock.svg"), contentDescription = null) },
        onClick = { openTimePicker.value = !openTimePicker.value }
    )
    when {
        openTimePicker.value -> {
            val timePickerState = rememberTimePickerState(
                initialHour = LocalTime.parse(time.value, TIMEFORMAT).hour,
                initialMinute = LocalTime.parse(time.value, TIMEFORMAT).minute,
                is24Hour = false
            )
            DatePickerDialog(
                onDismissRequest = { /* Do nothing */},
                confirmButton = {
                    TextButton(
                        onClick = {
                            openTimePicker.value = false
                            time.value = LocalTime.of(timePickerState.hour, timePickerState.minute).format(TIMEFORMAT)
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { openTimePicker.value = false }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                TimeInput(state = timePickerState, modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 0.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun YardSaleCategoryPicker() {
    val openCategoryPicker = remember { mutableStateOf(false) }
    val fabText = remember { mutableStateOf("Categories in Yard Sale")}
    ExtendedFloatingActionButton(
        text = { Text(text = fabText.value, fontSize = 16.sp) },
        icon = { /* No icon */ },
        onClick = { openCategoryPicker.value = !openCategoryPicker.value },
        modifier = Modifier.fillMaxWidth().padding(0.dp, 16.dp)
    )
    when {
        openCategoryPicker.value -> {
            AlertDialog(
                title = { Text(text = "Select Categories") },
                text = {
                    Card(modifier = Modifier.padding(0.dp, 16.dp)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            maxItemsInEachRow = 3
                        ) {
                            for (category in YardSaleCategory.entries) {
                                ElevatedFilterChip(
                                    selected = category.inSale.value,
                                    onClick = { category.inSale.value = !category.inSale.value },
                                    label = { Text(text = category.text) },
                                    leadingIcon = {
                                        if (category.inSale.value) {
                                            Icon(painter = painterResource("check.svg"), contentDescription = null)
                                        }
                                    },
                                    modifier = Modifier.padding(4.dp, 0.dp)
                                )
                            }
                        }
                    }
                },
                onDismissRequest = { /* Do nothing */ },
                confirmButton = {
                    TextButton(onClick = { openCategoryPicker.value = false }
                    ) {
                        Text(text = "Done")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openCategoryPicker.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}