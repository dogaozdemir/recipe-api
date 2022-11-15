package com.dogaozdemir.recipeapi.repository;

import com.dogaozdemir.recipeapi.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InstructionRepository extends JpaRepository<Instruction,Long> {

}
