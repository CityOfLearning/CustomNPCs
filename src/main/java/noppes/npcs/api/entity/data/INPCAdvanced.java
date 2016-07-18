//

//

package noppes.npcs.api.entity.data;

public interface INPCAdvanced {
	String getLine(final int p0, final int p1);

	int getLineCount(final int p0);

	String getSound(final int p0);

	void setLine(final int p0, final int p1, final String p2, final String p3);

	void setSound(final int p0, final String p1);
}
