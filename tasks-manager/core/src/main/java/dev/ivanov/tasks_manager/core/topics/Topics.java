package dev.ivanov.tasks_manager.core.topics;

public class Topics {

    //auth-service
    public static final String ACCOUNT_CREATED_EVENTS_TOPIC = "account-created-events-topic";
    public static final String ACCOUNT_DELETED_EVENTS_TOPIC = "account-deleted-events-topic";
    public static final String TOKEN_ADDED_TO_BLACKLIST_EVENTS_TOPIC = "token-added-to-blacklist-events-topic";
    public static final String ACCOUNT_CREATION_ROLLBACK_EVENTS_TOPIC = "account-creation-rollback-events-topic";
    public static final String ACCOUNT_DELETION_ROLLBACK_EVENTS_TOPIC = "account-deletion-rollback-events-topic";
    public static final String ACCOUNT_CREATION_COMMIT_EVENTS_TOPIC = "account-creation-commit-events-topic";
    public static final String ACCOUNT_DELETION_COMMIT_EVENTS_TOPIC = "account-deletion-commit-events-topic";

    //user-service
    public static final String USER_CREATED_EVENTS_TOPIC = "user-created-events-topic";
    public static final String USER_DELETED_EVENTS_TOPIC = "user-deleted-events-topic";
    public static final String USER_CREATE_EVENTS_TOPIC = "user-create-events-topic";
    public static final String USER_DELETE_EVENTS_TOPIC = "user-delete-events-topic";

}