package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception403;
import shop.mtcoding.blog._core.error.ex.Exception404;
import shop.mtcoding.blog.love.Love;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final LoveRepository loveRepository;
    private final ReplyRepository replyRepository;

    // TODO
    @Transactional
    public void 글삭제(Integer id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id);

        if (boardPS == null) throw new Exception404("게시글이 존재하지 않습니다.");

        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("권한이 없습니다.");
        }

        boardRepository.delete(boardPS);
    }

    public BoardResponse.DTO 글목록보기(Integer userId, Integer page, String keyword) {
        if (userId == null) {
            Long totalCount = boardRepository.totalCount(keyword);
            List<Board> boards = boardRepository.findAll(page, keyword);
            return new BoardResponse.DTO(boards, page, totalCount.intValue(), keyword);
        } else {
            Long totalCount = boardRepository.totalCount(userId, keyword);
            List<Board> boards = boardRepository.findAll(userId, page, keyword);
            return new BoardResponse.DTO(boards, page, totalCount.intValue(), keyword);
        }
    }

    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        Board board = saveDTO.toEntity(sessionUser);
        boardRepository.save(board);
    }

    public BoardResponse.DetailDTO 글상세보기(Integer id, Integer userId) {
        Board boardPS = boardRepository.findByIdJoinUserReplies(id);

        Love love = loveRepository.findByUserIdAndBoardId(userId, id);
        Long loveCount = loveRepository.findByBoardId(id);

        Integer loveId = love == null ? null : love.getId();
        Boolean isLove = love == null ? false : true;

        BoardResponse.DetailDTO detailDTO = new BoardResponse.DetailDTO(boardPS, userId, isLove, loveCount.intValue(), loveId);
        return detailDTO;
    }

    public Board 업데이트글보기(int id, Integer sessionUserId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) throw new Exception404("자원을 찾을 수 없습니다");

        if (!boardPS.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("권한이 없습니다");
        }
        return null;
    }

    @Transactional
    public void 글수정하기(BoardRequest.UpdateDTO reqDTO, Integer boardId, User sessionUser) {
        Board boardPS = boardRepository.findById(boardId);

        if (boardPS == null) throw new Exception404("자원을 찾을 수 없습니다.");
        if (!boardPS.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403("권한이 없습니다.");
        }


        boardPS.update(reqDTO.getTitle(), reqDTO.getContent(), reqDTO.getIsPublic());
    }
}
