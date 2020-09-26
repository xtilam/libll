package com.dinz.library.common;

import org.springframework.data.util.Pair;

interface IAPIResultMessage {
    Pair<Integer, String> INSERT_SUCCESS = Pair.of(0, "Thêm thành công");
    Pair<Integer, String> ACCESS_DENIED = Pair.of(1, "Truy cập bị từ chối");
    Pair<Integer, String> INSERT_FAILED = Pair.of(1, "Thêm thất bại");
    Pair<Integer, String> SUCCESS = Pair.of(0, "Thành công");
    Pair<Integer, String> FAILED = Pair.of(1, "Thất bại");
    Pair<Integer, String> PAGE_OUT_INDEX = Pair.of(2, "Chỉ mục trang không phù hợp");
    Pair<Integer, String> UPDATE_SUCCESS = Pair.of(0, "Update thành công");
    Pair<Integer, String> UPDATE_FAILED = Pair.of(1, "Update thất bại");
    Pair<Integer, String> DELETE_SUCCESS = Pair.of(0, "Delete thành công");
    Pair<Integer, String> DELETE_FAILED = Pair.of(1, "Delete thất bại");
    Pair<Integer, String> NOT_FOUND_ITEM = Pair.of(1, "Không tìm thấy đối tượng tương ứng");
}