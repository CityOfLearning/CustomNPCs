//

//

package noppes.npcs.api.entity.data;

public interface INPCDisplay {
	int getBossbar();

	String getCapeTexture();

	boolean getHasLivingAnimation();

	float[] getModelScale(final int p0);

	String getName();

	String getOverlayTexture();

	int getShowName();

	int getSize();

	String getSkinPlayer();

	String getSkinTexture();

	String getSkinUrl();

	int getTint();

	String getTitle();

	int getVisible();

	void setBossbar(final int p0);

	void setCapeTexture(final String p0);

	void setHashLivingAnimation(final boolean p0);

	void setModelScale(final int p0, final float p1, final float p2, final float p3);

	void setName(final String p0);

	void setOverlayTexture(final String p0);

	void setShowName(final int p0);

	void setSize(final int p0);

	void setSkinPlayer(final String p0);

	void setSkinTexture(final String p0);

	void setSkinUrl(final String p0);

	void setTint(final int p0);

	void setTitle(final String p0);

	void setVisible(final int p0);
}
