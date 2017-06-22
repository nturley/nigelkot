package Intelligence

import BwapiWrappers.BaseLocationInfo
import BwapiWrappers.RegionInfo
import LifeCycle.AI


object MapTools {

    /**
     * Returns neighboring regions with a reasonable base location
     */
    fun findNeighboringBasePositions(regionInfo: RegionInfo = AI.startRegion): List<BaseLocationInfo> {
       return BaseLocationInfo.baseLocations()
                .filter { regionInfo.neighbors().contains(it.region()) }
                .filter { !it.isMineralOnly() }
    }
}