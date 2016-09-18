
package noppes.npcs.api.wrapper;

import net.minecraft.scoreboard.ScoreObjective;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardObjective;

public class ScoreboardObjectiveWrapper implements IScoreboardObjective {
	private ScoreObjective objective;

	protected ScoreboardObjectiveWrapper(ScoreObjective objective) {
		this.objective = objective;
	}

	@Override
	public String getCriteria() {
		return objective.getCriteria().getName();
	}

	@Override
	public String getDisplayName() {
		return objective.getDisplayName();
	}

	@Override
	public String getName() {
		return objective.getName();
	}

	@Override
	public boolean isReadyOnly() {
		return objective.getCriteria().isReadOnly();
	}

	@Override
	public void setDisplayName(String name) {
		if ((name.length() <= 0) || (name.length() > 32)) {
			throw new CustomNPCsException("Score objective display name must be between 1-32 characters: %s",
					new Object[] { name });
		}
		objective.setDisplayName(name);
	}
}
