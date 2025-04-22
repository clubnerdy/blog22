package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.reply.Reply;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DTO {
        private List<Board> boards;
        private Integer prev;
        private Integer next;

        // 페이징
        private Integer size;
        private Integer totalCount;
        private Integer totalPage;
        private Integer current;
        private Boolean isFirst; // currentPage
        private Boolean isLast; // totalCount, totalPage == current
        private List<Integer> numbers; // 20개 [1, 2, 3, 4, 5, 6, 7] -> model.numbers -> {{.}} 그냥 안에 있는거 뿌려줌

        public DTO(List<Board> boards, Integer current, Integer totalCount) {
            this.boards = boards;
            this.prev = current - 1;
            this.next = current + 1;
            this.size = 3; // 원래는 따로 빼서 처리하는데
            this.totalCount = totalCount; // 5로 given 먼저 해서 연산해보고
            this.totalPage = makeTotalPage(totalCount, size);
            this.isFirst = current == 0;
            this.isLast = (totalPage - 1) == current;
            this.numbers = makeNumbers(current, totalPage);
        }

        private Integer makeTotalPage(int totalCount, int size) {
            int rest = totalCount % size > 0 ? 1 : 0; //나머지값은 0~2를 순회함
            return totalCount / size + rest;
        }

        private List<Integer> makeNumbers(int current, int totalPage) {
            List<Integer> numbers = new ArrayList<>();

            int start = (current / 5) * 5;
            int end = Math.min(start + 5, totalPage);

            for (int i = start; i < end; i++) {
                numbers.add(i);
            }

            return numbers;
        }
    }

    // 상세보기 화면에 필요한 데이터
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner;
        private Boolean isLove;
        private Integer loveCount;
        private String username;
        private Timestamp createdAt;
        private Integer loveId;

        private List<ReplyDTO> replies;

        @Data
        // 내부 클래스로 만듬, 관리하기 편함
        public class ReplyDTO {
            private Integer id;
            private String content;
            private String username;
            private Boolean isOwner; // 댓글 삭제 기능때문에 있어야함

            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this.id = reply.getId();
                this.content = reply.getContent();
                this.username = reply.getUser().getUsername();
                this.isOwner = reply.getUser().getId().equals(sessionUserId);
            }
        }

        public DetailDTO(Board board, Integer sessionUserId, Boolean isLove, Integer loveCount, Integer loveId) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isPublic = board.getIsPublic();
            this.isOwner = sessionUserId == board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt();
            this.isLove = isLove;
            this.loveCount = loveCount;
            this.loveId = loveId;

            List<ReplyDTO> repliesDTO = new ArrayList<>();

            for (Reply reply : board.getReplies()) {
                ReplyDTO replyDTO = new ReplyDTO(reply, sessionUserId);
                repliesDTO.add(replyDTO);
            }

            this.replies = repliesDTO;
        }
    }

}
