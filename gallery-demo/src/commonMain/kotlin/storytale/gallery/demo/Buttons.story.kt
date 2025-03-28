package storytale.gallery.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.storytale.story

/**
 * Parses a hex color string (#RRGGBB, #AARRGGBB, or partial inputs) into a Compose Color.
 * Handles optional '#' prefix and case-insensitivity.
 * - If 1-6 hex digits are provided, assumes RGB and pads with '0' to the right
 *   to complete 6 digits, then assumes full alpha ('FF').
 * - If 7-8 hex digits are provided, assumes ARGB and pads with '0' to the right
 *   to complete 8 digits.
 * - Returns a default color if the string contains invalid hex characters,
 *   is empty/just '#', or is longer than 8 hex digits.
 *
 * Examples:
 *   "#F" -> "#FF F0 00 00" (Red, full alpha)
 *   "#123" -> "#FF 12 30 00"
 *   "#ABCDEF" -> "#FF AB CD EF"
 *   "#AABBC" -> "#AA BB C0 00" (Alpha from input, padded)
 *   "#AABBCCDD" -> "#AA BB CC DD"
 *   "#G" -> defaultColor
 *   "" -> defaultColor
 *
 * @param colorString The potentially partial or complete hex color string.
 * @param defaultColor The Color to return if parsing is not possible even leniently.
 *                     Defaults to Color.Black.
 * @return The parsed Compose Color, or the defaultColor.
 */
fun parseColorLeniently(colorString: String, defaultColor: Color = Color.Black): Color {
    // 1. Basic cleanup and initial validation
    val trimmed = colorString.trim()
    if (trimmed.isEmpty() || trimmed == "#") {
        return defaultColor
    }

    // 2. Remove prefix, standardize case
    val hex = trimmed.removePrefix("#").uppercase()

    // 3. Check for invalid characters (only 0-9, A-F allowed)
    if (!hex.all { it.isDigit() || it in 'A'..'F' }) {
        return defaultColor
    }

    // 4. Check for excessive length (more than 8 hex digits is invalid)
    if (hex.length > 8) {
        return defaultColor
    }

    // 5. Determine final 8-digit hex (AARRGGBB) based on input length
    val fullHex = when (hex.length) {
        in 1..6 -> {
            // Assume RGB input, pad to 6 digits, prepend FF alpha
            val paddedRgb = hex.padEnd(6, '0')
            "FF$paddedRgb"
        }
        7, 8 -> {
            // Assume ARGB input, pad to 8 digits
            hex.padEnd(8, '0')
        }
        else -> {
            // Should not happen due to initial checks, but handle defensively
            return defaultColor
        }
    }

    // 6. Parse the constructed 8-digit hex string
    // Since we validated characters and length, this should succeed
    val colorLong = fullHex.toLongOrNull(16) ?: return defaultColor

    // 7. Convert Long (ARGB) to Compose Color Int (ARGB)
    return Color(colorLong.toInt())
}


val `Floating Action Buttons` by story {
    val Density by parameter(LocalDensity.current)
    val `Container color (hex)` by parameter("705FA6")

    val bgColor = `Container color (hex)`.let {
        parseColorLeniently(it, MaterialTheme.colorScheme.primary)
    }

    CompositionLocalProvider(LocalDensity provides Density) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            SmallFloatingActionButton(onClick = {}, containerColor = bgColor) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            FloatingActionButton(onClick = {}, containerColor = bgColor) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            ExtendedFloatingActionButton(onClick = {}, containerColor = bgColor) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                Spacer(Modifier.padding(4.dp))
                Text("Extended")
            }
            LargeFloatingActionButton(onClick = {}, containerColor = bgColor) {
                Text("Large")
            }
        }
    }
}

val `Segmented buttons` by story {
    val selectedIndex = remember { mutableStateOf(0) }

    SingleChoiceSegmentedButtonRow {
        repeat(3) { index ->
            SegmentedButton(
                selected = index == selectedIndex.value,
                onClick = { selectedIndex.value = index },
                shape = SegmentedButtonDefaults.itemShape(index, 3)
            ) {
                Text("Button $index", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

val `Common buttons` by story {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        ElevatedButton(onClick = {}) {
            Text("Elevated Button")
        }

        Button(onClick = {}) {
            Text("Filled", softWrap = false)
        }

        FilledTonalButton(onClick = {}) {
            Text("Tonal", softWrap = false)
        }

        OutlinedButton(onClick = {}) {
            Text("Outlined", softWrap = false)
        }

        TextButton(onClick = {}) {
            Text("Text", softWrap = false)
        }
    }
}

