//

//

package noppes.npcs.constants;

public enum EnumParts {
	EARS("ears"), HORNS("horns"), HAIR("hair"), MOHAWK("mohawk"), SNOUT("snout"), BEARD("beard"), TAIL("tail"), CLAWS(
			"claws"), LEGS("legs"), FIN("fin"), SKIRT("skirt"), WINGS("wings"), HEAD("head"), BODY("body"), BREASTS(
					"breasts"), PARTICLES("particles"), ARM_LEFT(
							"armleft"), ARM_RIGHT("armright"), LEG_LEFT("legleft"), LEG_RIGHT("legright");

	public static EnumParts FromName(final String name) {
		for (final EnumParts e : values()) {
			if (e.name.equals(name)) {
				return e;
			}
		}
		return null;
	}

	public String name;

	public int patterns;

	private EnumParts(final String name) {
		patterns = 1;
		this.name = name;
	}
}