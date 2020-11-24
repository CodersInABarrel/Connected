package net.fabricmc.friend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
/*
    Please only touch this if you know what you are doing
 */

public class FriendList {
    private int playerId;
    private final Set<UUID> friends = new HashSet<>(); // holds accepted friends
    private final Set<UUID> requests = new HashSet<>(); // holds friend requests

    public Set<UUID> getFriends() {
        return this.friends;
    }

    public int getOwnerId() {
        return this.playerId;
    }

    public Set<UUID> getRequests() {
        return this.requests;
    }

    public boolean hasSentRequest(UUID targetId) {
        return this.requests.contains(targetId);
    }

    public boolean hasFriend(UUID target) {
        return this.friends.contains(target);
    }

    public void addFriend(UUID addedFriend) {
        this.friends.add(addedFriend);
    }

    public void removeRequest(UUID request) {
        this.requests.remove(request);
    }

    public boolean addRequest(UUID requesterId) {
        this.requests.add(requesterId);
        return true;
    }

    public void removeFriend(UUID removeId) { // only call this method if you are using the /friend remove command or you 100% know what you are doing
        this.friends.remove(removeId);
    }
}
