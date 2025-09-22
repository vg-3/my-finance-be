package com.dev.my_finance.service;

import com.dev.my_finance.dto.ReceiverCreateRequest;
import com.dev.my_finance.dto.ReceiverDto;
import com.dev.my_finance.dto.SuccessResponse;
import com.dev.my_finance.entity.Receiver;
import com.dev.my_finance.entity.User;
import com.dev.my_finance.exceptions.ResourceNotFoundException;
import com.dev.my_finance.repository.ReceiverRepository;
import com.dev.my_finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiverService {

    private final ReceiverRepository receiverRepository;

    private final UserRepository userRepository;

    public SuccessResponse createReceiver(ReceiverCreateRequest receiverCreateRequest){

        User user = userRepository.findById(receiverCreateRequest.lenderId())
                .orElseThrow(()-> new ResourceNotFoundException("Lender not found"));

        Receiver receiver = Receiver.builder()
                .name(receiverCreateRequest.name())
                .contact(receiverCreateRequest.contact())
                .lender(user)
                .loans(new ArrayList<>())
                .build();

        receiverRepository.save(receiver);

        return SuccessResponse.builder()
                .message("Receiver created successfully")
                .build();
    }

    public List<ReceiverDto> getReceiversByLenderId(Long lenderId){
        List<Receiver> receivers = receiverRepository.findByLenderId(lenderId);
        List<ReceiverDto> response = new ArrayList<>();
        for(Receiver receiver :receivers) {
            response.add(ReceiverDto.builder()
                    .id(receiver.getId())
                    .name(receiver.getName())
                    .contact(receiver.getContact())
                    .build());
        }
        return  response;
    }
}
