package com.asalavei.tennisscoreboard.dbaccess.mapper;

import com.asalavei.tennisscoreboard.dbaccess.entities.PlayerEntity;
import com.asalavei.tennisscoreboard.dto.Player;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerEntityMapper extends EntityMapper<Player, PlayerEntity> {
}
