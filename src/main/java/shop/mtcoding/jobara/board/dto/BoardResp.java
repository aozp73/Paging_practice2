package shop.mtcoding.jobara.board.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardResp {
    @Getter
    @Setter
    public static class BoardListRespDto {
        private Integer id;
        private String title;
        private String companyName;
        private Integer userId;
        private String profile;
    }

    @Getter
    @Setter
    public static class MyBoardListRespDto {
        private Integer id;
        private String title;
        private String companyName;
        private Integer userId;
        private String profile;
    }

    @Getter
    @Setter
    public static class BoardMainRespDto {
        private Integer id;
        private String title;
        private String companyName;
        private Integer userId;
        private String profile;
    }

    @Getter
    @Setter
    public static class BoardUpdateRespDto {
        private Integer id;
        private String title;
        private String content;

        private Integer career;
        private String careerString;
        private Integer education;
        private String educationString;
        private Integer jobType;
        private String jobTypeString;

        private String favor;
        private Integer userId;
    }

    @Getter
    @Setter
    public static class BoardDetailRespDto {
        private Integer id;
        private String title;
        private String content;

        private Integer career;
        private String careerString;
        private Integer education;
        private String educationString;
        private Integer jobType;
        private String jobTypeString;

        private String favor;
        private Integer userId;
        private String companyName;
        private String companyScale;
        private String companyField;
        private String profile;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DetailDto {
        private Integer id;
        private String title;
        private String content;
        private Integer usersId;
        private Timestamp createdAt;
        private Integer loveCount;
        private boolean isLoved;
        private Integer lovesId;
    }

    @Setter
    @Getter
    public static class MainDto {
        private Integer id;
        private String title;
        private String username;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PagingDto {
        public static final int ROW = 8;
        private boolean isNotResult;
        private String keyword;
        private Integer blockCount; // ?????? ??????????????? ????????? ?????? ??????(5) 1-5, 6-10
        private Integer currentBlock; // ??????
        private Integer currentPage;
        private Integer startPageNum; // ?????? 1 -> 6 -> 11
        private Integer lastPageNum; // ?????? 5 -> 10 -> 15
        private Integer totalCount;
        private Integer totalPage;
        private boolean isLast; // getter??? ??????????????? isLast() ???????????? ????????????. -> el????????? last??? ??????
        private boolean isFirst; // getter??? ??????????????? isFirst() ???????????? ????????????. -> el????????? first??? ??????

        private List<BoardListRespDto> boardListDtos;

        public void makeBlockInfo(String keyword) {
            this.keyword = keyword;
            this.blockCount = 5;

            this.currentBlock = currentPage / blockCount;
            this.startPageNum = 1 + blockCount * currentBlock;
            this.lastPageNum = 5 + blockCount * currentBlock;
            this.totalPage = (int) Math.ceil((double) totalCount / ROW);
            if (this.currentPage == totalPage - 1) { // totalCount 14 / 5 - 1
                this.isLast = true;
            } else {
                this.isLast = false;
            }

            if (totalPage < lastPageNum) {
                this.lastPageNum = totalPage;
            }
        }
    }

}
