package com.example.demo.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class MerchantService {
    private final MerchantRepository merchantRepository;
    @Autowired
    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @GetMapping
    public List<Merchant> getMerchants(){
        return merchantRepository.findAll();
    }

    public void addNewMerchant(Merchant merchant) {
        merchantRepository.save(merchant);
    }
}
