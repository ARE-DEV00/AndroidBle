package kr.co.are.androidble.ui.component


sealed interface Route {
    val path: String

    data object Connection : Route {
        override val path: String = "connection"
    }

    data object History : Route {
        override val path: String = "history"
    }

    data object Chart : Route {
        override val path: String = "chart"
    }

}