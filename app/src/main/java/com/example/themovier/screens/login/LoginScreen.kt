package com.example.themovier.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.themovier.navigation.MovierScreens
import com.example.themovier.widgets.EmailInput
import com.example.themovier.widgets.MovierAppBar
import com.example.themovier.widgets.PasswordInput

@Composable
fun LoginScreen(
    navController: NavController,
viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    val context = LocalContext.current
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    Scaffold(
        topBar = {MovierAppBar(title = if(showLoginForm.value) "Login" else "SignUp")}
    ) {
        Column() {
        it
        if(showLoginForm.value) {
            UserForm(navController, isCreateAccount = false) { email, password ->
                viewModel.signInWithEmailAndPassword(email, password,
                    onFailure = {exception->
                        Toast.makeText(context, exception, Toast.LENGTH_LONG).show()}
                ){
                    navController.navigate(MovierScreens.HomeScreen.name)
                }
            }
        }
        else {
            UserForm(navController, isCreateAccount = true) { email, password ->
                viewModel.createUserWithEmailAndPassword(email, password, onFailure = {exception->
                    Toast.makeText(context, exception, Toast.LENGTH_LONG).show()}
                ){
                    navController.navigate(MovierScreens.HomeScreen.name)
                }
            }
        }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) "Sign Up" else "Login"
                Text(text = if (showLoginForm.value) "New User?" else "Already Have an Account?")
                Text(text = text,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondaryVariant)
            }
        }
        }
    }


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    navController: NavController,
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit
) {
    val email = rememberSaveable{
        mutableStateOf("")
    }
    val password = rememberSaveable{
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().length >= 6
    }

    val modifier = Modifier
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isCreateAccount){
            Text(text = "Please enter valid email and password at least 6 letters long",
            modifier = Modifier.padding(4.dp))
        }
        EmailInput(emailState = email, enabled = true,
            onAction = KeyboardActions {passwordFocusRequest.requestFocus()})

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = true,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
                keyboardController?.hide()
            }
        )

        SubmitButton(
            textId = if(isCreateAccount) "Create Account" else "Login",
            validInputs = valid
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = validInputs,
        shape = CircleShape
    ) {
        Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}

