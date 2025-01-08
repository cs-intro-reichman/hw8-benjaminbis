/** Represents a user in a social network. A user is characterized by a name,
 *  a list of user names that s/he follows, and the list's size. */
public class User {

    // Maximum number of users that a user can follow
    static int maxfCount = 10;

    private String name;       // name of this user
    private String[] follows;  // array of user names that this user follows
    private int fCount;        // actual number of followees (must be <= maxfCount)

    /** Creates a user with an empty list of followees. */
    public User(String name) {
        this.name = name;
        follows = new String[maxfCount];
        fCount = 0;
    }

    /** Creates a user with some followees. The only purpose of this constructor is 
     *  to allow testing the toString and follows methods, before implementing other methods. */
    public User(String name, boolean gettingStarted) {
        this(name);
        follows[0] = "Foo";
        follows[1] = "Bar";
        follows[2] = "Baz";
        fCount = 3;
    }

    /** Returns the name of this user. */
    public String getName() {
        return name;
    }

    /** Returns the follows array. */
    public String[] getfFollows() {
        return follows;
    }

    /** Returns the number of users that this user follows. */
    public int getfCount() {
        return fCount;
    }

    /** If this user follows the given name, returns true; otherwise returns false. */
    public boolean follows(String name) {
        for (int i = 0; i < fCount; i++) {
            if (follows[i] != null && follows[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** Makes this user follow the given name. If successful, returns true. 
     *  If this user already follows the given name, or if the follows list is full, does nothing and returns false; */
    public boolean addFollowee(String name) {
        if (fCount >= maxfCount || follows(name)) {
            return false;
        }
        follows[fCount] = name;
        fCount++;
        return true;
    }

    /** Removes the given name from the follows list of this user. If successful, returns true.
     *  If the name is not in the list, does nothing and returns false. */
    public boolean removeFollowee(String name) {
        for (int i = 0; i < fCount; i++) {
            if (follows[i] != null && follows[i].equals(name)) {
                for (int j = i; j < fCount - 1; j++) {
                    follows[j] = follows[j + 1];
                }
                follows[fCount - 1] = null;
                fCount--;
                return true;
            }
        }
        return false;
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

    /** Returns this user's name, and the names that s/he follows. */
    public String toString() {
        String result = name + " -> ";
        for (int i = 0; i < fCount; i++) {
            result += follows[i] + " ";
        }
        return result.trim();
    }
}

/** Represents a social network. The network has users, who follow other users.
 *  Each user is an instance of the User class. */
public class Network {

    // Fields
    private User[] users;  // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network  with some users. The only purpose of this constructor is 
     *  to allow testing the toString and getUser methods, before implementing other methods. */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }

    /** Finds in this network, and returns, the user that has the given name.
     *  If there is no such user, returns null.
     *  Notice that the method receives a String, and returns a User object. */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equalsIgnoreCase(name)) {
                return users[i];
            }
        }
        return null;
    }

    /** Adds a new user with the given name to this network.
    *  If this network is full, does nothing and returns false;
    *  If the given name is already a user in this network, does nothing and returns false;
    *  Otherwise, creates a new user with the given name, adds the user to this network, and returns true. */
    public boolean addUser(String name) {
        if (userCount >= users.length || getUser(name) != null) {
            return false;
        }
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    /** Makes the user with name1 follow the user with name2. If successful, returns true.
     *  If any of the two names is not a user in this network,
     *  or if the "follows" addition failed for some reason, returns false. */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null) {
            return false;
        }
        return user1.addFollowee(name2);
    }

    /** For the user with the given name, recommends another user to follow. The recommended user is
     *  the user that has the maximal mutual number of followees as the user with the given name. */
    public String recommendWhoToFollow(String name) {
        User currentUser = getUser(name);
        if (currentUser == null) {
            return null;
        }
        User recommendedUser = null;
        int maxMutual = 0;

        for (int i = 0; i < userCount; i++) {
            User otherUser = users[i];
            if (!currentUser.follows(otherUser.getName()) && !currentUser.getName().equals(otherUser.getName())) {
                int mutualCount = currentUser.countMutual(otherUser);
                if (mutualCount > maxMutual) {
                    maxMutual = mutualCount;
                    recommendedUser = otherUser;
                }
            }
        }
        return recommendedUser != null ? recommendedUser.getName() : null;
    }

    /** Computes and returns the name of the most popular user in this network: 
     *  The user who appears the most in the follow lists of all the users. */
    public String mostPopularUser() {
        String mostPopular = null;
        int maxCount = 0;

        for (int i = 0; i < userCount; i++) {
            int count = followeeCount(users[i].getName());
            if (count > maxCount) {
                maxCount = count;
                mostPopular = users[i].getName();
            }
        }
        return mostPopular;
    }

    /** Returns the number of times that the given name appears in the follows lists of all
     *  the users in this network. Note: A name can appear 0 or 1 times in each list. */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    public String toString() {
        StringBuilder result = new StringBuilder("Network:");
        for (int i = 0; i < userCount; i++) {
            result.append("\n").append(users[i].toString());
        }
        return result.toString();
    }
}
