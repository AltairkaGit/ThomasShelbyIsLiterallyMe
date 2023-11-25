package com.thomas.modules.music.dto;

import java.util.List;

public class TracksRequestDto {
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
