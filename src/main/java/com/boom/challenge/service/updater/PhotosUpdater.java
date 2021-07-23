package com.boom.challenge.service.updater;

import com.boom.challenge.model.File;
import com.boom.challenge.model.Order;
import com.boom.challenge.model.OrderState;
import com.boom.challenge.repository.FileRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhotosUpdater implements OrderUpdater {

    private final File file;
    private final FileRepository fileRepository;

    @Override
    public void updateOrder(Order order) {
        String photosUrl = this.fileRepository.storeFile(this.file);
        order.setPhotosUrl(photosUrl);
        order.setState(OrderState.UPLOADED);
    }
}
