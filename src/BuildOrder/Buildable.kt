package BuildOrder

import BwapiWrappers.UnitInfo
import LifeCycle.AI
import Schedule.GameEvents
import Schedule.Until
import bwapi.TechType
import bwapi.TilePosition
import bwapi.UnitType
import bwapi.UpgradeType

enum class BuildArea {
    MAIN, MAIN_CHOKE, NATURAL, NATURAL_CHOKE, EXPAND, CONTAIN, SNEAK
}

abstract class Buildable {
    abstract fun cost():Resources
    abstract fun whatBuilds():bwapi.UnitType
    abstract fun canBuild(unitInfo: UnitInfo):Boolean
    abstract fun build(unitInfo: UnitInfo)

    open fun supplyProvided(): Int {
        return 0
    }

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

class BuildUnit(val unitType:bwapi.UnitType, val area:BuildArea = BuildArea.MAIN) : Buildable() {

    override fun cost(): Resources {
        return Resources(unitType.mineralPrice(), unitType.gasPrice(), unitType.supplyRequired())
    }

    override fun whatBuilds(): UnitType {
        return unitType.whatBuilds().first
    }

    override fun canBuild(unitInfo: UnitInfo): Boolean {
        if (unitType.isAddon) {
            return unitInfo.base.canBuild(unitType)
        }
        return unitInfo.base.canTrain(unitType)
    }

    override fun build(unitInfo: UnitInfo) {
        if (unitType.isAddon) {
            Schedule.run("build addon") {
                while (unitInfo.base.isIdle) {
                    unitInfo.base.buildAddon(unitType)
                    yield(Until(GameEvents.frame))
                }
            }

        } else {
            unitInfo.base.train(unitType)
        }
    }

    override fun supplyProvided(): Int {
        return unitType.supplyProvided()
    }

    override fun toString(): String {
        return unitType.toString()
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

    override fun toString(): String {
        return techType.toString()
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

    override fun toString(): String {
        return upgradeType.toString()
    }
}