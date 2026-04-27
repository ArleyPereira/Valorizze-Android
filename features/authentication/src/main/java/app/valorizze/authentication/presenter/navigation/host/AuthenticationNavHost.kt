package app.valorizze.authentication.presenter.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.valorizze.authentication.presenter.features.login.screen.LoginScreen
import app.valorizze.authentication.presenter.features.recover.code.screen.RecoverCodeScreen
import app.valorizze.authentication.presenter.features.recover.email.screen.RecoverEmailScreen
import app.valorizze.authentication.presenter.features.recover.password.screen.RecoverPasswordScreen
import app.valorizze.authentication.presenter.features.signup.create.screen.SignupScreen
import app.valorizze.authentication.presenter.features.signup.validate.screen.ValidateSignupScreen
import app.valorizze.authentication.presenter.navigation.routes.AuthenticationRoutes
import app.valorizze.core.extensions.popBackStackSafely

@Composable
fun AuthenticationNavHost(
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = AuthenticationRoutes.Login(),
    ) {
        composable<AuthenticationRoutes.Login> {
            LoginScreen(
                navigateToSignupScreen = { navHostController.navigate(AuthenticationRoutes.Signup) },
                navigateToRecoverEmailScreen = { navHostController.navigate(AuthenticationRoutes.RecoverEmail) },
                navigateToValidateSignupScreen = { email, password, message ->
                    navHostController.navigate(
                        AuthenticationRoutes.Validate(
                            email = email,
                            password = password,
                            message = message,
                        )
                    )
                },
            )
        }

        composable<AuthenticationRoutes.Signup> {
            SignupScreen(
                navigateToValidateSignupScreen = { email, password, message ->
                    navHostController.navigate(
                        AuthenticationRoutes.Validate(
                            email = email,
                            password = password,
                            message = message,
                        )
                    ) {
                        popUpTo(AuthenticationRoutes.Signup) { inclusive = true }
                    }
                },
                onBackPressed = navHostController::popBackStackSafely,
            )
        }

        composable<AuthenticationRoutes.Validate> {
            ValidateSignupScreen(
                navigateToLoginScreen = { email, password, message ->
                    navHostController.navigate(
                        AuthenticationRoutes.Login(email = email, password = password, message = message)
                    ) {
                        popUpTo(AuthenticationRoutes.Login()) { inclusive = true }
                    }
                },
                onBackPressed = navHostController::popBackStackSafely,
            )
        }

        composable<AuthenticationRoutes.RecoverEmail> {
            RecoverEmailScreen(
                navigateToRecoverCodeScreen = { email, message ->
                    navHostController.navigate(AuthenticationRoutes.RecoverCode(email = email, message = message))
                },
                onBackPressed = navHostController::popBackStackSafely,
            )
        }

        composable<AuthenticationRoutes.RecoverCode> {
            RecoverCodeScreen(
                navigateToRecoverPasswordScreen = { email, code ->
                    navHostController.navigate(AuthenticationRoutes.RecoverPassword(email = email, code = code))
                },
                onBackPressed = navHostController::popBackStackSafely,
            )
        }

        composable<AuthenticationRoutes.RecoverPassword> {
            RecoverPasswordScreen(
                navigateToLoginScreen = { message ->
                    navHostController.navigate(AuthenticationRoutes.Login(message = message)) {
                        popUpTo(AuthenticationRoutes.Graph) { inclusive = true }
                    }
                },
                onBackPressed = {
                    navHostController.navigate(AuthenticationRoutes.RecoverEmail) {
                        popUpTo(AuthenticationRoutes.RecoverEmail) { inclusive = true }
                    }
                },
            )
        }
    }
}
