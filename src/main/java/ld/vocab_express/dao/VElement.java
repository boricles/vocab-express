package ld.vocab_express.dao;

public class VElement {
	
	String uri;
	String usage;
	
	public VElement() {
		
	}
	
	public VElement(String uri, String usage) {
		setUri(uri);
		setUsage(usage);
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	

}


