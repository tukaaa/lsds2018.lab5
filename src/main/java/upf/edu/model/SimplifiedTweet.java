package upf.edu.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.Optional;

public class SimplifiedTweet implements Serializable {

    private static JsonParser parser = new JsonParser();
    private static Gson gson = new Gson();

    private final long tweetId;			// the id of the tweet ('id')
    private final String text;  		// the content of the tweet ('text')
    private final long userId;			// the user id ('user->id')
    private final String userName;		// the user name ('user'->'name')
    private final long followersCount;	// the number of followers ('user'->'followers_count')
    private final boolean isRetweeted;	// is it a retweet? (the object 'retweeted_status' exists?)
    private final Long retweetedUserId; // [if retweeted] ('retweeted_status'->'user'->'id')
    private final String retweetedUserName; // [if retweeted] ('retweeted_status'->'user'->'name')
    private final long timestampMs;		// seconds from epoch ('timestamp_ms')
    private final String lang;

    public SimplifiedTweet(long tweetId, String text, long userId, String userName,
                           long followersCount, boolean isRetweeted, Long retweetedUserId,
                           String retweetedUserName, long timestampMs, String lang) {
        this.tweetId = tweetId;
        this.text = text;
        this.userId = userId;
        this.userName = userName;
        this.followersCount = followersCount;
        this.isRetweeted = isRetweeted;
        this.retweetedUserName = retweetedUserName;
        this.retweetedUserId = retweetedUserId;
        this.timestampMs = timestampMs;
        this.lang = lang;
    }

    /**
     * Returns a {@link SimplifiedTweet} from a JSON String.
     * If parsing fails, for any reason, return an {@link Optional#empty()}
     *
     * @param jsonStr
     * @return an {@link Optional} of a {@link SimplifiedTweet}
     */
    public static Optional<SimplifiedTweet> fromJson(String jsonStr) {
        try {
            JsonObject json = parser.parse(jsonStr).getAsJsonObject();
            long tweetId = json.get("id").getAsLong();
            String text = json.get("text").getAsString();
            long userId = json.get("user").getAsJsonObject().get("id").getAsLong();
            String userName = json.get("user").getAsJsonObject().get("name").getAsString();
            long followersCount = json.get("user").getAsJsonObject().get("followers_count").getAsLong();
            boolean isRetweeted = json.has("retweeted_status");
            Optional<JsonObject> optionalUser = Optional
                    .ofNullable(json.get("retweeted_status"))
                    .map(JsonElement::getAsJsonObject)
                    .map(o -> o.get("user"))
                    .map(JsonElement::getAsJsonObject);
            Long retweetedUserId = optionalUser
                    .map(o -> o.get("id"))
                    .map(JsonElement::getAsLong)
                    .orElse(null);
            String retweetedUserName = optionalUser
                    .map(o -> o.get("name"))
                    .map(JsonElement::getAsString)
                    .orElse(null);
            long timestampMs = json.get("timestamp_ms").getAsLong();
            String lang = json.get("lang").getAsString();
            return Optional.of(new SimplifiedTweet(tweetId, text, userId, userName, followersCount,
                    isRetweeted, retweetedUserId, retweetedUserName, timestampMs, lang));
        } catch (Exception ise) {
            System.err.println("Error message: " + ise.getMessage());
            System.err.println("Unable to parse: " + jsonStr);
            return Optional.empty();
        }
    }

    public long getTweetId() {
        return tweetId;
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public Long getRetweetedUserId() {
        return retweetedUserId;
    }

    public String getRetweetedUserName() {
        return retweetedUserName;
    }

    public String getLang() {
        return lang;
    }
}

