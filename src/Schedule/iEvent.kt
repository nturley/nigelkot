package Schedule


abstract class iEvent(val label: String) {
    abstract fun clear()
    abstract fun unsubscribe(label:String)
    abstract fun subscribe(
            label : String,
            invoke : () -> Unit,
            condition : () -> Boolean = { true},
            priority : Int = 0,
            oneShot : Boolean = false)
}