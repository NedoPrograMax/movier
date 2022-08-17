package com.example.themovier.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovier.ui.navigation.MovierScreens
import com.example.themovier.ui.widgets.*
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    var showLoginForm by rememberSaveable {
        mutableStateOf(true)
    }

    var isLoginScreen by remember {
        mutableStateOf(true)
    }

    var userCreationError by remember {
        mutableStateOf<Result<Unit, Exception>?>(null)
    }

    var userLogInError by remember {
        mutableStateOf<Result<Unit, Exception>?>(null)
    }

    Scaffold(
        topBar = { MovierAppBar(title = if (showLoginForm) "Login" else "SignUp") }
    ) {
        Column {
            it
            if (showLoginForm) {
                UserForm(navController, isCreateAccount = false) { email, password ->

                    viewModel.signInWithEmailAndPassword(email, password)
                }
            } else {
                UserForm(navController, isCreateAccount = true) { email, password ->

                    viewModel.createUserWithEmailAndPassword(
                        email,
                        password,
                    )
                }
            }


            LaunchedEffect(Unit) {
                viewModel.action.collect { action ->
                    when (action) {
                        is LoginAction.ExceptionSignUp -> {
                            userCreationError = action.result
                        }
                        is LoginAction.ExceptionLogIn -> {
                            userLogInError = action.result
                        }
                    }
                }
            }
            if (isLoginScreen) {
                userCreationError?.let {
                    it.fold(
                        {
                            if (isLoginScreen) {
                                navController.navigate(MovierScreens.HomeScreen.name) {
                                    popUpTo(MovierScreens.LoginScreen.name) {
                                        inclusive = true
                                    }
                                    isLoginScreen = false
                                }
                            }

                        },
                        { e ->
                            showToast(context, e.message!!)

                        }
                    )
                    userCreationError = null
                }

                userLogInError?.let {
                    it.fold(
                        {
                            if (isLoginScreen) {
                                navController.navigate(MovierScreens.HomeScreen.name) {
                                    popUpTo(MovierScreens.LoginScreen.name) {
                                        inclusive = true
                                    }
                                    isLoginScreen = false
                                }
                            }

                        },
                        { e ->
                            if (isLoginScreen) {
                                showToast(context, e.message!!)
                            }
                        }
                    )
                    userLogInError = null
                }

            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm) "Sign Up" else "Login"
                Text(text = if (showLoginForm) "New User?" else "Already Have an Account?")
                Text(text = text,
                    modifier = Modifier
                        .clickable { showLoginForm = !showLoginForm }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondaryVariant)
            }

            if (viewModel.loading) {
                LoadingDialog()
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    navController: NavController,
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit,
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
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
        if (isCreateAccount) {
            Text(text = "Please enter valid email and password at least 6 letters long",
                modifier = Modifier.padding(4.dp))
        }
        EmailInput(emailState = email, enabled = true,
            onAction = KeyboardActions { passwordFocusRequest.requestFocus() })

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
            textId = if (isCreateAccount) "Create Account" else "Login",
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    validInputs: Boolean,
    onClick: () -> Unit,
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


