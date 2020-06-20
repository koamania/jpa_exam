package jpabook.jpashop.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateItemDto {
    private final String name;
    private final int price;
    private final int stockQuantity;
}
