package kr.co.are.androidble.ui.component


sealed interface Route {
    val path: String

    data object Connection : Route {
        override val path: String = "connection"
    }

    data object Data : Route {
        override val path: String = "data"
    }

    data object Chart : Route {
        override val path: String = "chart"
    }

}