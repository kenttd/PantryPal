@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kenttravis.pantrypal.PantryPalViewModelFactory
import com.kenttravis.pantrypal.sources.local.AuthManager
import com.kenttravis.pantrypal.ui.account.ProfileViewModel
import com.kenttravis.pantrypal.ui.chatbot.ChatbotViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.getValue

/*
Add these dependencies to your build.gradle (Module: app):

dependencies {
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
    implementation "androidx.compose.material3:material3:1.1.2"
    implementation "androidx.activity:activity-compose:1.8.0"
    implementation "androidx.navigation:navigation-compose:2.7.4"
    implementation "androidx.fragment:fragment-ktx:1.6.2"

    // For traditional navigation
    implementation "androidx.navigation:navigation-fragment-ktx:2.7.4"
    implementation "androidx.navigation:navigation-ui-ktx:2.7.4"
}
*/

// Data class to represent user data
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val dob: String,
    val is_activated: Int
)

@Composable
fun UserProfileScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "User Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Avatar and Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.username.first().uppercase(),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = user.username,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ID: ${user.id}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Activation Status
                    ActivationStatusChip(is_activated = user.is_activated == 1)
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                // User Details
                UserDetailItem(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = user.email
                )

                UserDetailItem(
                    icon = Icons.Default.DateRange,
                    label = "Date of Birth",
                    value = formatDate(user.dob)
                )

                UserDetailItem(
                    icon = Icons.Default.Person,
                    label = "Account Status",
                    value = if (user.is_activated == 1) "Active" else "Inactive"
                )
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { /* Handle edit */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Edit Profile")
            }

            OutlinedButton(
                onClick = { /* Handle settings */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Settings")
            }
        }
    }
}

@Composable
fun UserDetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ActivationStatusChip(is_activated: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (is_activated) Color(0xFF4CAF50) else Color(0xFFFF9800),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (is_activated) "Active" else "Inactive",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Helper function to format the date
fun formatDate(dateString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
        dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    } catch (e: Exception) {
        dateString
    }
}

class UserProfileFragment : Fragment() {
    private val vm by activityViewModels<ProfileViewModel> { PantryPalViewModelFactory }
    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the token and fetch data
        vm.getData(AuthManager.getToken(requireContext())!!)

        // Create and return the ComposeView
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the data and update the UI using the SAME ComposeView
        vm.data.observe(viewLifecycleOwner) { data ->
            data?.let {
                composeView.setContent {
                    MaterialTheme {
                        UserProfileScreen(user = it.user)
                    }
                }
            }
        }
    }
}