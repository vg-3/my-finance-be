package com.dev.my_finance.controller;

import com.dev.my_finance.dto.ReceiverCreateRequest;
import com.dev.my_finance.dto.ReceiverDto;
import com.dev.my_finance.dto.SuccessResponse;
import com.dev.my_finance.service.ReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receiver")
@RequiredArgsConstructor
public class ReceiverController {

    private final ReceiverService receiverService;

    @PostMapping()
    public SuccessResponse createReceiver(@RequestBody ReceiverCreateRequest receiver){
        return receiverService.createReceiver(receiver);
    }

    @GetMapping("/{lenderId}")
    public List<ReceiverDto> getReceiversByLenderId(@PathVariable Long lenderId){
        return receiverService.getReceiversByLenderId(lenderId);
    }

}
