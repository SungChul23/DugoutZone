package com.kim.SpringStudy.service;

import com.kim.SpringStudy.domain.Item;
import com.kim.SpringStudy.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//상품 저장 메소드 생성
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    //상품 저장
    public void saveItem(String title, Integer price,String imageUrl){
        if(title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목이 비어있습니다.");
        }
        if(price == null || price<0){
            throw  new IllegalArgumentException("올바르지 않은 가격입니다.");
        }
        if(imageUrl == null || imageUrl.trim().isEmpty()){
            throw new IllegalArgumentException("이미지 URL이 비어있습니다.");
        }
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setImageUrl(imageUrl);
        itemRepository.save(item);
    }
    //상품 출력
    public List<Item> findItem(){
        return itemRepository.findAll();
    }

    //상품 수정
    public void editItem(Long id, String title, Integer price,String imageUrl){
        Optional<Item> editItem = itemRepository.findById(id);
        if (editItem.isPresent()) {
            Item item = editItem.get(); //기존 db 객체 사용
            item.setTitle(title);
            item.setPrice(price);
            item.setImageUrl(imageUrl);
            itemRepository.save(item);
        }
    }

    //상품 삭제
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }
}
