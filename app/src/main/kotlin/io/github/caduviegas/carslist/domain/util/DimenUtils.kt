package io.github.caduviegas.carslist.domain.util

import androidx.compose.ui.unit.Dp

fun Boolean.then(ifTrue: Dp, ifFalse: Dp) = if (this) ifTrue else ifFalse
