package Events

data class Until(val label: String, val event:Event<Unit>, val condition: () -> Boolean = {true})
