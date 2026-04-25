package app.valorizze.design.presenter.components.bottom.sheet.default

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.valorizze.design.presenter.theme.ColorScheme
import app.valorizze.design.presenter.theme.ShapeBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit = {},
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .navigationBarsPadding(),
        sheetState = sheetState,
        shape = ShapeBottomSheet,
        containerColor = ColorScheme.colorScheme.screen.backgroundSecondary,
        dragHandle = {},
        properties = ModalBottomSheetProperties(
            shouldDismissOnClickOutside = dismissOnClickOutside,
            shouldDismissOnBackPress = dismissOnBackPress
        ),
        content = content
    )
}

