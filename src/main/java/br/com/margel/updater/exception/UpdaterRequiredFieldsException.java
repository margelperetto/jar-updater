package br.com.margel.updater.exception;

import java.util.List;

@SuppressWarnings("serial")
public class UpdaterRequiredFieldsException extends Exception{

	private final List<String> nullRequiredFields;

	public UpdaterRequiredFieldsException(List<String> nullFields) {
		super("Complete the following fields -> "+nullFields);
		this.nullRequiredFields = nullFields;
	}

	public List<String> getNullRequiredFields() {
		return nullRequiredFields;
	}
}
