package com.web.fitmaster.service.Imp;
import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.exceptions.APIException;
import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.service.MemberService;
import org.springframework.stereotype.Service;


@Service
public class MemberServiceImp implements MemberService {

    private final UserRepository userRepository;

    public MemberServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new APIException("User not found"));

        user.setFullName(request.getName());
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public MemberDTOs.MemberDTO updateMember(Long id, MemberDTOs.MemberUpdate request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new APIException("User not found"));

        if(request.getName() != null) user.setFullName(request.getName());
        if(request.getPhone() != null) user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public String deleteMember(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new APIException("User not found with id: " + id));

        userRepository.delete(user);
        return "Member deleted successfully";
    }

    private MemberDTOs.MemberDTO mapToDTO(User user){
        MemberDTOs.MemberDTO dto = new MemberDTOs.MemberDTO();
        dto.setId(user.getId());
        dto.setName(user.getFullName());
        dto.setPhone(user.getPhone());
        return dto;
    }
}