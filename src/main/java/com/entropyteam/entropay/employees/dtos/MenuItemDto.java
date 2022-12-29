package com.entropyteam.entropay.employees.dtos;

import java.util.List;

public record MenuItemDto(String name,
                          String href,
                          String icon,
                          Integer key,
                          List<MenuItemDto> submenu) {

}
