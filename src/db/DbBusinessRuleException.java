package db;

public class DbBusinessRuleException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DbBusinessRuleException (String msg) {
		super(msg);
	}
}