package com.javalab.boot.service;

import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.item.ItemRepository;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.ItemDto;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 상품 등록
    public void save(ItemDto itemDto, MultipartFile file, List<MultipartFile> additionalImages) throws Exception {
        Item item = ItemDto.dtoToEntity(itemDto); // ItemDto를 Item 엔터티로 변환

        if (file != null) {
            String projectPath = System.getProperty("user.dir") + File.separator + "files" + File.separator;
            log.info("projectPath : " + projectPath);
            File directory = new File(projectPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            item.setFilename(fileName);
            item.setFilepath("/files/" + fileName);
        } else {
            item.setFilepath("https://dummyimage.com/450x300/dee2e6/6c757d.jpg");
        }

// 추가 이미지 처리
        List<String> additionalImagePaths = new ArrayList<>();
        if (additionalImages != null) {
            for (MultipartFile additionalImage : additionalImages) {
                // 추가 이미지 파일을 저장하는 로직
                String projectPath = System.getProperty("user.dir") + File.separator + "files" + File.separator;
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + additionalImage.getOriginalFilename();
                File saveFile = new File(projectPath, fileName);

                additionalImage.transferTo(saveFile);
                additionalImagePaths.add("/files/" + fileName);
            }
        }
        // 아래 라인을 변경
        // item.setAdditionalImages(additionalImagePaths);
        item.setAdditionalImages(additionalImagePaths);
        item.setCount(item.getStock());
        item.setSoldout(true);

        itemRepository.save(item);
    }
    /*// 전체 상품 목록 조회
    public List<Item> itemList(){
        return itemRepository.findAll();
    }*/

    // 전체 상품 목록 조회
    public List<Item> itemList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return itemRepository.findAll(sort);
    }
    // 특정 상품 조회
    public ItemDto itemView(Long id){
        Optional<Item> result = itemRepository.findById(id);
        Item item = result.orElseThrow();
        ItemDto itemDto = Item.fromEntity(item);

        log.info("itemDto : " + itemDto);

        return itemDto;
    }
    // 특정 유저 상품 조회
    @Transactional
    public List<Item> userItemView(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + userDto.getId()));

        // User에 연결된 아이템 리스트 반환
        return user.getItems();
    }
    // 특정 상품 수정
    public void itemModify(ItemDto itemDto, Long id, MultipartFile file, List<MultipartFile> additionalImages)throws Exception{
        // 영속화 상태 아이템정보 가져오기
        Item existItem = itemRepository.findItemById(id);
        // 기존 이미지 파일 정보를 유지
        String existFileName = existItem.getFilename();
        String existFilePath = existItem.getFilepath();

        // 새로운 아이템 정보 엔티티 생성
        Item modifyItem = ItemDto.dtoToEntity(itemDto);

        // 새로운 이미지 파일이 전송 될 경우에만 처리
        if (file != null) {
        String projectPath = System.getProperty("user.dir") + File.separator + "files" + File.separator;
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath,fileName);
        file.transferTo(saveFile);

        modifyItem.setFilename(fileName);
        modifyItem.setFilepath("/files/" + fileName);
        } else {
            // 이미지 파일이 전송되지 않은 경우 기존 이미지 파일 정보를 사용
            modifyItem.setFilename(existFileName);
            modifyItem.setFilepath(existFilePath);
        }
        // 추가 이미지 처리
        List<String> additionalImagePaths = new ArrayList<>();
        if (additionalImages != null) {
            for (MultipartFile additionalImage : additionalImages) {
                // 추가 이미지 파일을 저장하는 로직
                String projectPath = System.getProperty("user.dir") + File.separator + "files" + File.separator;
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + additionalImage.getOriginalFilename();
                File saveFile = new File(projectPath, fileName);

                additionalImage.transferTo(saveFile);
                additionalImagePaths.add("/files/" + fileName);
            }
        }
        // 기존의 추가 이미지 파일 삭제
        deleteAdditionalImages(existItem.getAdditionalImages());
        // 새로운 추가 이미지로 업데이트
        modifyItem.setAdditionalImages(additionalImagePaths);

        // 기존 상품 정보에 새로운 상품 정보 업데이트
        existItem.updateItem(modifyItem);


        itemRepository.save(existItem);
    }


    // 기존 추가 이미지 파일 삭제 메소드
    private void deleteAdditionalImages(List<String> additionalImages) {
        if (additionalImages != null) {
            for (String imagePath : additionalImages) {
                String projectPath = System.getProperty("user.dir");
                String filePath = projectPath + File.separator + imagePath.replace("/files/", "");
                File file = new File(filePath);

                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    // 특정 상품 삭제
    public void itemDelete(Long id){
        itemRepository.deleteById(id);
    }

}
