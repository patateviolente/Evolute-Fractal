package fract.lang;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BottomPan {
	private static final String BUNDLE_NAME = "fract.lang.bottompan"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private BottomPan() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
