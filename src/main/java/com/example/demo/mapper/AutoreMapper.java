package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.entityDTO.AutoreDTO;
import com.example.demo.model.entity.Autore;

@Mapper(componentModel = "spring")
public interface AutoreMapper {
	
	@Mapping(source ="nome", target="nomeAutore")
	AutoreDTO autoreToAutoreDto (Autore autore);
	
	@Mapping(source ="nomeAutore", target="nome")
	Autore autoreDtoToAutore (AutoreDTO autoreDTO);

}
