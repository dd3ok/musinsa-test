package com.dd3ok.musinsatest.domain.brand;

import com.dd3ok.musinsatest.global.exception.BaseException;
import com.dd3ok.musinsatest.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 생성, 이름 수정, 삭제 가능
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자
public class Brand {
    private Long id;
    private String name;

    private Brand(String name) {
        validateName(name);
        this.name = name;
    }

    public static Brand create(String name) {
        return new Brand(name);
    }

    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}