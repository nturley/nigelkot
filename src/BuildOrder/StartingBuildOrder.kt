package BuildOrder

import LifeCycle.AI
import bwapi.Race
import bwapi.TechType
import bwapi.UnitType
import bwapi.UpgradeType

fun startingBuildOrder() {
    val q = BuildOrderExec.toBuildQ
    q.clear()
    /*
    9/10 - Supply Depot
11/18 - Barracks
12/18 - Send scouting SCV
14/18 - Marine[1]
15/18 - Command Center
15/18 - Refinery
15/18 - Marine
16/18 - Supply Depot[2]
21/26 - Factory
     */
    when (AI.game.self().race) {
        Race.Terran -> {
            BuildOrderExec.supplyUnit = BuildUnit(UnitType.Terran_Supply_Depot)
            val worker = BuildUnit(UnitType.Terran_SCV)
            for (i in 1..7)
                q.add(worker)
            // 11
            q.add(BuildUnit(UnitType.Terran_Barracks))
            for (i in 1..3)
                q.add(worker)
            //14
            q.add(BuildUnit(UnitType.Terran_Marine))
            //15
            q.add(BuildUnit(UnitType.Terran_Command_Center, BuildArea.NATURAL))
            q.add(BuildUnit(UnitType.Terran_Bunker, BuildArea.NATURAL_CHOKE))
            q.add(BuildUnit(UnitType.Terran_Refinery))
            q.add(BuildUnit(UnitType.Terran_Marine))
            // 16
            for (i in 1..5)
                q.add(worker)
            // 21
            q.add(BuildUnit(UnitType.Terran_Factory))
            q.add(BuildUnit(UnitType.Terran_Engineering_Bay))

            for (i in 1..3)
                q.add(BuildUnit(UnitType.Terran_Marine))
            q.add(BuildUnit(UnitType.Terran_Machine_Shop))
            q.add(BuildUnit(UnitType.Terran_Factory))
            q.add(BuildUnit(UnitType.Terran_Marine))
            q.add(BuildUnit(UnitType.Terran_Machine_Shop))
            q.add(BuildUnit(UnitType.Terran_Missile_Turret, BuildArea.NATURAL_CHOKE))
            q.add(BuildTech(TechType.Tank_Siege_Mode))
            q.add(BuildUnit(UnitType.Terran_Armory))

            q.add(BuildUnit(UnitType.Terran_Marine))
            q.add(BuildUnit(UnitType.Terran_Siege_Tank_Tank_Mode))
            q.add(BuildUnit(UnitType.Terran_Missile_Turret, BuildArea.NATURAL_CHOKE))
            for (i in 1..10) {
                q.add(BuildUnit(UnitType.Terran_Marine))
                q.add(BuildUnit(UnitType.Terran_Siege_Tank_Tank_Mode))
                q.add(BuildUnit(UnitType.Terran_Goliath))
            }
            q.add(BuildUnit(UnitType.Terran_Academy))
            q.add(BuildUnit(UnitType.Terran_Science_Facility))
            q.add(BuildUnit(UnitType.Terran_Refinery))
            q.add(BuildUnit(UnitType.Terran_Starport))

            for (i in 1..10) {
                q.add(BuildUnit(UnitType.Terran_Marine))
                q.add(BuildUnit(UnitType.Terran_Siege_Tank_Tank_Mode))
                q.add(BuildUnit(UnitType.Terran_Goliath))
            }
            q.add(BuildUnit(UnitType.Terran_Control_Tower))
            q.add(BuildUnit(UnitType.Terran_Comsat_Station))
            q.add(BuildUpgrade(UpgradeType.Terran_Vehicle_Plating, 0))
            q.add(BuildUpgrade(UpgradeType.Terran_Infantry_Weapons, 0))
        }
        Race.Protoss -> {
            /*
            8 - Pylon
10 - Gate
11 - Assimilator
13 - Cybernetics Core
15 - Pylon
18 - Dragoon (build continually from now on until you have 4-5)
20 - Dragoon Range Upgrade
21 - Pylon
26 - Robotics Facility (when 200 gas)
29 - Pylon
34 - Observatory; build Observer when it finishes
             */
            BuildOrderExec.supplyUnit = BuildUnit(UnitType.Protoss_Pylon)
            val worker = BuildUnit(UnitType.Protoss_Probe)
            val goon = BuildUnit(UnitType.Protoss_Dragoon)
            for (i in 1..6)
                q.add(worker)
            // 10
            q.add(BuildUnit(UnitType.Protoss_Gateway))
            q.add(worker)
            q.add(BuildUnit(UnitType.Protoss_Assimilator))
            q.add(worker)
            q.add(worker)
            //13
            q.add(BuildUnit(UnitType.Protoss_Cybernetics_Core))
            for (i in 1..4)
                q.add(worker)
            //18
            q.add(goon)
            //20
            q.add(BuildUpgrade(UpgradeType.Singularity_Charge, 1))
            q.add(worker)
            // 21
            q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Nexus, BuildArea.NATURAL))
            q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Forge))

            q.add(BuildUnit(UnitType.Protoss_Pylon, BuildArea.NATURAL_CHOKE))
            q.add(goon)
            q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Photon_Cannon, BuildArea.NATURAL_CHOKE))
            q.add(goon)
            q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Photon_Cannon, BuildArea.NATURAL_CHOKE))
            q.add(goon)
            q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Photon_Cannon, BuildArea.NATURAL_CHOKE))
            q.add(goon)
            q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Photon_Cannon, BuildArea.NATURAL_CHOKE))
            q.add(BuildUnit(UnitType.Protoss_Gateway))

            for (i in 1..10) q.add(goon)
            q.add(BuildUnit(UnitType.Protoss_Stargate))
            for (i in 1..10) q.add(goon)
        }
        Race.Zerg -> {
            BuildOrderExec.supplyUnit = BuildUnit(UnitType.Zerg_Overlord)
            val worker = BuildUnit(UnitType.Zerg_Drone)

            q.add(worker)
            q.add(BuildUnit(UnitType.Zerg_Spawning_Pool))
            for (i in 1..3) {
                q.add(worker)
                q.add(BuildUnit(UnitType.Zerg_Zergling))
            }
            q.add(BuildUnit(UnitType.Zerg_Hatchery, BuildArea.NATURAL))
            q.add(BuildUnit(UnitType.Zerg_Extractor))
            for (i in 1..10) {
                q.add(worker)
            }
            for (i in 1..3) {
                q.add(BuildUnit(UnitType.Zerg_Creep_Colony, BuildArea.NATURAL_CHOKE))
                q.add(BuildUnit(UnitType.Zerg_Sunken_Colony, BuildArea.NATURAL_CHOKE))
                q.add(BuildUnit(UnitType.Zerg_Zergling))
                q.add(worker)
            }
            q.add(BuildUnit(UnitType.Zerg_Evolution_Chamber))
            q.add(BuildUnit(UnitType.Zerg_Lair))
            for (i in 1..10) {
                q.add(BuildUnit(UnitType.Zerg_Zergling))
            }
            q.add(BuildUnit(UnitType.Zerg_Hydralisk_Den))
            for (i in 1..100) {
                q.add(BuildUnit(UnitType.Zerg_Hydralisk))
            }

        }
    }
}
