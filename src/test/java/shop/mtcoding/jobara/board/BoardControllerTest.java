package shop.mtcoding.jobara.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.jobara.board.dto.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.jobara.board.dto.BoardResp.BoardDetailRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.BoardListRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.BoardMainRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.BoardUpdateRespDto;
import shop.mtcoding.jobara.board.dto.BoardResp.MyBoardListRespDto;
import shop.mtcoding.jobara.user.vo.UserVo;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;
    private MockHttpSession mockSession2;

    @BeforeEach
    public void setUp() {
        UserVo principal = new UserVo();
        principal.setId(6);
        // principal.setId(1);
        principal.setUsername("cos");
        principal.setRole("company");

        // principal.setRole("employee");

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", principal);
    }

    @Test
    public void myBoardList_test() throws Exception {
        // given
        // int id = 7; ????????? id:userId, ???????????? ?????? ??????
        int id = 6;

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/boardList/" + id).session(mockSession));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<MyBoardListRespDto> boardListDto = (List<MyBoardListRespDto>) map.get("myBoardList");
        // String model = om.writeValueAsString(boardListDto);
        // System.out.println("????????? : " + model);

        // then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void update_test() throws Exception {
        // given
        // int id = 3;
        int id = 1;

        BoardUpdateReqDto boardUpdateReqDto = new BoardUpdateReqDto();
        // boardUpdateReqDto.setId(3); ???????????? ??????
        // boardUpdateReqDto.setUserId(2);
        boardUpdateReqDto.setId(1);
        boardUpdateReqDto.setUserId(1);
        boardUpdateReqDto.setTitle("????????? ??????");
        boardUpdateReqDto.setContent("????????? ??????");
        boardUpdateReqDto.setCareerString("1????????? ~ 3?????????");
        boardUpdateReqDto.setEducationString("4??? ????????????");
        boardUpdateReqDto.setJobTypeString("?????????");
        boardUpdateReqDto.setFavor("?????? ???????????? ??????");
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(1, 3, 5, 7));
        boardUpdateReqDto.setCheckedValues(arrayList);

        String requestBody = om.writeValueAsString(boardUpdateReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                put("/board/update/" + id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession));

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void updateForm_test() throws Exception {
        // given
        // int id = 3; ?????? ???????????? ??????
        // int id = 10; ?????? ????????? ??????
        int id = 2;

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/updateForm/" + id)
                        .session(mockSession));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardUpdateRespDto boardDto = (BoardUpdateRespDto) map.get("boardDetail");

        // String boardmodel = om.writeValueAsString(boardDto);
        // System.out.println("????????? : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(boardDto.getCareerString()).isEqualTo("3????????? ~ 5?????????");
    }

    @Test
    public void save_test() throws Exception {
        // given
        String requestBody = "title=???????????????&content=???????????????&careerString=1????????? ~ 3?????????&educationString=4??? ????????????&jobTypeString=?????????&favor=?????????????????? ??????&userId=6&checkLang=2&checkLang=4&checkLang=6";

        // when
        ResultActions resultActions = mvc.perform(
                post("/board/save")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void saveForm_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/saveForm")
                        .session(mockSession));

        HttpSession session = resultActions.andReturn().getRequest().getSession();
        UserVo principal = (UserVo) session.getAttribute("principal");

        // then
        assertThat(principal.getUsername()).isEqualTo("cos");
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void certification_test() throws Exception {
        // given
        UserVo employeeUser = new UserVo();
        employeeUser.setId(1);
        employeeUser.setUsername("ssar");
        employeeUser.setRole("employee");

        mockSession2 = new MockHttpSession();
        mockSession2.setAttribute("principal", employeeUser);

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/saveForm")
                        .session(mockSession));
        // .session(mockSession2)); ???????????? ????????? ??? ???????????? ????????? ??????

        HttpSession session = resultActions.andReturn().getRequest().getSession();
        UserVo coPrincipal = (UserVo) session.getAttribute("principal");

        // then
        // assertThat(coPrincipal.getUsername()).isEqualTo("ssar");
        assertThat(coPrincipal.getUsername()).isEqualTo("cos");
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void boardDetail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/" + id));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto board = (BoardDetailRespDto) map.get("board");

        // String model = om.writeValueAsString(board);
        // System.out.println("????????? : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(board.getCompanyScale()).isEqualTo("?????????");
        assertThat(board.getCompanyField()).isEqualTo("IT???");
    }

    @Test
    public void boardList_test() throws Exception {
        // given
        String keyword = "lang";

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/list?keyword=lang")
                        .session(mockSession));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardListRespDto> boardList = (List<BoardListRespDto>) map.get("boardList");

        // String model = om.writeValueAsString(boardList);
        // System.out.println("????????? : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(boardList.get(0).getTitle()).isEqualTo("????????????1");
        assertThat(boardList.get(1).getTitle()).isEqualTo("????????????2");
    }

    @Test
    public void boardMainList_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/"));

        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardMainRespDto> boardList = (List<BoardMainRespDto>) map.get("boardMainList");

        // String model = om.writeValueAsString(boardList);
        // System.out.println("????????? : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(boardList.get(0).getTitle()).isEqualTo("????????????1");
        assertThat(boardList.get(1).getTitle()).isEqualTo("????????????2");
    }
}
