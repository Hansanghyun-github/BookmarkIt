package project.bookmark.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Directory;
import project.bookmark.Form.CreateForm;
import project.bookmark.Service.DirectoryService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookmarkControllerTest {

    @Autowired MockMvc mvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired DirectoryService directoryService;
    @Test
    public void createSuccess() throws Exception {
        // given
        List<Long> ids = new ArrayList<>();
        directoryService.findAll().forEach(it -> {
            ids.add(it.getId());
        });

        CreateForm createForm = new CreateForm();
        createForm.setUrl("https://www.google.com/");
        createForm.setName("google");
        createForm.setDirectoryId(ids.get(0));

        // when
        mvc.perform(MockMvcRequestBuilders
                    .post("/bookmarks")
                    .content(objectMapper.writeValueAsString(createForm))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("google"))
                .andExpect(jsonPath("$.url").value("https://www.google.com/"))
                .andExpect(jsonPath("$.directoryId").value(ids.get(0)));


        // then

    }

    @Test
    public void 생성_에러_BY_directoryId() throws Exception {
        // given
        List<Long> ids = new ArrayList<>();
        directoryService.findAll().forEach(it -> {
            ids.add(it.getId());
        });

        CreateForm createForm = new CreateForm();
        createForm.setUrl("https://www.google.com/");
        createForm.setName("google");
        createForm.setDirectoryId(100L);

        // when
        mvc.perform(MockMvcRequestBuilders
                        .post("/bookmarks")
                        .content(objectMapper.writeValueAsString(createForm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors.directoryId").value("Invalid Directory Id"))
                .andDo(print());


        // then
    }

    @Test
    public void 생성에러_BY_Validation() throws Exception {
        // given
        List<Long> ids = new ArrayList<>();
        directoryService.findAll().forEach(it -> {
            ids.add(it.getId());
        });

        CreateForm createForm = new CreateForm();
        createForm.setUrl("hps://www.google.com/");
        createForm.setDirectoryId(ids.get(0));

        // when
        mvc.perform(MockMvcRequestBuilders
                        .post("/bookmarks")
                        .content(objectMapper.writeValueAsString(createForm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors.url").value("must be a valid URL"));
    }
}