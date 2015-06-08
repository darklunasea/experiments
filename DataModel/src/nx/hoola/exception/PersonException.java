package nx.hoola.exception;

public class PersonException extends Exception
{
	ExceptionType type;
	Exception exception;
	
	public PersonException(ExceptionType type, String error, Exception e)
	{
		super(error, e);
		this.type = type;
	}
	
	public PersonException(ExceptionType type, String error)
	{
		super(error);
		this.type = type;
	}
}
