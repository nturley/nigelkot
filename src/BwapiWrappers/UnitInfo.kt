package BwapiWrappers
import BuildOrder.Buildable
import Jobs.Job
import Jobs.IdleJob

class UnitInfo (val id:Int, b:bwapi.Unit?) {
    lateinit var base:bwapi.Unit
    init {
        if (b!=null) base = b
    }

    var job:Job = IdleJob
        set(value) {
            field.stop(this)
            field = value
            value.start(this)
        }

    fun canBuild(buildable: Buildable):Boolean {
        if (base.isTraining || base.isUpgrading || base.isResearching || base.isMorphing) return false
        return buildable.canBuild(this)
    }

    fun build(buildable: Buildable) {
        buildable.build(this)
    }
}