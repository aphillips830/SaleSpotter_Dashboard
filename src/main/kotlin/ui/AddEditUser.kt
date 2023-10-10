package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun AddEditUser(
    name: MutableState<String>,
    address: MutableState<String>,
    city: MutableState<String>,
    state: MutableState<String>,
    postcode: MutableState<String>,
    email: MutableState<String>,
    userValidated: MutableState<Boolean>
) {
    val statesExpanded: MutableState<Boolean> = mutableStateOf(false)
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    nameError = name.value == ""
    emailError = email.value == ""
    userValidated.value = (!nameError && !emailError)
    Column {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text(text = "Name") },
            isError = nameError,
            supportingText = { if (nameError) { Text(text = "Name Required") } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = address.value,
            onValueChange = { address.value = it },
            label = { Text(text = "Address") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = city.value,
            onValueChange = { city.value = it },
            label = { Text(text = "City") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        StateSelector(state, statesExpanded, false)
        OutlinedTextField(
            value = postcode.value,
            onValueChange = { postcode.value = it },
            label = { Text(text = "Postcode") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(text = "Email") },
            isError = emailError,
            supportingText = { if (emailError) { Text(text = "Name Required") } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth()
        )
    }
}