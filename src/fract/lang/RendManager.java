package fract.lang;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class RendManager {
	private static final String BUNDLE_NAME = "fract.lang.renderManager"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private RendManager() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
