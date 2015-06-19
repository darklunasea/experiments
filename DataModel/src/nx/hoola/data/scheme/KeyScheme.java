package nx.hoola.data.scheme;

public enum KeyScheme
{
	// ========== Common ==========
	/**
	 * Indicator of Active Event List.
	 */
	ActiveEventListIndicator("@e", Type.NoID),

	// ========== Person ==========	
	/**
	 * All persons. Set of person_id.
	 */
	AllPersonsList("p:$all-p", Type.NoID),
	/**
	 * Person's location(s). Set of location_id.
	 */
	PersonLocations("p:<id>:$p-loc", Type.ReqID),
	/**
	 * Person's friends. Sorted Set of person_id.
	 */
	PersonFriends("p:<id>:$p-lnk", Type.ReqID),
	/**
	 * (Active) events the person attends. Set of event_id.
	 */
	PersonAttendEvents("p:<id>:$e-attnd-@e", Type.ReqID),	
	/**
	 * (Active) events the person marks as favorite. Set of event_id.
	 */
	PersonFavoriteEvents("p:<id>:$e-fav-@e", Type.ReqID),
	/**
	 * (Active) events the person is invited to. 
	 * Sorted Set of event_id. Score is based on interest score based on tags.
	 */
	PersonInvitedToEvents("p:<id>:$e-invt-@e", Type.ReqID),
	/**
	 * All events the person has attend/favorite/invited (including expired).
	 * Set of event_id.
	 */
	PersonAllEvents("p:<id>:$e-all", Type.ReqID),
	/**
	 * Person's interests (tags) with score on each interest. Sorted Set of
	 * <interest_tag, score>.
	 */
	PersonInterestGrid("p:<id>:$i-tags", Type.ReqID),
	/**
	 * Person's reputation score. Value (counter).
	 */
	PersonReputation("p:<id>:$p-rep", Type.ReqID),
	/**
	 * Recommended events to the person. Set of event_id.
	 */
	PersonRecommendationEvents("p:<id>:$e-recm-@e", Type.ReqID),

	// ========== Event ==========
	/**
	 * Event ID Counter.
	 */
	EventIdCounter("e:$id", Type.NoID),
	/**
	 * All active public events. Set of event_id.
	 */
	AllActivePublicEventsList("e:$all-pub-@e", Type.NoID),
	/**
	 * Event locations. Set of location_id.
	 */
	EventLocations("e:<id>:$e-loc", Type.ReqID),
	/**
	 * Event status. Bitmap of [0]active, [1]public.
	 */
	EventStatus("e:<id>:$e-status", Type.ReqID),
	/**
	 * Event's interests (tags). Set of interest_id (tag).
	 */
	EventInterestTags("e:<id>:$e-itags", Type.ReqID),
	/**
	 * Event editors. Set of person_id.
	 */
	EventEditors("e:<id>:$p-editor", Type.ReqID),
	/**
	 * Events that linked with the event. Set of event_id.
	 */
	EventLinkedEvents("e:<id>:$e-lnk", Type.ReqID),
	/**
	 * Persons that attend the event. Set of person_id.
	 */
	EventAttendedByPersons("e:<id>:$p-attnd", Type.ReqID),
	/**
	 * Persons that mark the event as favorite. Set of person_id.
	 */
	EventFavoritedByPersons("e:<id>:$p-fav", Type.ReqID),
	/**
	 * Basic score of the event. Value calculated by persons attend/fav.
	 */
	EventBasicScore("e:<id>:$e-score", Type.ReqID),
	/**
	 * Rating score of the event. Value calculated by persons attend and their
	 * rating.
	 */
	EventRatingScore("e:<id>:$e-rating", Type.ReqID),

	// ========== Interest ==========
	/**
	 * Rating score of the event. Value calculated by persons attend and their
	 * rating.
	 */
	AllInterestTagsList("i:$all-itags", Type.NoID),
	/**
	 * All events listed under the interest tag. Set of event_id.
	 */
	InterestTagEvents("i:<id>:$e-lst-@e", Type.ReqID),

	// ========== Time ==========
	/**
	 * All events that expired on the time. Set of event_id.
	 */
	TimeOfExpireEvents("t:<id>:$e-exp", Type.ReqID),
	/**
	 * All active public events listed on the time. Set of event_id.
	 */
	TimeOfActivePublicEvents("t:<id>:$e-pub-@e", Type.ReqID),

	// ========== Location ==========
	/**
	 * All locations. Set of location_id.
	 */
	AllLocations("l:$all-l", Type.NoID),
	/**
	 * All active public events at the location. Set of event_id.
	 */
	LocationOfActiveEvents("l:<id>:$e-lst-@e", Type.ReqID),
	
	;

	// Enum Helper

	private static final String ID_PLACE_HOLDER = "<id>";
	private String key;
	private Type type;

	private KeyScheme(String key, Type type)
	{
		this.key = key;
		this.type = type;
	}

	public String getKey(String id)
	{
		switch (type)
		{
		case NoID:
			return key;
		case ReqID:
			key.replaceFirst(ID_PLACE_HOLDER, id);
		default:
			return key;
		}
	}

	enum Type
	{
		NoID,
		ReqID,
	}
}
