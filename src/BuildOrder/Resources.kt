package BuildOrder


class Resources (var minerals:Int, var gas:Int, var supply:Int) {
    fun add(other:Resources) {
        minerals += other.minerals
        gas += other.gas
        supply += other.supply

    }

    fun subtract(other:Resources) {
        minerals -= other.minerals
        gas -= other.gas
        supply -= other.supply
    }

    override fun toString(): String {
        return "(min: " + minerals + ", gas: " + gas + ", supply: " + supply+")"
    }
}