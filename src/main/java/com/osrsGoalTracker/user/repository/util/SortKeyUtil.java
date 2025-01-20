package com.osrsGoalTracker.user.repository.util;

import java.time.Instant;

/**
 * Utility class for generating sort keys for DynamoDB items.
 */
public final class SortKeyUtil {
    private static final String METADATA = "METADATA";
    private static final String NOTIFICATION = "NOTIFICATION";
    private static final String CHARACTER = "CHARACTER";
    private static final String GOAL = "GOAL";
    private static final String LATEST = "LATEST";
    private static final String EARLIEST = "EARLIEST";

    public static final String CHARACTER_METADATA_PREFIX = CHARACTER + "#" + METADATA + "#";

    /**
     * Default constructor to prevent instantiation.
     */
    private SortKeyUtil() {
        // Prevent instantiation
    }

    /**
     * Gets the sort key for user metadata.
     *
     * @return The sort key for user metadata
     */
    public static String getUserMetadataSortKey() {
        return METADATA;
    }

    /**
     * Gets the sort key for goal metadata.
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @return The sort key for goal metadata
     */
    public static String getGoalMetadataSortKey(String characterName, String goalId) {
        return String.format("%s#%s#%s#%s#%s", CHARACTER, characterName, GOAL, METADATA, goalId);
    }

    /**
     * Gets the sort key for latest goal progress.
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @return The sort key for latest goal progress
     */
    public static String getLatestGoalProgressSortKey(String characterName, String goalId) {
        return String.format("%s#%s#%s#%s#%s", CHARACTER, characterName, GOAL, goalId, LATEST);
    }

    /**
     * Gets the sort key for earliest goal progress.
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @return The sort key for earliest goal progress
     */
    public static String getEarliestGoalProgressSortKey(String characterName, String goalId) {
        return String.format("%s#%s#%s#%s#%s", CHARACTER, characterName, GOAL, goalId, EARLIEST);
    }

    /**
     * Gets the sort key for notification channels.
     *
     * @param channelType The type of notification channel (e.g., SMS, Discord)
     * @return The sort key for the notification channel
     */
    public static String getNotificationChannelSortKey(String channelType) {
        return String.format("%s#%s", NOTIFICATION, channelType);
    }

    /**
     * Gets the sort key for character metadata.
     *
     * @param characterName The name of the character
     * @return The sort key for character metadata
     */
    public static String getCharacterMetadataSortKey(String characterName) {
        return String.format("%s#%s#%s", CHARACTER, METADATA, characterName);
    }

    /**
     * Builds the sort key for a goal's metadata record.
     * Format: CHARACTER#character_name#GOAL#METADATA#goal_id
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @return The sort key for the goal's metadata record
     */
    public static String buildGoalMetadataSortKey(String characterName, String goalId) {
        return String.format("CHARACTER#%s#GOAL#METADATA#%s", characterName, goalId);
    }

    /**
     * Builds the sort key for a goal's earliest progress record.
     * Format: CHARACTER#character_name#GOAL#goal_id#EARLIEST
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @return The sort key for the goal's earliest progress record
     */
    public static String buildGoalEarliestSortKey(String characterName, String goalId) {
        return String.format("CHARACTER#%s#GOAL#%s#EARLIEST", characterName, goalId);
    }

    /**
     * Builds the sort key for a goal progress record.
     * Format: CHARACTER#character_name#GOAL#goal_id#timestamp
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @param timestamp     The timestamp of the progress record
     * @return The sort key for the goal progress record
     */
    public static String buildGoalProgressSortKey(String characterName, String goalId, Instant timestamp) {
        return String.format("CHARACTER#%s#GOAL#%s#%s", characterName, goalId, timestamp.toString());
    }

    /**
     * Builds the sort key for a goal's latest progress record.
     * Format: CHARACTER#character_name#GOAL#goal_id#LATEST
     *
     * @param characterName The name of the character
     * @param goalId        The ID of the goal
     * @return The sort key for the goal's latest progress record
     */
    public static String buildGoalLatestSortKey(String characterName, String goalId) {
        return String.format("CHARACTER#%s#GOAL#%s#LATEST", characterName, goalId);
    }
}
