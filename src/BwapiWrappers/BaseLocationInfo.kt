package BwapiWrappers

import Schedule.Cached
import Schedule.GameEvents
import Tracking.UnitTracker
import bwapi.Position
import bwapi.TilePosition
import bwta.BWTA
import bwta.BaseLocation
import bwta.Region




class BaseLocationInfo (val base:bwta.BaseLocation) {

    val isMineralOnly = Cached{ base.isMineralOnly }
    val region = Cached { RegionInfo.getRegion(base.region) }
    val minerals = Cached { base.minerals.map { UnitTracker.knownUnits[it.id] } }

    companion object {
        private val bases = mutableMapOf<String, BaseLocationInfo>()
        fun init() {
            bases.clear()
        }

        fun getBaseInfo(baseLocation: BaseLocation) :BaseLocationInfo {
            return bases.getOrPut(baseLocation.tilePosition.toString(), { BaseLocationInfo(baseLocation) })
        }

        fun getNearestBaseLocation(tilePosition: TilePosition):BaseLocationInfo {
            return getBaseInfo(BWTA.getNearestBaseLocation(tilePosition))
        }

        fun getNearestBaseLocation(position: Position):BaseLocationInfo {
            return getBaseInfo(BWTA.getNearestBaseLocation(position))
        }

        val baseLocations = Cached(GameEvents.start) { BWTA.getBaseLocations().map{ getBaseInfo(it) } }
    }
}