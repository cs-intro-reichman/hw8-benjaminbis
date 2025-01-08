public class Network {

    private User[] users;
    private int userCount;

    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

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

    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equalsIgnoreCase(name)) {
                return users[i];
            }
        }
        return null;
    }

    public boolean addUser(String name) {
        if (userCount >= users.length || getUser(name) != null) {
            return false;
        }
        users[userCount++] = new User(name);
        return true;
    }

    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null) {
            return false;
        }
        if (name1.equalsIgnoreCase(name2)) {
            return false;
        }
        return user1.addFollowee(name2);
    }

    public String recommendWhoToFollow(String name) {
        User user = getUser(name);
        if (user == null) {
            return null;
        }

        String recommendation = null;
        int maxMutual = -1;

        for (int i = 0; i < userCount; i++) {
            User potential = users[i];
            if (!user.follows(potential.getName()) && !potential.getName().equals(name)) {
                int mutual = user.countMutual(potential);
                if (mutual > maxMutual) {
                    maxMutual = mutual;
                    recommendation = potential.getName();
                }
            }
        }
        return recommendation;
    }

    public String mostPopularUser() {
        String mostPopular = null;
        int maxCount = 0;

        for (int i = 0; i < userCount; i++) {
            String name = users[i].getName();
            int count = followeeCount(name);
            if (count > maxCount) {
                maxCount = count;
                mostPopular = name;
            }
        }
        return mostPopular;
    }

    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Network:");
        for (int i = 0; i < userCount; i++) {
            result.append("\n").append(users[i].toString());
        }
        return result.toString();
    }
}
