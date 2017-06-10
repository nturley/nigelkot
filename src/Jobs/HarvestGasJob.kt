package Jobs

import BwapiWrappers.UnitInfo


object HarvestGasJob : Job("HarvestGas", 2){
    override fun start(unitInfo: UnitInfo) {
        unitInfo.targetUnit?.let { unitInfo.base.gather(it.base) }

    }
}