package com.arcao.menza;

public class DownloadException extends Exception {
	private static final long serialVersionUID = -6369642464553846131L;

	public DownloadException(String message) {
		super(message);
	}
	
	public DownloadException(Throwable cause) {
		super(cause);
	}
}
