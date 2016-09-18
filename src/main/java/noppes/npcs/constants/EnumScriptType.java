
package noppes.npcs.constants;

public enum EnumScriptType {
	INIT("init"), TICK("tick"), INTERACT("interact"), DIALOG("dialog"), DAMAGED("damaged"), DIED("died"), ATTACK_MELEE(
			"meleeAttack"), TARGET("target"), COLLIDE("collide"), KILL("kill"), DIALOG_OPTION(
					"dialog_option"), TARGET_LOST("targetLost"), ROLE("role"), RANGED_LAUNCHED(
							"rangedLaunched"), CLICKED("clicked"), FALLEN_UPON("fallenUpon"), RAIN_FILLED(
									"rainFilled"), BROKEN("broken"), HARVESTED("harvested"), EXPLODED(
											"exploded"), NEIGHBOR_CHANGED("neighborChanged"), REDSTONE(
													"redstone"), DOOR_TOGGLE("doorToggle"), TIMER("timer");

	public String function;

	private EnumScriptType(String function) {
		this.function = function;
	}
}
