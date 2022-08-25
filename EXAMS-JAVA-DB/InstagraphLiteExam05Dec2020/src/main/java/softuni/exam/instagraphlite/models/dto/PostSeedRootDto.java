package softuni.exam.instagraphlite.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "posts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostSeedRootDto {
    @XmlElement(name = "post")
    private List<PostSeedDto>posts;


    public List<PostSeedDto> getPost() {
        return posts;
    }

    public void setPost(List<PostSeedDto> post) {
        this.posts = post;
    }
}
