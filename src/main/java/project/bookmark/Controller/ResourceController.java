package project.bookmark.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
public class ResourceController {

    @Value("${file.dir}")
    private String fileDir; // local file path

    @ResponseBody
    @GetMapping("/icon/{filepath}")
    public Resource downloadImage(@PathVariable String filepath) throws
            MalformedURLException {
        return new UrlResource("file:" + fileDir + filepath); // return image's resource
    }
}
