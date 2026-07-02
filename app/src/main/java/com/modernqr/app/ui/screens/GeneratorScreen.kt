package com.modernqr.app.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

import androidx.lifecycle.viewmodel.compose.viewModel
import com.modernqr.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen(viewModel: MainViewModel = viewModel()) {
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedType by remember { mutableStateOf("Text") }
    val types = listOf("Text", "URL", "WiFi", "Location", "Contact")

    // State for inputs
    var textInput by remember { mutableStateOf("") }
    
    // WiFi state
    var ssidInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var wifiType by remember { mutableStateOf("WPA") }
    var expandedWifiType by remember { mutableStateOf(false) }
    val wifiTypes = listOf("WPA", "WEP", "nopass")
    
    // Location state
    var latInput by remember { mutableStateOf("") }
    var lngInput by remember { mutableStateOf("") }
    
    // Contact state
    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScrollableTabRow(
            selectedTabIndex = types.indexOf(selectedType),
            edgePadding = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            types.forEach { type ->
                Tab(
                    selected = selectedType == type,
                    onClick = { selectedType = type; qrBitmap = null },
                    text = { Text(type) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedType) {
            "Text" -> {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Enter text") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "URL" -> {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Enter URL (e.g., https://...)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "WiFi" -> {
                OutlinedTextField(
                    value = ssidInput,
                    onValueChange = { ssidInput = it },
                    label = { Text("Network Name (SSID)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedWifiType,
                    onExpandedChange = { expandedWifiType = it }
                ) {
                    OutlinedTextField(
                        value = wifiType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Security Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedWifiType) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedWifiType,
                        onDismissRequest = { expandedWifiType = false }
                    ) {
                        wifiTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    wifiType = type
                                    expandedWifiType = false
                                }
                            )
                        }
                    }
                }
            }
            "Location" -> {
                OutlinedTextField(
                    value = latInput,
                    onValueChange = { latInput = it },
                    label = { Text("Latitude") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lngInput,
                    onValueChange = { lngInput = it },
                    label = { Text("Longitude") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "Contact" -> {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val contentToEncode = when (selectedType) {
                "Text", "URL" -> textInput
                "WiFi" -> "WIFI:S:$ssidInput;T:$wifiType;P:$passwordInput;;"
                "Location" -> "geo:$latInput,$lngInput"
                "Contact" -> "BEGIN:VCARD\nVERSION:3.0\nN:$nameInput\nTEL:$phoneInput\nEMAIL:$emailInput\nEND:VCARD"
                else -> ""
            }

            if (contentToEncode.isNotEmpty()) {
                try {
                    val writer = MultiFormatWriter()
                    val matrix = writer.encode(contentToEncode, BarcodeFormat.QR_CODE, 400, 400)
                    val encoder = BarcodeEncoder()
                    qrBitmap = encoder.createBitmap(matrix)
                    viewModel.addHistoryItem(contentToEncode, "QR_CODE", "Generated")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Generate QR Code")
        }
        Spacer(modifier = Modifier.height(32.dp))
        
        qrBitmap?.let { bitmap ->
            Card(
                modifier = Modifier.size(250.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Generated QR Code",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
