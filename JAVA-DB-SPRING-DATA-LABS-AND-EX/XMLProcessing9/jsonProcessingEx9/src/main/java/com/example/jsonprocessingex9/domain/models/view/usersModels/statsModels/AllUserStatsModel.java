package com.example.jsonprocessingex9.domain.models.view.usersModels.statsModels;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllUserStatsModel {
    @XmlAttribute(name = "count")
    private int userCount;

    @XmlElement(name = "user")
    private List<UserStatsModel> users;

    public AllUserStatsModel() {
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<UserStatsModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserStatsModel> users) {
        this.users = users;
    }
}
