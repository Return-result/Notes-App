package com.return_result.notes.miscellaneous

data class Operation(val textId: Int, val drawableId: Int, val operation: () -> Unit)