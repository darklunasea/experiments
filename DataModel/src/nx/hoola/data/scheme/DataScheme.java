package nx.hoola.data.scheme;

public enum DataScheme
{
	//Person
	P_NewFriendInitScore(1),
	P_AttendEventInterestScore(2),
	P_UnattendEventInterestScore(-2),
	P_FavEventInterestScore(1),
	P_UnfavEventInterestScore(-1),
	
	//Event
	E_AttendedRewardScore(2),
	E_UnattendedRewardScore(-2),
	E_FavedRewardScore(1),
	E_UnfavedRewardScore(-1),
	;

	// Enum Helper
	private Double value;

	private DataScheme(double value)
	{
		this.value = value;
	}

	public Double doubleValue()
	{
		return value;
	}

	public Integer intValue()
	{
		return value.intValue();
	}
	
	public Long longValue()
	{
		return value.longValue();
	}
}
