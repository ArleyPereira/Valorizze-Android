package app.valorizze.authentication.presenter.features.recover.password.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.valorizze.authentication.util.MainDispatcherRule
import app.valorizze.domain.usecase.remote.confirmation.ConfirmConfirmationUseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

class RecoverPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val confirmConfirmationUseCase: ConfirmConfirmationUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    private lateinit var vm: RecoverPasswordViewModel

    @Before
    fun setUp() {
        vm = RecoverPasswordViewModel(
            confirmConfirmationUseCase = confirmConfirmationUseCase,
            savedStateHandle = savedStateHandle
        )
    }

}