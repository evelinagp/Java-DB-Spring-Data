package softuni.exam.instagraphlite.models.dto;

import softuni.exam.instagraphlite.models.entity.Picture;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserSeedDto {
    private String username;
    private String password;
    private String profilePicture;

    public UserSeedDto() {
    }

    @NotBlank
    @Size(min = 2, max = 18)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Size(min = 4)
    @NotBlank
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NotBlank
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
