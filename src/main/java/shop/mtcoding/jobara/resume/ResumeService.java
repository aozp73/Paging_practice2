package shop.mtcoding.jobara.resume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.jobara.common.ex.CustomException;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.resume.dto.ResumeReq.ResumeSaveReq;
import shop.mtcoding.jobara.resume.model.Resume;
import shop.mtcoding.jobara.resume.model.ResumeRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Transactional
    public void insertResume(Integer principalId, ResumeSaveReq resumeSaveReq) {
        Resume resume = new Resume(principalId, resumeSaveReq.getTitle(), resumeSaveReq.getContent());
        try {
            resumeRepository.insert(resume);
        } catch (Exception e) {
            throw new CustomException("서버 오류: 작성 실패 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Resume findById(Integer principalId, Integer resumeId) {
        Resume resumePS = resumeRepository.findById(resumeId);
        Verify.validateObject(resumePS, "존재하지 않는 게시물입니다.");
        if (principalId != resumePS.getUserId()) {
            throw new CustomException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        return resumePS;
    }

}
