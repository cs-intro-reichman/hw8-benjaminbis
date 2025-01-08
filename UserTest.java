/** If this user follows the given name, returns true; otherwise returns false. */
public boolean follows(String name) {
    for (int i = 0; i < fCount; i++) {
        if (follows[i].equals(name)) {
            return true;
        }
    }
    return false;
}

/** Makes this user follow the given name. If successful, returns true. 
 *  If this user already follows the given name, or if the follows list is full, does nothing and returns false; */
public boolean addFollowee(String name) {
    if (fCount >= maxfCount || follows(name)) {
        return false; // Cannot add if the list is full or already follows the name
    }
    follows[fCount] = name; // Add the new followee to the list
    fCount++; // Increment the count of followees
    return true;
}

/** Removes the given name from the follows list of this user. If successful, returns true.
 *  If the name is not in the list, does nothing and returns false. */
public boolean removeFollowee(String name) {
    for (int i = 0; i < fCount; i++) {
        if (follows[i].equals(name)) {
            // Shift all elements after the current one left to fill the gap
            for (int j = i; j < fCount - 1; j++) {
                follows[j] = follows[j + 1];
            }
            follows[fCount - 1] = null; // Clear the last slot
            fCount--; // Decrement the count of followees
            return true;
        }
    }
    return false; // Name not found
}

/** Counts the number of users that both this user and the other user follow.
 *  This is the size of the intersection of the two follows lists. */
public int countMutual(User other) {
    int count = 0;
    for (int i = 0; i < fCount; i++) {
        if (other.follows(follows[i])) {
            count++;
        }
    }
    return count;
}

/** Checks if this user is a friend of the other user.
 *  (if two users follow each other, they are said to be "friends.") */
public boolean isFriendOf(User other) {
    return this.follows(other.getName()) && other.follows(this.getName());
}
