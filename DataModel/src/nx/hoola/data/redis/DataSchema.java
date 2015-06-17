package nx.hoola.data.redis;

public enum DataSchema
{
	//Person
	NewFriendInitScore(1),
	
	;

	// Enum Helper
	private Double value;

	private DataSchema(double value)
	{
		this.value = value;
	}

	public Integer intValue()
	{
		return value.intValue();
	}

	public Double doubleValue()
	{
		return value;
	}
}
