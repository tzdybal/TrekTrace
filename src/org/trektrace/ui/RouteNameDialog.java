package org.trektrace.ui;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;

import org.trektrace.entities.Route;

public class RouteNameDialog extends Dialog {
	private EditField nameField = new EditField();

	public RouteNameDialog(Route r) {
		this(r, Dialog.D_OK_CANCEL);
	}

	public RouteNameDialog(Route r, int type) {
		super(type, "Edit route name", Dialog.OK, null, Screen.FIELD_BOTTOM);
		nameField.setText(r.getName());
		add(nameField);
	}

	public String getText() {
		return nameField.getText();
	}
}
