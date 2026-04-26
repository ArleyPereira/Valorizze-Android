package app.valorizze.authentication.presenter.features.recover.code.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.valorizze.authentication.util.MainDispatcherRule
import app.valorizze.domain.usecase.remote.confirmation.ValidateConfirmationUseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

class RecoverCodeViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val validateConfirmationUseCase: ValidateConfirmationUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    private lateinit var vm: RecoverCodeViewModel

    @Before
    fun setUp() {
        vm = RecoverCodeViewModel(
            validateConfirmationUseCase = validateConfirmationUseCase,
            savedStateHandle = savedStateHandle
        )
    }

}