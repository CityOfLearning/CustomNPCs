//

//

package noppes.npcs.api.entity.data;

public interface IData {
	void clear();

	Object get(final String p0);

	boolean has(final String p0);

	void put(final String p0, final Object p1);

	void remove(final String p0);
}
