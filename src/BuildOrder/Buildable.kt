package BuildOrder

import BwapiWrappers.UnitInfo
import LifeCycle.AI
import bwapi.TechType
import bwapi.TilePosition
import bwapi.UnitType
import bwapi.UpgradeType


abstract class Buildable {
    abstract fun cost():Resources
    abstract fun whatBuilds():bwapi.UnitType
    abstract fun canBuild(unitInfo: UnitInfo):Boolean
    abstract fun build(unitInfo: UnitInfo)

    fun canAfford():Boolean {
        val self = AI.game.self()
        val reserved = AI.reserved
        val myCost = cost()
        if (self.minerals() - reserved.minerals < myCost.minerals) return false
        if (self.gas() - reserved.gas < myCost.gas) return false
        if (self.supplyTotal() - self.supplyUsed() < myCost.supply) return false
        return true
    }
}

class BuildUnit(val unitType:bwapi.UnitType) : Buildable() {

    var targetPos : TilePosition? = null

    override fun cost(): Resources {
        return Resources(unitType.mineralPrice(), unitType.gasPrice(), unitType.supplyRequired())
    }

    override fun whatBuilds(): UnitType {
        return unitType.whatBuilds().first
    }

    override fun canBuild(unitInfo: UnitInfo): Boolean {
        return unitInfo.base.canTrain(unitType)
    }

    override fun build(unitInfo: UnitInfo) {
        unitInfo.base.train(unitType)
    }
}

class BuildTech(val techType: TechType) : Buildable() {
    override fun cost(): Resources {
        return Resources(techType.gasPrice(), techType.gasPrice(), 0)
    }

    override fun whatBuilds(): UnitType {
        return techType.whatResearches()
    }

    override fun canBuild(unitInfo: UnitInfo): Boolean {
       return  unitInfo.base.canResearch(techType)
    }

    override fun build(unitInfo: UnitInfo) {
        unitInfo.base.research(techType)
    }
}

class BuildUpgrade(val upgradeType: UpgradeType, val level:Int): Buildable() {
    override fun cost(): Resources {
        return Resources(upgradeType.mineralPrice(level), upgradeType.gasPrice(level), 0)
    }

    override fun whatBuilds(): UnitType {
        return upgradeType.whatUpgrades()
    }

    override fun canBuild(unitInfo: UnitInfo): Boolean {
        return unitInfo.base.canUpgrade(upgradeType)
    }

    override fun build(unitInfo: UnitInfo) {
        unitInfo.base.upgrade(upgradeType)
    }
}