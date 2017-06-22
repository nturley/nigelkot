package BwapiWrappers

import Schedule.Cached
import bwta.Region

class RegionInfo (val reg:bwta.Region){
    val id:String = reg.center.toString()

    val neighbors = Cached {
        reg.chokepoints.map {
            val reg = it.regions
            listOf(getRegion(reg.first), getRegion(reg.second))
        }.reduce {a,b -> a+b}.filter {
            it != this
        }
    }

    val chokes = Cached {
        reg.chokepoints.map { it.center }
    }

    fun graphDistance(other:RegionInfo):Int {
        if (this == other) return 0
        val visited = mutableSetOf(this)
        var distance = 1
        var toVisit = neighbors().toHashSet()
        while (toVisit.isNotEmpty()) {
            if (toVisit.contains(other)) return distance
            visited.addAll(toVisit)
            val nextVisit = toVisit
                    .map { it.neighbors() }
                    .reduce{a,b -> a + b}
                    .filter {r:RegionInfo -> !visited.contains(r) }
                    .toHashSet()
            distance += 1
            toVisit = nextVisit
        }
        return -1
    }


    companion object {
        private val regs = mutableMapOf<String, RegionInfo>()
        fun init() {
            regs.clear()
        }

        fun getRegion(reg:Region) :RegionInfo {
            return regs.getOrPut(reg.center.toString(), {RegionInfo(reg)})
        }

        fun getRegion(pos:bwapi.TilePosition) :RegionInfo {
            val reg = bwta.BWTA.getRegion(pos)
            return regs.getOrPut(reg.center.toString(), {RegionInfo(reg)})
        }

        fun getRegion(pos:bwapi.Position): RegionInfo {
            val reg = bwta.BWTA.getRegion(pos)
            return regs.getOrPut(reg.center.toString(), {RegionInfo(reg)})
        }
    }


}