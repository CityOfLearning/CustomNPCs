//

//

package noppes.npcs.api.entity.data;

public interface INPCDisplay {
	int getBossbar();

	String getCapeTexture();

	boolean getHasLivingAnimation();

	float[] getModelScale(int p0);

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

	void setBossbar(int p0);

	void setCapeTexture(String p0);

	void setHashLivingAnimation(boolean p0);

	void setModelScale(int p0, float p1, float p2, float p3);

	void setName(String p0);

	void setOverlayTexture(String p0);

	void setShowName(int p0);

	void setSize(int p0);

	void setSkinPlayer(String p0);

	void setSkinTexture(String p0);

	void setSkinUrl(String p0);

	void setTint(int p0);

	void setTitle(String p0);

	void setVisible(int p0);
}
