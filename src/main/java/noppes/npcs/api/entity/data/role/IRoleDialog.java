//

//

package noppes.npcs.api.entity.data.role;

public interface IRoleDialog {
	String getDialog();

	String getOption(final int p0);

	String getOptionDialog(final int p0);

	void setDialog(final String p0);

	void setOption(final int p0, final String p1);

	void setOptionDialog(final int p0, final String p1);
}
