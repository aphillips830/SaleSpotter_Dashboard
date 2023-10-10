package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utility.US_STATES

@Composable
fun TableHeaderText(text: String, width: Dp) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(width)
    )
}

@Composable
fun TableRowText(text: String, width: Dp) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier.width(width)
    )
}

@Composable
fun UISpacer() {
    Spacer(modifier = Modifier.height(48.dp))
}

@Composable
fun StateSelector(
    state: MutableState<String>,
    expanded: MutableState<Boolean>,
    isError: Boolean
) {
    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        OutlinedTextField(
            value = state.value,
            onValueChange = { },
            label = { Text(text = "State") },
            trailingIcon = {
                if (expanded.value)
                    Icon(painter = painterResource("expand_less.svg"), "")
                else
                    Icon(painter = painterResource("expand_more.svg"), "")
            },
            isError = isError,
            supportingText = { if (isError) { Text(text = "State Required") } },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Surface(modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.extraSmall)
            .padding(top = 8.dp, bottom = 16.dp)
            .clickable { expanded.value = true }, color = Color.Transparent) {}
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            offset = DpOffset(250.dp, 0.dp)
        ) {
            US_STATES.forEach { usState ->
                DropdownMenuItem(
                    text = { Text(text = usState) },
                    onClick = {
                        state.value = usState
                        expanded.value = false
                    },
                    modifier = Modifier.padding(16.dp, 0.dp)
                )
            }
        }
    }
}