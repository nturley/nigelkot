package BwapiWrappers
import BuildOrder.Buildable
import Jobs.Job
import Jobs.IdleJob

class UnitInfo (val id:Int, val base :bwapi.Unit) {

    var job:Job = IdleJob
        set(value) {
            field.stop(this)
            field = value
            value.start(this)
        }

    var targetUnit:UnitInfo? = null

    fun canBuild(buildable: Buildable):Boolean {
        if (base.isTraining || base.isUpgrading || base.isResearching || base.isMorphing) return false
        return buildable.canBuild(this)
    }

    fun build(buildable: Buildable) {
        buildable.build(this)
    }
}