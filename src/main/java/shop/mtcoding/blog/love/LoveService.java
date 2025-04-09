package shop.mtcoding.blog.love;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoveService {
    private final LoveRepository loveRepository;

    @Transactional
    public LoveResponse.SaveDTO 좋아요(LoveRequest.SaveDTO reqDTO, Integer sessionUserId) {
        Love lovePS = loveRepository.save(reqDTO.toEntity(sessionUserId));
        Long loveCount = loveRepository.findByBoardId(reqDTO.getBoardId());
        return new LoveResponse.SaveDTO(lovePS.getId(), loveCount.intValue());
    }

    @Transactional
    public LoveResponse.DeleteDTO 좋아요취소(Integer id) {
        Love lovePS = loveRepository.findById(id);
        if (lovePS == null) throw new RuntimeException("좋아요를 안했는데 취소를 어캐해");

        Integer boardId = lovePS.getBoard().getId();

        Long loveCount = loveRepository.findByBoardId(boardId);
        return new LoveResponse.DeleteDTO(loveCount.intValue());
    }
}
