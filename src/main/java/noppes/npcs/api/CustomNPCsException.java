//

//

package noppes.npcs.api;

public class CustomNPCsException extends RuntimeException {
	public CustomNPCsException(final String message, final Object... obs) {
		super(String.format(message, obs));
	}
}
