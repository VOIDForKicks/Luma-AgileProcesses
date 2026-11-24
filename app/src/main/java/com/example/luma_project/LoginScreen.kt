
package com.example.luma_project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    onSignUp: (String, String) -> Unit = { _, _ -> },
    onLogin: (String, String) -> Unit = { _, _ -> }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Luma",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSignUp) {
                "Create your account"
            } else {
                "Welcome back"
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("Email address") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text("Please enter a valid email address")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = passwordError,
            supportingText = {
                if (passwordError) {
                    Text("Password must be at least 6 characters")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email.trim())
                    .matches()

                passwordError = password.length < 6

                if (!emailError && !passwordError) {
                    if (isSignUp) {
                        onSignUp(email.trim(), password)
                    } else {
                        onLogin(email.trim(), password)
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isSignUp) "Create Account" else "Log In"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                isSignUp = !isSignUp
                emailError = false
                passwordError = false
            },
            enabled = !isLoading
        ) {
            Text(
                text = if (isSignUp) {
                    "Already have an account? Log in"
                } else {
                    "Don't have an account? Sign up"
                }
            )
        }
    }
}
